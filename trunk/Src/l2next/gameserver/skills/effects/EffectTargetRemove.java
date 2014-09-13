package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

public final class EffectTargetRemove extends Effect
{
	public EffectTargetRemove(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		getEffected().setTargetable(false);
	}

	@Override
	public void onExit()
	{
		super.onExit();
		getEffected().setTargetable(true);
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}