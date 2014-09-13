package l2next.gameserver.network.serverpackets;

public class ExPeriodicItemList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeD(0); // count of dd
	}
}