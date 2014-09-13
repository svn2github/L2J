package l2next.gameserver.model.items.primeshop;

import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.templates.item.ItemTemplate;

/**
 * @user: Mifesto
 * @date: 23:06 / 29.03.13
 * @team: NOVELL-PROJECT.RU
 */
public class PrimeItem
{
	private final int _itemId;
	private final int _count;

	private final int _weight;
	private final boolean _dropable;

	public PrimeItem(int item_id, int count)
	{
		ItemTemplate item = ItemHolder.getInstance().getTemplate(item_id);

		_itemId = item_id;
		_count = count;

		_weight = item != null ? item.getWeight() : 0;
		_dropable = item != null && item.isDropable();
	}

	public int getItemId()
	{
		return _itemId;
	}

	public int getCount()
	{
		return _count;
	}

	public int getWeight()
	{
		return _weight;
	}

	public boolean dropable()
	{
		return _dropable;
	}
}
