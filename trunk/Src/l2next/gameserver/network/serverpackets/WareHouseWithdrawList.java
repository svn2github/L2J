package l2next.gameserver.network.serverpackets;

import l2next.commons.lang.ArrayUtils;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInfo;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.Warehouse.ItemClassComparator;
import l2next.gameserver.model.items.Warehouse.WarehouseType;
import l2next.gameserver.templates.item.ItemTemplate.ItemClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ALF
 * @data 10.02.2012
 */
public class WareHouseWithdrawList extends L2GameServerPacket
{

	private long _adena;
	private List<ItemInfo> _itemList = new ArrayList<ItemInfo>();
	private int _type;
	private int freeSlot;

	public WareHouseWithdrawList(Player player, WarehouseType type, ItemClass clss)
	{
		_adena = player.getAdena();
		_type = type.ordinal();

		ItemInstance[] items;
		switch(type)
		{
			case PRIVATE:
				items = player.getWarehouse().getItems(clss);
				break;
			case FREIGHT:
				items = player.getFreight().getItems(clss);
				break;
			case CLAN:
			case CASTLE:
				items = player.getClan().getWarehouse().getItems(clss);
				break;
			default:
				_itemList = Collections.emptyList();
				return;
		}

		_itemList = new ArrayList<ItemInfo>(items.length);
		ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
		for(ItemInstance item : items)
		{
			_itemList.add(new ItemInfo(item));
		}
		freeSlot = player.getInventory().getAllSize();
	}

	@Override
	protected final void writeImpl()
	{
		writeH(_type);
		writeQ(_adena);
		writeH(_itemList.size());

		writeH(0x00);
		writeD(freeSlot);

		for(ItemInfo item : _itemList)
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
			writeD(0x00);
			writeD(0x00);
		}
	}
}