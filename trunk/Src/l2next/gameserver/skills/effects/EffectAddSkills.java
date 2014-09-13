package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Skill.AddedSkill;
import l2next.gameserver.stats.Env;

public class EffectAddSkills extends Effect
{
	public EffectAddSkills(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		for(AddedSkill as : getSkill().getAddedSkills())
		{
			getEffected().addSkill(as.getSkill());
		}
	}

	@Override
	public void onExit()
	{
		super.onExit();
		for(AddedSkill as : getSkill().getAddedSkills())
		{
			getEffected().removeSkill(as.getSkill());
		}
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}