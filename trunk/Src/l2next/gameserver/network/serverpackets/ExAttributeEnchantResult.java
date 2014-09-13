package l2next.gameserver.network.serverpackets;

/**
 * @author SYS
 */
public class ExAttributeEnchantResult extends L2GameServerPacket
{
	private int _result;

	public ExAttributeEnchantResult(int unknown)
	{
		_result = unknown;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_result);
	}
}