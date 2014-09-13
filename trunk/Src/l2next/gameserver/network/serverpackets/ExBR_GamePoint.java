package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

public class ExBR_GamePoint extends L2GameServerPacket
{
	private int _objectId;
	private long _points;

	public ExBR_GamePoint(Player player)
	{
		_objectId = player.getObjectId();
		_points = player.getPremiumPoints();
	}

	@Override
	protected void writeImpl()
	{
		writeD(_objectId);
		writeQ(_points);
		writeD(0x00); // ??
	}
}