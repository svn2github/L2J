package l2next.gameserver.network.serverpackets;

public class PledgeCrest extends L2GameServerPacket
{
	private int serverId;
	private int _crestId;
	private int _crestSize;
	private byte[] _data;

	public PledgeCrest(int crestId, byte[] data)
	{
		serverId = 1;
		_crestId = crestId;
		_data = data;
		_crestSize = _data.length;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(serverId);
		writeD(_crestId);
		if (_data != null)
		{
			writeD(_data.length);
			writeB(_data);
		}
		else 
		{
			writeD(0x00);
		}
	}
}