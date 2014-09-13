package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

import static l2next.gameserver.skills.EffectType.DoubleCasting;
import static l2next.gameserver.skills.EffectType.ElementalyStance;

public class EffectElementalyStance extends Effect
{
	public EffectElementalyStance(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	protected void onStart()
	{
		onActionTime();
		super.onStart();
	}

	@Override
	protected boolean onActionTime()
	{
		if(_effected.getEffectList().getEffectByType(DoubleCasting) == null)
		{
			Effect efa = _effected.getEffectList().getEffectByType(ElementalyStance);
			if((efa != null) && (efa.getSkill() != getSkill()))
			{
				efa.exit();
			}
		}

		return true;
	}
}
