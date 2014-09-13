package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.utils.Location;

public class ValidateLocationInVehicle extends L2GameServerPacket
{
	private int _playerObjectId, _boatObjectId;
	private Location _loc;

	public ValidateLocationInVehicle(Player player)
	{
		_playerObjectId = player.getObjectId();
		_boatObjectId = player.getBoat().getBoatId();
		_loc = player.getInBoatPosition();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_playerObjectId);
		writeD(_boatObjectId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
	}
}