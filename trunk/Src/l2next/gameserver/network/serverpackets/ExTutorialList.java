package l2next.gameserver.network.serverpackets;

public class ExTutorialList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeB(new byte[128]);
	}
}