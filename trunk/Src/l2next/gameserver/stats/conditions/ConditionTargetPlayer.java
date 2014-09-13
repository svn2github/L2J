package l2next.gameserver.stats.conditions;

import l2next.gameserver.model.Creature;
import l2next.gameserver.stats.Env;

public class ConditionTargetPlayer extends Condition
{
	private final boolean _flag;

	public ConditionTargetPlayer(boolean flag)
	{
		_flag = flag;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		Creature target = env.target;
		return target != null && target.isPlayer() == _flag;
	}
}
