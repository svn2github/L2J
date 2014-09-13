package zones;

import l2next.gameserver.instancemanager.ArcanManager;
import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Zone;
import l2next.gameserver.network.serverpackets.EventTrigger;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.ReflectionUtils;

public class ArcanZone implements ScriptFile
{
	private static final String TELEPORT_ZONE_NAME = "[Arcan_0]";
	private static ZoneListener _zoneListener;
	private static int _BLUE = 262001;
	private static int _RED = 262003;

	private void init()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		zone.addListener(_zoneListener);
	}

	@Override
	public void onLoad()
	{
		init();
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
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

			if(cha instanceof Player)
			{
				Player player = cha.getPlayer();
				if(ArcanManager.getStage() == _RED)
				{
					player.sendPacket(new EventTrigger(_BLUE, false));
					player.sendPacket(new EventTrigger(_RED, true));
				}
				else
				{
					player.sendPacket(new EventTrigger(_RED, false));
					player.sendPacket(new EventTrigger(_BLUE, true));
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}