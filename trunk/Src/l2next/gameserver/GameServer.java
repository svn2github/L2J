package l2next.gameserver;

import l2next.commons.lang.StatsUtils;
import l2next.commons.listener.Listener;
import l2next.commons.listener.ListenerList;
import l2next.commons.net.nio.impl.SelectorThread;
import l2next.commons.versioning.Version;
import l2next.gameserver.cache.CrestCache;
import l2next.gameserver.dao.CharacterDAO;
import l2next.gameserver.dao.ItemsDAO;
import l2next.gameserver.data.BoatHolder;
import l2next.gameserver.data.xml.Parsers;
import l2next.gameserver.data.xml.holder.EventHolder;
import l2next.gameserver.data.xml.holder.ResidenceHolder;
import l2next.gameserver.data.xml.holder.StaticObjectHolder;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.geodata.GeoEngine;
import l2next.gameserver.handler.admincommands.AdminCommandHandler;
import l2next.gameserver.handler.items.ItemHandler;
import l2next.gameserver.handler.usercommands.UserCommandHandler;
import l2next.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2next.gameserver.idfactory.IdFactory;
import l2next.gameserver.instancemanager.*;
import l2next.gameserver.instancemanager.commission.CommissionShopManager;
import l2next.gameserver.instancemanager.games.FishingChampionShipManager;
import l2next.gameserver.instancemanager.games.LotteryManager;
import l2next.gameserver.instancemanager.games.MiniGameScoreManager;
import l2next.gameserver.instancemanager.itemauction.ItemAuctionManager;
import l2next.gameserver.instancemanager.naia.NaiaCoreManager;
import l2next.gameserver.instancemanager.naia.NaiaTowerManager;
import l2next.gameserver.listener.GameListener;
import l2next.gameserver.listener.game.OnShutdownListener;
import l2next.gameserver.listener.game.OnStartListener;
import l2next.gameserver.loginservercon.LoginServerCommunication;
import l2next.gameserver.model.AutoChatHandler;
import l2next.gameserver.model.*;
import l2next.gameserver.model.entity.Hero;
import l2next.gameserver.model.entity.MonsterRace;
import l2next.gameserver.model.entity.olympiad.Olympiad;
import l2next.gameserver.model.party.PartySubstitute;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.GamePacketHandler;
import l2next.gameserver.scripts.Scripts;
import l2next.gameserver.tables.AugmentationData;
import l2next.gameserver.tables.ClanTable;
import l2next.gameserver.tables.EnchantHPBonusTable;
import l2next.gameserver.tables.PetSkillsTable;
import l2next.gameserver.tables.SkillTreeTable;
import l2next.gameserver.tables.SubClassTable;
import l2next.gameserver.taskmanager.ItemsAutoDestroy;
import l2next.gameserver.taskmanager.TaskManager;
import l2next.gameserver.taskmanager.tasks.RestoreOfflineTraders;
import l2next.gameserver.utils.*;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;

public class GameServer
{
	public static final int LOGIN_SERVER_PROTOCOL = 2;
	private static final Logger _log = LoggerFactory.getLogger(GameServer.class);
	public static GameServer _instance;
	private final SelectorThread<GameClient> _selectorThreads[];
	private final GameServerListenerList _listeners;
	private int _serverStarted;

	@SuppressWarnings("unchecked")
	public GameServer() throws Exception
	{
		long serverLoadStart = System.currentTimeMillis();
		_instance = this;
		_serverStarted = time();
		_listeners = new GameServerListenerList();

		new File("./log/").mkdir();
		
		// Initialize config
		Config.load();
		// Check binding address
		checkFreePorts();
		// Initialize database
		LockerManager.getInstance();
		Class.forName(Config.DATABASE_DRIVER).newInstance();
		DatabaseFactory.getInstance().getConnection().close();

		IdFactory _idFactory = IdFactory.getInstance();
		if(!_idFactory.isInitialized())
		{
			_log.error("Could not read object IDs from DB. Please Check Your Data.");
			throw new Exception("Could not initialize the ID factory");
		}

		CacheManager.getInstance();

		ThreadPoolManager.getInstance();

		Scripts.getInstance();
		GeoEngine.loadGeo();

		Strings.reload();

		GameTimeController.getInstance();

		World.init();

		Parsers.parseAll();

		ItemsDAO.getInstance();

		CrestCache.getInstance();

		CharacterDAO.getInstance();

		ClanTable.getInstance();

		SkillTreeTable.getInstance();

		AugmentationData.getInstance();

		PetSkillsTable.getInstance();

		EnchantHPBonusTable.getInstance();

		ItemAuctionManager.getInstance();

		SpawnManager.getInstance().spawnAll();

		StaticObjectHolder.getInstance().spawnAll();

		RaidBossSpawnManager.getInstance();

		Scripts.getInstance().init();

		Announcements.getInstance();

		LotteryManager.getInstance();

		PlayerMessageStack.getInstance();

		if(Config.AUTODESTROY_ITEM_AFTER > 0)
		{
			ItemsAutoDestroy.getInstance();
		}

		MonsterRace.getInstance();

		AutoChatHandler _autoChatHandler = AutoChatHandler.getInstance();
		_log.info("AutoChatHandler: Loaded " + _autoChatHandler.size() + " handlers in total.");

		if(Config.ENABLE_OLYMPIAD)
		{
			Olympiad.load();
			Hero.getInstance();
		}

		PetitionManager.getInstance();

		CursedWeaponsManager.getInstance();

		if(!Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
			_log.info("CoupleManager initialized");
		}

		ItemHandler.getInstance();

		ServerPacketOpCodeManager.getInstance();

		AdminCommandHandler.getInstance().log();
		UserCommandHandler.getInstance().log();
		VoicedCommandHandler.getInstance().log();

		TaskManager.getInstance();

		_log.info("=[Events]=========================================");
		ResidenceHolder.getInstance().callInit();
		EventHolder.getInstance().callInit();
		_log.info("==================================================");

		BoatHolder.getInstance().spawnAll();
		CastleManorManager.getInstance();

		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

		_log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());

