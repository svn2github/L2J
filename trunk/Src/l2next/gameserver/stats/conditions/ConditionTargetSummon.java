package l2next.gameserver.stats.conditions;

import l2next.gameserver.model.Creature;
import l2next.gameserver.stats.Env;

public class ConditionTargetSummon extends Condition
{
	private final boolean _flag;

	public ConditionTargetSummon(boolean flag)
	{
		_flag = flag;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		Creature target = env.target;
		return target != null && target.isSummon() == _flag;
	}
}
