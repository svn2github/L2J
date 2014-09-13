package l2next.gameserver.network.serverpackets;

public class ExChangeClientEffectInfo extends L2GameServerPacket
{
	private int _state;

	public ExChangeClientEffectInfo(int state)
	{
		_state = state;
	}

	@Override
	protected void writeImpl()
	{
		writeD(0);
		writeD(_state);
	}
}