		CoupleManager.getInstance();

		if(Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
		{
			FishingChampionShipManager.getInstance();
		}

		HellboundManager.getInstance();

		NaiaTowerManager.getInstance();
		NaiaCoreManager.getInstance();

		HarnakUndegroundManager.getInstance();

		MiniGameScoreManager.getInstance();

		/************** Durandia **************/
		if(Config.L2TOP_MANAGER_ENABLED)
		{
			L2TopManager.getInstance();
		}
		if(Config.MMO_TOP_MANAGER_ENABLED)
		{
			MMOTopManager.getInstance();
		}
		CommissionShopManager.getInstance();
		AwakingManager.getInstance();
		PartySubstitute.getInstance();
		/************** Durandia **************/
		ArcanManager.getInstance();
		WorldStatisticsManager.getInstance();
		DimensionalRiftManager.getInstance();
		ToIManager.getInstance();
		ParnassusManager.getInstance();
		BaltusManager.getInstance();
		SubClassTable.getInstance();
		PremiumIpManager.getInstance();
		SoAManager.getInstance();
		SoDManager.getInstance();
		SoHManager.getInstance();
		SoIManager.getInstance();
		KaynakVillageManager.getInstance();
		ChaosInviteManager.getInstance();
		InstanceHistoryManager.getInstance();

		if(Config.GARBAGE_COLLECTOR_INTERVAL > 0)
		{
			Class.forName(GarbageCollector.class.getName());
		}

		Shutdown.getInstance().schedule(Config.RESTART_AT_TIME, Shutdown.RESTART);

		_log.info("GameServer Started");
		_log.info("Maximum Numbers of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);

		GamePacketHandler gph = new GamePacketHandler();

		InetAddress serverAddr = Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*") ? null : InetAddress.getByName(Config.GAMESERVER_HOSTNAME);

		_selectorThreads = new SelectorThread[Config.PORTS_GAME.length];
		for(int i = 0; i < Config.PORTS_GAME.length; i++)
		{
			_selectorThreads[i] = new SelectorThread<>(Config.SELECTOR_CONFIG, gph, gph, gph, null);
			_selectorThreads[i].openServerSocket(serverAddr, Config.PORTS_GAME[i]);
			_selectorThreads[i].start();
		}

		LoginServerCommunication.getInstance().start();

		if(Config.SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART)
		{
			ThreadPoolManager.getInstance().schedule(new RestoreOfflineTraders(), 30000L);
		}

		getListeners().onStart();

		_log.info("=================================================");
		String memUsage = String.valueOf(StatsUtils.getMemUsage());
		for(String line : memUsage.split("\n"))
		{
			_log.info(line);
		}
		_log.info("=================================================");

		long serverLoadEnd = System.currentTimeMillis();
		_log.info("Server Loaded in " + ((serverLoadEnd - serverLoadStart) / 1000) + " seconds");
		Information.getInstance();
		Player.getAutoLicenseCheck();

	}

	public static GameServer getInstance()
	{
		return _instance;
	}

	public static void checkFreePorts()
	{
		boolean binded = false;
		while(!binded)
		{
			for(int PORT_GAME : Config.PORTS_GAME)
			{
				try
				{
					ServerSocket ss;
					if(Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*"))
					{
						ss = new ServerSocket(PORT_GAME);
					}
					else
					{
						ss = new ServerSocket(PORT_GAME, 50, InetAddress.getByName(Config.GAMESERVER_HOSTNAME));
					}
					ss.close();
					binded = true;
				}
				catch(Exception e)
				{
					_log.warn("Port " + PORT_GAME + " is allready binded. Please free it and restart server.");
					binded = false;
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException ignored)
					{
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		new GameServer();
	}

	public SelectorThread<GameClient>[] getSelectorThreads()
	{
		return _selectorThreads;
	}

	public int time()
	{
		return (int) (System.currentTimeMillis() / 1000);
	}

	public int uptime()
	{
		return time() - _serverStarted;
	}

	public GameServerListenerList getListeners()
	{
		return _listeners;
	}

	public <T extends GameListener> boolean addListener(T listener)
	{
		return _listeners.add(listener);
	}

	public <T extends GameListener> boolean removeListener(T listener)
	{
		return _listeners.remove(listener);
	}

	public class GameServerListenerList extends ListenerList<GameServer>
	{
		public void onStart()
		{
			for(Listener<GameServer> listener : getListeners())
			{
				if(OnStartListener.class.isInstance(listener))
				{
					((OnStartListener) listener).onStart();
				}
			}
		}

		public void onShutdown()
		{
			for(Listener<GameServer> listener : getListeners())
			{
				if(OnShutdownListener.class.isInstance(listener))
				{
					((OnShutdownListener) listener).onShutdown();
				}
			}
		}
	}
}