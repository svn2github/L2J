package l2next.gameserver.network.serverpackets;

public class ChooseInventoryItem extends L2GameServerPacket
{
	private int ItemID;

	public ChooseInventoryItem(int id)
	{
		ItemID = id;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(ItemID);
	}
}