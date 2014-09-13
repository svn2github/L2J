package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.Element;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.templates.item.ItemTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SYS
 */
public class ExShowBaseAttributeCancelWindow extends L2GameServerPacket
{
	private final List<ItemInstance> _items = new ArrayList<ItemInstance>();

	public ExShowBaseAttributeCancelWindow(Player activeChar)
	{
		for(ItemInstance item : activeChar.getInventory().getItems())
		{
			if(item.getAttributeElement() == Element.NONE || !item.canBeEnchanted() || getAttributeRemovePrice(item) == 0)
			{
				continue;
			}
			_items.add(item);
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_items.size());
		for(ItemInstance item : _items)
		{
			writeD(item.getObjectId());
			writeQ(getAttributeRemovePrice(item));
		}
	}

	public static long getAttributeRemovePrice(ItemInstance item)
	{
		switch(item.getCrystalType())
		{
			case S:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 50000 : 40000;
			case S80:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 100000 : 80000;
			// S84 По идеи теперь весь как s80... Инфа не точна...
			case S84:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 200000 : 160000;
			case R:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 400000 : 320000;
			case R95:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 800000 : 640000;
			case R99:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 3200000 : 2560000;
		}
		return 0;
	}
}