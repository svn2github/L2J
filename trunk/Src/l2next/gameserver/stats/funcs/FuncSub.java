package l2next.gameserver.stats.funcs;

import l2next.gameserver.stats.Env;
import l2next.gameserver.stats.Stats;

public class FuncSub extends Func
{
	public FuncSub(Stats stat, int order, Object owner, double value)
	{
		super(stat, order, owner, value);
	}

	@Override
	public void calc(Env env)
	{
		env.value -= value;
	}
}
