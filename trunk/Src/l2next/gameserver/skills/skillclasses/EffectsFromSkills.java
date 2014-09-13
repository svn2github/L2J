package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.templates.StatsSet;

public class EffectsFromSkills extends Skill
{
	public EffectsFromSkills(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for(Creature target : targets)
		{
			if(target != null)
			{
				for(AddedSkill as : getAddedSkills())
				{
					as.getSkill().getEffects(activeChar, target, false, false);
				}
			}
		}
	}
}