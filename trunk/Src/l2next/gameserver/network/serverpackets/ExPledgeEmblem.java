package l2next.gameserver.network.serverpackets;

/**
 * User: Samurai
 */
public class ExPledgeEmblem extends L2GameServerPacket
{
	private int _crestId;
	private int _pledgeId;
	private int _order;
	private byte[] _data;

	public ExPledgeEmblem(int pledgeId, int crestId, int order, byte[] data)
	{
		_pledgeId = pledgeId;
		_crestId = crestId;
		_order = order;
		_data = data;
	}

	@Override
	protected void writeImpl()
	{
		writeD(200);
		writeD(_pledgeId);
		writeD(_crestId);
		writeD(_order);
		writeD(65664);
		writeD(_data.length);
		writeB(_data);
	}
}