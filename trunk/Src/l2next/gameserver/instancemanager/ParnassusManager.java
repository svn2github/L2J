package l2next.gameserver.instancemanager;

import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Zone;
import l2next.gameserver.network.serverpackets.EventTrigger;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * @Author Awakeninger
 * @Date: 26.04.2013
 * @Modified: 4.08.2013
 * не очень нравится с зонами.
 **/
public class ParnassusManager
{
	private static final String TELEPORT_ZONE_NAME = "[parnassus]";
	private static final Logger _log = LoggerFactory.getLogger(ParnassusManager.class);
	private static ParnassusManager _instance;

	private static ZoneListener _zoneListener;

	public static ParnassusManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new ParnassusManager();
		}
		return _instance;
	}

	public ParnassusManager()
	{
		_zoneListener = new ZoneListener();
		//ThreadPoolManager.getInstance().scheduleAtFixedRate(new ChangeStage(), _taskDelay, _taskDelay);
		Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		zone.addListener(_zoneListener);
		_log.info("Parnasus Manager: Loaded");
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

			broadcastPacket(24230010, true);
			broadcastPacket(24230012, true);
			if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 0 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 2 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4)
			{
				broadcastPacket(24230014, true);
				broadcastPacket(24230016, false);
				broadcastPacket(24230018, false);
			}
			else if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 3 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 5)
			{
				broadcastPacket(24230014, false);
				broadcastPacket(24230016, true);
				broadcastPacket(24230018, false);
			}
			else
			{
				broadcastPacket(24230014, false);
				broadcastPacket(24230016, false);
				broadcastPacket(24230018, true);
			}
		}
	
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}

	public void broadcastPacket(int value, boolean b)
	{
		L2GameServerPacket trigger = new EventTrigger(value, b);
		for(Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(trigger);
		}
	}
}