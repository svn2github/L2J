package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Skill;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.stats.Env;
import l2next.gameserver.tables.SkillTable;

public class EffectCallSkills extends Effect
{
	public EffectCallSkills(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		int[] skillIds = getTemplate().getParam().getIntegerArray("skillIds");
		int[] skillLevels = getTemplate().getParam().getIntegerArray("skillLevels");

		for(int i = 0; i < skillIds.length; i++)
		{
			Skill skill = SkillTable.getInstance().getInfo(skillIds[i], skillLevels[i]);
			if(skill == null)
			{
				continue;
			}
			for(Creature cha : skill.getTargets(getEffector(), getEffected(), false))
			{
				getEffector().broadcastPacket(new MagicSkillUse(getEffector(), cha, skillIds[i], skillLevels[i], 0, 0));
			}
			getEffector().callSkill(skill, skill.getTargets(getEffector(), getEffected(), false), false);
		}
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}