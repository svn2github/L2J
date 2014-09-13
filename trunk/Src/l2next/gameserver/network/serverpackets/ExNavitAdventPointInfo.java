package l2next.gameserver.network.serverpackets;

public class ExNavitAdventPointInfo extends L2GameServerPacket
{
	private int _points;

	public ExNavitAdventPointInfo(int points)
	{
		_points = points;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_points);
	}
}