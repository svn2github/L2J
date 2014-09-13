package l2next.gameserver.instancemanager;

import l2next.commons.util.Rnd;
import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.base.InvisibleType;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseEnter;
import l2next.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseMemberList;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javolution.util.FastList;

/**
* @Author Awakeninger
**/
public class ChaosInviteManager
{
	private static final Logger _log = LoggerFactory.getLogger(ChaosInviteManager.class);
	private static ChaosInviteManager _instance;
	private static ChaosFestivalStatus _festivalStatus;
	private static final int GRANKAIN_LUMIERE = 33685;
	private static final int[] ARENAS = {220,221,222,223};
	private static int currentArena;
	private static int _Stage;
	private static final Location[] LOC1 = { new Location(-82630, -259280, -3328), new Location(-82179, -259732, -3328), new Location(-81736, -259292, -3328), new Location(-82174, -258850, -3328) };
	private static final Location[] LOC2 = { new Location(-81259, -245270, -3324), new Location(-82585, -245264, -3324), new Location(-82577, -246079, -3326), new Location(-81275, -246075, -3324) };
	private static final Location[] LOC3 = { new Location(-9593, -220697, -7664), new Location(-8562, -220710, -7670), new Location(-8577, -219705, -7670), new Location(-9540, -219710, -7672) };
	private static final Location[] LOC4 = { new Location(-212014, 244496, 2117), new Location(-212019, 245300, 2117), new Location(-212907, 244896, 2038), new Location(-214072, 244892, 2032), new Location(-214909, 245300, 2112), new Location(-214908, 244485, 2117) };
	private static long task = 60 * 1000L;
	private static Date date = new Date();
	private static boolean isPvP;
	private static boolean _isInvited;
	private static List<Player> challenger;
	private static Player player;
	private static final List<Player> _fightingNow = new FastList();
	private static Location currentLoc;

	public static Location LocApp()
	{
		switch(getArenaID())
		{
			case 220:
			{
				Location curLoc1 = LOC1[Rnd.get(LOC1.length)];
				currentLoc = curLoc1;
			}
			break;
			case 221:
			{
				Location curLoc2 = LOC2[Rnd.get(LOC2.length)];
				currentLoc = curLoc2;
			}
			break;
			case 222:
			{
				Location curLoc3 = LOC3[Rnd.get(LOC3.length)];
				currentLoc = curLoc3;
			}
			break;
			case 223:
			{
				Location curLoc4 = LOC4[Rnd.get(LOC4.length)];
				currentLoc = curLoc4;
			}
			break;
		}
		return currentLoc;
	}
	public static ChaosInviteManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new ChaosInviteManager();
		}
		return _instance;
	}

	public ChaosInviteManager()
	{
		setStage(1);
		_log.info("ChaosInviteManager: Loaded.");
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new NextCompCheck(), task, task);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new CheckInvite(), task, task);
	}
	
	private class CheckInvite extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			if(isInvited())
			{
				prepareStart();
			}	
		}
	}
	
	private void prepareStart()
	{
		ThreadPoolManager.getInstance().schedule(new sheduleStart(), 5*60*1000);
	}
	
	private class sheduleStart extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			enterInstance(player);
		}
	}
	
	private void enterInstance(Player player)
	{
		challenger = new ArrayList<Player>();
		Reflection r = player.getActiveReflection();
		if(player.canEnterInstance(currentArena))
		{
			ReflectionUtils.enterReflection(player, currentArena);
			startChallenge(challenger);
		}
	}
	
	public static void startChallenge(List<Player> players)
	{
		for (Player player : players)
		{
			Reflection r = player.getActiveReflection();
			if (players.size() > 1) 
			{
				player.sendPacket(new ExCuriousHouseMemberList(players));
			}
			player.stopMove();
			_fightingNow.add(player);
			player.setTarget(null);
			player.teleToLocation(LocApp(), r);
			player.sendPacket(new ExCuriousHouseEnter());
			int index = 1;
			player.setVisibleName("Player" + String.valueOf(index));
			player.setInvisibleType(InvisibleType.NORMAL);
			if (players.size() <= 1)
			{
				endChallenge();
			}
		}
	}
	
	public static void endChallenge()
	{
		
	}

	private class NextCompCheck extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			if(date.getHours() >= 20 && date.getHours() <= 23)
			{	
				for(Player player : GameObjectsStorage.getAllPlayersForIterate())
				{
					if(player.getClan() == null || !player.isAwaking())
					{
						return;
					}
					if(player.isAwaking() && player.getLevel() >= 85 && player.getClan().getLevel() >= 3)
					{
						if(getStage() == 1)
						{
							if(date.getMinutes() >= 00 && date.getMinutes() <= 05)
							{
								startSending(player);
							}	
							if(date.getMinutes() >= 15 && date.getMinutes() <= 20)
							{
								startSending(player);
							}
							if(date.getMinutes() >= 30 && date.getMinutes() <= 35)
							{
								startSending(player);
							}
							if(date.getMinutes() >= 45 && date.getMinutes() <= 50)
							{
								startSending(player);
							}
						}
						else if(getStage() == 0)
						{
							setStage(1);
						}
					}
				}
			}
		}
	}
	
	private void startSending(Player player)
	{
		if(!player.isInChaosBattle())
		{
			setStage(0);
			player.sendPacket(new NpcHtmlMessage(player, 0 ,"", 1));
			player.sendPacket(new ExCuriousHouseEnter());
		}
	}

	public static int getStage()
	{
		return _Stage;
	}

	public void setStage(int s)
	{
		_Stage = s;
	}
	
	public static boolean isInvited()
	{
		return _isInvited;
	}
	
	public static void setIsInvited(boolean inv)
	{
		_isInvited = inv;
	}
	
	public static boolean getType()
	{
		return isPvP;
	}
	
	public static void setType(boolean type)
	{
		isPvP = type;
	}
	
	public static int getArenaID()
	{
		currentArena = ARENAS[Rnd.get(ARENAS.length)];
		return currentArena;
	}
	
	public static enum ChaosFestivalStatus
	{
		SCHEDULED, 
		INVITING, 
		PREPARING, 
		RUNNING;
	}
	
	public static ChaosFestivalStatus getStatus()
	{
		return _festivalStatus;
	}
}