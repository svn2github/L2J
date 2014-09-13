package l2next.gameserver.model.items.etcitems;

/**
 * @author ALF
 * @date 08.11.2012
 */
public class RecipeProduction
{
	private final int itemId;
	private final int count;
	private final int chance;

	public RecipeProduction(int itemId, int count, int chance)
	{
		this.itemId = itemId;
		this.count = count;
		this.chance = chance;
	}

	public int getItemId()
	{
		return itemId;
	}

	public int getCount()
	{
		return count;
	}

	public int getChance()
	{
		return chance;
	}

}
