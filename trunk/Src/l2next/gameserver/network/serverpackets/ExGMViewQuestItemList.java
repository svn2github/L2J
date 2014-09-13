package l2next.gameserver.network.serverpackets;

import l2next.gameserver.Config;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;

/**
 * @author VISTALL
 * @date 4:20/06.05.2011
 */
public class ExGMViewQuestItemList extends L2GameServerPacket
{
	private int _size;
	private ItemInstance[] _items;

	private int _limit;
	private String _name;

	public ExGMViewQuestItemList(Player player, ItemInstance[] items, int size)
	{
		_items = items;
		_size = size;
		_name = player.getName();
		_limit = Config.QUEST_INVENTORY_MAXIMUM;
	}

	@Override
	protected final void writeImpl()
	{
		writeS(_name);
		writeD(_limit);
		writeH(_size);
		for(ItemInstance temp : _items)
		{
			if(temp.getTemplate().isQuest())
			{
				writeItemInfo(temp);
			}
		}
	}
}
