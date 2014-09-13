package l2next.gameserver.skills.effects;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

public final class EffectStun extends Effect
{
	public EffectStun(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public boolean checkCondition()
	{
		return Rnd.chance(_template.chance(100));
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_effected.startStunning();
		_effected.abortAttack(true, true);
		_effected.abortCast(true, true);
		_effected.stopMove();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopStunning();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}