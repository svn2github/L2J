package l2next.gameserver.network.serverpackets;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class ExRegistPartySubstitute extends L2GameServerPacket
{
	public static final int REGISTER_OK = 1;
	public static final int REGISTER_TIMEOUT = 0;

	private int _objId;
	private int _code;

	public ExRegistPartySubstitute(int objId, int code)
	{
		_objId = objId;
		_code = code;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_objId);
		writeD(_code);
	}
}
