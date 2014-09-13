package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

public final class EffectDeathImmunity extends Effect
{
	public EffectDeathImmunity(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public boolean checkCondition()
	{
		return super.checkCondition();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		getEffected().addDeathImmunity();
	}

	@Override
	public void onExit()
	{
		getEffected().removeDeathImmunity();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
