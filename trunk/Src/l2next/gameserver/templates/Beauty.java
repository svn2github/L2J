package l2next.gameserver.templates;

import gnu.trove.list.array.TIntArrayList;
import l2next.gameserver.model.Player;

/**
 * @Author Awakeninger
 */
public class Beauty
{
	private final int _id;
	private final long _cost;
	private final long _resetCost;
	private final long _val;
	private final int _ownCoin;

	public Beauty(int id, long cost, long resetCost, long val, int ownCoin)
	{
		_id = id;
		_cost = cost;
		_resetCost = resetCost;
		_val = val;
		_ownCoin = ownCoin;
	}

	public int getId()
	{
		return _id;
	}

	public long getCost()
	{
		return _cost;
	}

	public long getResetCost()
	{
		return _resetCost;
	}

	public long getVal()
	{
		return _val;
	}

	public int getOwnCoin()
	{
		return _ownCoin;
	}
}