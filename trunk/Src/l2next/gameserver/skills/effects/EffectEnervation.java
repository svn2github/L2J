package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.stats.Env;

public class EffectEnervation extends Effect
{
	public EffectEnervation(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if(_effected.isNpc())
		{
			((NpcInstance) _effected).setParameter("DebuffIntention", 0.5);
		}
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}

	@Override
	public void onExit()
	{
		super.onExit();
		if(_effected.isNpc())
		{
			((NpcInstance) _effected).setParameter("DebuffIntention", 1.);
		}
	}
}