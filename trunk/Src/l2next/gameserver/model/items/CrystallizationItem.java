package l2next.gameserver.model.items;

/**
 * @author ALF
 */
public class CrystallizationItem
{
	private final int _itemId;
	private final long _count;
	private final double _chance;

	public CrystallizationItem(int itemId, long count, double chance)
	{
		_itemId = itemId;
		_count = count;
		_chance = chance;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public long getCount()
	{
		return _count;
	}

	public double getChance()
	{
		return _chance;
	}

}
