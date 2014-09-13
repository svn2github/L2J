package l2next.gameserver.network.serverpackets;

public class ExPledgeCount extends L2GameServerPacket
{
	private int _count;

	public ExPledgeCount(int count)
	{
		_count = count;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_count + 1);
	}
}