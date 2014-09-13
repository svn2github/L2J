package l2next.gameserver.templates.item.support;

import l2next.gameserver.templates.item.EnchantType;

/**
 * @author VISTALL
 * @date 22:40/19.05.2011
 */
public class EnchantItem
{
	private final int _itemId;
	private EnchantType _itemType;

	public EnchantItem(int itemId, EnchantType itemType)
	{
		_itemId = itemId;
		_itemType = itemType;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public EnchantType getItemType()
	{
		return _itemType;
	}
}
