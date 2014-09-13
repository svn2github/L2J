package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.items.ItemInfo;

public class ExChangeAttributeItemList extends L2GameServerPacket
{
	private ItemInfo[] _itemsList;
	private int _itemId;

	public ExChangeAttributeItemList(int itemId, ItemInfo[] itemsList)
	{
		_itemId = itemId;
		_itemsList = itemsList;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_itemId);
		writeD(_itemsList.length); // size
		for(ItemInfo item : _itemsList)
		{
			writeItemInfo(item);
		}
	}
}
