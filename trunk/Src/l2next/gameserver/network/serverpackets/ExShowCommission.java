package l2next.gameserver.network.serverpackets;

public class ExShowCommission extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeD(1);// unk
	}
}