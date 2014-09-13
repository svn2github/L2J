package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInfo;
import l2next.gameserver.model.items.ItemInstance;

import java.util.ArrayList;
import java.util.List;

public class InventoryUpdate extends L2GameServerPacket
{
	public static final int UNCHANGED = 0;
	public static final int ADDED = 1;
	public static final int MODIFIED = 2;
	public static final int REMOVED = 3;

	private final List<ItemInfo> _items = new ArrayList<ItemInfo>(1);

	public InventoryUpdate addNewItem(ItemInstance item)
	{
		addItem(item).setLastChange(ADDED);
		return this;
	}

	public InventoryUpdate addModifiedItem(ItemInstance item)
	{
		addItem(item).setLastChange(MODIFIED);
		return this;
	}

	public InventoryUpdate addModifiedItem(Player player, ItemInstance item)
	{
		addItem(item).setLastChange(MODIFIED);
		return this;
	}

	public InventoryUpdate addRemovedItem(ItemInstance item)
	{
		addItem(item).setLastChange(REMOVED);
		return this;
	}

	private ItemInfo addItem(ItemInstance item)
	{
		ItemInfo info;
		_items.add(info = new ItemInfo(item));
		return info;
	}

	@Override
	protected final void writeImpl()
	{
		writeH(_items.size());
		for(ItemInfo temp : _items)
		{
			writeH(temp.getLastChange());
			writeItemInfo(temp);
		}
	}
}