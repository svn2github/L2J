package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.entity.boat.Boat;
import l2next.gameserver.utils.Location;

public class VehicleCheckLocation extends L2GameServerPacket
{
	private int _boatObjectId;
	private Location _loc;

	public VehicleCheckLocation(Boat instance)
	{
		_boatObjectId = instance.getBoatId();
		_loc = instance.getLoc();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_boatObjectId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
	}
}