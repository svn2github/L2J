package l2next.gameserver.network.serverpackets;

/**
 * sample: d
 */
public class ShowCalc extends L2GameServerPacket
{
	private int _calculatorId;

	public ShowCalc(int calculatorId)
	{
		_calculatorId = calculatorId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_calculatorId);
	}
}