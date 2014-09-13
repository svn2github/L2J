package l2next.gameserver.network.serverpackets;

public class SendTradeRequest extends L2GameServerPacket
{
	private int _senderId;

	public SendTradeRequest(int senderId)
	{
		_senderId = senderId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_senderId);
	}
}