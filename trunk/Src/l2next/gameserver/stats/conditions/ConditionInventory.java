package l2next.gameserver.stats.conditions;

import l2next.gameserver.stats.Env;

public abstract class ConditionInventory extends Condition
{
	protected final int _slot;

	public ConditionInventory(int slot)
	{
		_slot = slot;
	}

	@Override
	protected abstract boolean testImpl(Env env);
}