package l2next.gameserver.templates.item;

public final class StartItem
{
	private final int _id;
	private final long _count;
	private final boolean _equiped;

	public StartItem(int id, long count, boolean equiped)
	{
		_id = id;
		_count = count;
		_equiped = equiped;
	}

	public int getItemId()
	{
		return _id;
	}

	public long getCount()
	{
		return _count;
	}

	public boolean isEquiped()
	{
		return _equiped;
	}
}
