package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

public final class EffectClanBirth extends Effect
{
	public EffectClanBirth(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public boolean onActionTime()
	{
		return true;
	}
}