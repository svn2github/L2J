package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.skills.EffectType;
import l2next.gameserver.stats.Env;
import l2next.gameserver.tables.SkillTable;

/**
 *
 * @author Monithly
 */
public class EffectDoubleCasting extends Effect
{
	private static final int[] toggles = {
		11007,
		11008,
		11009,
		11010
	};

	public EffectDoubleCasting(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		for(final int t : toggles)
		{
			if(getEffected().getEffectList().getEffectsBySkillId(t) == null)
			{
				final Skill start = SkillTable.getInstance().getInfo(t, 1);

				if(start != null)
				{
					start.getEffects(getEffector(), getEffected(), false, false);
				}
			}
		}
		if(getEffected().isPlayer())
		{
			final Player player = (Player) getEffected();
			if(player.getEffectList().getEffectsBySkillId(Skill.SKILL_DUAL_CAST) != null)
			{
				player.setIsEnabledDoubleCast(true);
			}
		}
	}

	@Override
	protected void onExit()
	{
		super.onExit();

		for(final Effect ef : getEffected().getEffectList().getAllEffects())
		{
			if((ef.getEffectType() == EffectType.ElementalyStance) && (ef.getStartTime() >= getStartTime()))
			{
				ef.exit();
			}
		}

		if(getEffected().isPlayer())
		{
			final Player player = (Player) getEffected();
			if(player.getEffectList().getEffectsBySkillId(Skill.SKILL_DUAL_CAST) != null)
			{
				player.setIsEnabledDoubleCast(false);
			}
		}
	}

	@Override
	protected boolean onActionTime()
	{
		return false;
	}
}
