package l2next.gameserver.network.serverpackets;

public class JoinPledge extends L2GameServerPacket
{
	private int _pledgeId;

	public JoinPledge(int pledgeId)
	{
		_pledgeId = pledgeId;
	}

	@Override
	protected final void writeImpl()
	{

		writeD(_pledgeId);
	}
}