package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.instances.PetInstance;
import l2next.gameserver.model.items.ItemInstance;

public class PetItemList extends L2GameServerPacket
{
	private ItemInstance[] items;

	public PetItemList(PetInstance cha)
	{
		items = cha.getInventory().getItems();
	}

	@Override
	protected final void writeImpl()
	{
		writeH(items.length);

		for(ItemInstance item : items)
		{
			writeItemInfo(item);
		}
	}
}