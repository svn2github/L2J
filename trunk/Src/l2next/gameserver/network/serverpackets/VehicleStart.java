package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.entity.boat.Boat;

public class VehicleStart extends L2GameServerPacket
{
	private int _objectId, _state;

	public VehicleStart(Boat boat)
	{
		_objectId = boat.getBoatId();
		_state = boat.getRunState();
	}

	@Override
	protected void writeImpl()
	{
		writeD(_objectId);
		writeD(_state);
	}
}