package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Skill;
import l2next.gameserver.stats.Env;

/**
 * User: Samurai
 */
public class EffectDevilsSway extends Effect
{
	public EffectDevilsSway(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		for(Effect effect : _effected.getEffectList().getAllEffects())
		{
			if(effect.getSkill().getSkillType() == Skill.SkillType.DEBUFF && effect.getSkill().getId() != 11095)
			{
				//effect.setPeriod((long) (effect.getDuration()*1L));
				effect.restart();
			}
		}
		_effected.updateEffectIcons();
	}

	@Override
	protected boolean onActionTime()
	{
		return false;
	}
}
