package l2next.gameserver.network.serverpackets;

/**
 * @author kick
 **/
public class ExPutShapeShiftingExtractionItemResult extends L2GameServerPacket
{
	public static L2GameServerPacket FAIL = new ExPutShapeShiftingExtractionItemResult(0x00);
	public static L2GameServerPacket SUCCESS = new ExPutShapeShiftingExtractionItemResult(0x01);

	private final int _result;

	public ExPutShapeShiftingExtractionItemResult(int result)
	{
		_result = result;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_result); //Result
	}
}