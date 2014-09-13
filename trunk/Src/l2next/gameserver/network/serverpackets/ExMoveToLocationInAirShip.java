package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.boat.Boat;
import l2next.gameserver.utils.Location;

public class ExMoveToLocationInAirShip extends L2GameServerPacket
{
	private int char_id, boat_id;
	private Location _origin, _destination;

	public ExMoveToLocationInAirShip(Player cha, Boat boat, Location origin, Location destination)
	{
		char_id = cha.getObjectId();
		boat_id = boat.getBoatId();
		_origin = origin;
		_destination = destination;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(char_id);
		writeD(boat_id);

		writeD(_destination.x);
		writeD(_destination.y);
		writeD(_destination.z);
		writeD(_origin.x);
		writeD(_origin.y);
		writeD(_origin.z);
	}
}