package l2next.gameserver.network.serverpackets;

public class AllianceCrest extends L2GameServerPacket
{
	private int serverId;
	private int _crestId;
	private byte[] _data;

	public AllianceCrest(int crestId, byte[] data)
	{
		serverId = 1;
		_crestId = crestId;
		_data = data;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(serverId);
		writeD(_crestId);
		writeD(_data.length);
		writeB(_data);
	}
}