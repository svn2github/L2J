package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.boat.Boat;
import l2next.gameserver.utils.Location;

public class GetOffVehicle extends L2GameServerPacket
{
	private int _playerObjectId, _boatObjectId;
	private Location _loc;

	public GetOffVehicle(Player cha, Boat boat, Location loc)
	{
		_playerObjectId = cha.getObjectId();
		_boatObjectId = boat.getBoatId();
		_loc = loc;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_playerObjectId);
		writeD(_boatObjectId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
	}
}