package l2next.gameserver.network.serverpackets;

import l2next.commons.lang.ArrayUtils;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInfo;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.Warehouse.ItemClassComparator;
import l2next.gameserver.model.items.Warehouse.WarehouseType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ALF
 * @data 10.02.2012
 */
public class WareHouseDepositList extends L2GameServerPacket
{

	private int _whtype;
	private long _adena;
	private List<ItemInfo> _itemList;

	public WareHouseDepositList(Player cha, WarehouseType whtype)
	{
		_whtype = whtype.ordinal();
		_adena = cha.getAdena();

		ItemInstance[] items = cha.getInventory().getItems();
		ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
		_itemList = new ArrayList<ItemInfo>(items.length);
		for(ItemInstance item : items)
		{
			if(item.canBeStored(cha, _whtype == 1))
			{
				_itemList.add(new ItemInfo(item));
			}
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeH(_whtype);
		writeQ(_adena);
		writeH(0x00); // кол занятых слотов
		writeD(0x00); // Кол. Валюты в ВХ
		writeH(_itemList.size());
		for(ItemInfo item : _itemList)
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
		}
	}
}