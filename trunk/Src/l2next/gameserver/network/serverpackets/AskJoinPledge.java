package l2next.gameserver.network.serverpackets;

public class AskJoinPledge extends L2GameServerPacket
{
	private int _requestorId;
	private String _pledgeName;

	public AskJoinPledge(int requestorId, String pledgeName)
	{
		_requestorId = requestorId;
		_pledgeName = pledgeName;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_requestorId);
		writeS(_pledgeName);
	}
}