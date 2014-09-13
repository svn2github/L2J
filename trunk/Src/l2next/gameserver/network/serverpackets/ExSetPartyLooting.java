package l2next.gameserver.network.serverpackets;

public class ExSetPartyLooting extends L2GameServerPacket
{
	private int _result;
	private int _mode;

	public ExSetPartyLooting(int result, int mode)
	{
		_result = result;
		_mode = mode;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_result);
		writeD(_mode);
	}
}
