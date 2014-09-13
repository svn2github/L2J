package l2next.gameserver.listener.zone;

import l2next.commons.listener.Listener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Zone;

public interface OnZoneEnterLeaveListener extends Listener<Zone>
{
	public void onZoneEnter(Zone zone, Creature actor);

	public void onZoneLeave(Zone zone, Creature actor);
}
