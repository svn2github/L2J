package l2next.gameserver.model.items.etcitems;

/**
 * @author ALF
 * @date 08.11.2012
 */
public class RecipeIngredient
{
	private final int itemId;
	private final int count;

	public RecipeIngredient(int _itemId, int _count)
	{
		itemId = _itemId;
		count = _count;
	}

	public int getItemId()
	{
		return itemId;
	}

	public int getCount()
	{
		return count;
	}
}
