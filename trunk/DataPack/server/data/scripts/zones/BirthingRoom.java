package zones;

import instances.Sansililion;
import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.ReflectionUtils;

public class BirthingRoom implements ScriptFile
{

	private static final Location TELEPORT_LOC = new Location(-185839, 147909, -15312);
	private static final String[] zones = {
		"[Birthing_room_0]",
		"[Birthing_room_1]"
	};
	private static ZoneListener _zoneListener;
	private static final int InstanceId = 171;

	private void init()
	{
		_zoneListener = new ZoneListener();

		for(String s : zones)
		{
			Zone zone = ReflectionUtils.getZone(s);
			zone.addListener(_zoneListener);
		}
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

			Player player = cha.getPlayer();

			//if ((zone.getName().equalsIgnoreCase("[Birthing_room_0]"))
			//      || (zone.getName().equalsIgnoreCase("[Birthing_room_1]"))) {
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(InstanceId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(InstanceId))
			{
				ReflectionUtils.enterReflection(player, new Sansililion(), InstanceId);
			}
			//}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}