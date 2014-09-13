package l2next.gameserver.network.serverpackets;

public class ExPutEnchantSupportItemResult extends L2GameServerPacket
{
	private int _result;

	public ExPutEnchantSupportItemResult(int result)
	{
		_result = result;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_result);
	}
}