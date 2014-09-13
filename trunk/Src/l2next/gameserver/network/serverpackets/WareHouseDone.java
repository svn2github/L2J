package l2next.gameserver.network.serverpackets;

public class WareHouseDone extends L2GameServerPacket
{

	@Override
	protected void writeImpl()
	{
		writeD(0); // ?
	}
}