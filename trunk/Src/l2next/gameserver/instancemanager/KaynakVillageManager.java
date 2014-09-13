package l2next.gameserver.instancemanager;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Zone;
import l2next.gameserver.network.serverpackets.EventTrigger;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * @Author Awakeninger
 * @Date: 25.08.2013
 * не очень нравится с зонами.
 * //TODO: Спаун и сообщения.
 **/
public class KaynakVillageManager
{	
	private boolean isPvp;
	private static final int PVP_TRIGGER = 20140700;
	private static final String TELEPORT_ZONE_NAME = "[karnak]";
	private static final Logger _log = LoggerFactory.getLogger(KaynakVillageManager.class);
	private static KaynakVillageManager _instance;

	private static ZoneListener _zoneListener;

	public static KaynakVillageManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new KaynakVillageManager();
		}
		return _instance;
	}

	public KaynakVillageManager()
	{
		setPvP(false);
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		zone.addListener(_zoneListener);
		_log.info("Kaynak Village Manager: Loaded");
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Change(), 3600 * 1000, 3600 * 1000);
	}
	
	private class Change extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			if(!getIsPvP())
			{
				setPvP(true);
				broadcastPacket(20140700, false, true);
				SpawnManager.getInstance().despawn("Kaynak");
			}
			else
			{
				setPvP(false);
				broadcastPacket(20140700, true, true);
				SpawnManager.getInstance().spawn("Kaynak");
			}
			_log.info("Kaynak Village Manager: Stage Change To" + (getIsPvP() ? "PVP" : "PEACE"));
		}
	}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if(zone == null)
			{
				return;
			}

			if(cha == null)
			{
				return;
			}

			if(!cha.isPlayer())
			{
				return;
			}
			
			if(!getIsPvP())
			{
				broadcastPacket(20140700, false, false);
			}
			else
			{
				broadcastPacket(20140700, true, false);
			}
		}
	
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}

	public void broadcastPacket(int value, boolean b, boolean mes)
	{
		L2GameServerPacket trigger = new EventTrigger(value, b);
		for(Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(trigger);
		}
		
		if(mes)
		{
			for(Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				if(!getIsPvP())
				{
					L2GameServerPacket sm = new ExShowScreenMessage("Деревня Кайнак стала боевой зоной", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true);
					player.sendPacket(sm);
				}
				else
				{
					L2GameServerPacket sm = new ExShowScreenMessage("Деревня Кайнак стала мирной зоной", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true);
					player.sendPacket(sm);
				}
			}
		}
	}
	
	public boolean getIsPvP()
	{
		return isPvp;
	}

	public void setPvP(boolean s)
	{
		isPvp = s;
	}
}