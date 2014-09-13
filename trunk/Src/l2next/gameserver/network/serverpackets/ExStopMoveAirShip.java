package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.entity.boat.Boat;
import l2next.gameserver.utils.Location;

public class ExStopMoveAirShip extends L2GameServerPacket
{
	private int boat_id;
	private Location _loc;

	public ExStopMoveAirShip(Boat boat)
	{
		boat_id = boat.getBoatId();
		_loc = boat.getLoc();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(boat_id);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
	}
}