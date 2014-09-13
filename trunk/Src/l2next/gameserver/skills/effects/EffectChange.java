package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

public final class EffectChange extends Effect
{
	public EffectChange(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_effected.startSleeping();
		_effected.abortAttack(true, true);
		_effected.abortCast(true, true);
		_effected.stopMove();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopSleeping();
		_effected.setTransformation(0);
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}