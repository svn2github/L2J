package l2next.gameserver.network.serverpackets;

public class ExShowQuestMark extends L2GameServerPacket
{
	private int _questId;

	public ExShowQuestMark(int questId)
	{
		_questId = questId;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_questId);
		writeD(1);// unknown GOD
	}
}