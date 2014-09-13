package l2next.gameserver.stats.conditions;

import l2next.gameserver.stats.Env;

public class ConditionPlayerMinLevel extends Condition
{
	private final int _level;

	public ConditionPlayerMinLevel(int level)
	{
		_level = level;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		return env.character.getLevel() >= _level;
	}
}