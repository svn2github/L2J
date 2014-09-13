package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.utils.Location;

public class StopMoveToLocationInVehicle extends L2GameServerPacket
{
	private int _boatObjectId, _playerObjectId, _heading;
	private Location _loc;

	public StopMoveToLocationInVehicle(Player player)
	{
		_boatObjectId = player.getBoat().getBoatId();
		_playerObjectId = player.getObjectId();
		_loc = player.getInBoatPosition();
		_heading = player.getHeading();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_playerObjectId);
		writeD(_boatObjectId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_heading);
	}
}