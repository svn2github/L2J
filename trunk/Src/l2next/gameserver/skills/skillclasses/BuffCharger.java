package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Skill;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.templates.StatsSet;

import java.util.List;

public class BuffCharger extends Skill
{
	private int _target;

	public BuffCharger(StatsSet set)
	{
		super(set);
		_target = set.getInteger("targetBuff", 0);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for(Creature target : targets)
		{
			int level = 0;
			List<Effect> el = target.getEffectList().getEffectsBySkillId(_target);
			if(el != null)
			{
				level = el.get(0).getSkill().getLevel();
			}

			Skill next = SkillTable.getInstance().getInfo(_target, level + 1);
			if(next != null)
			{
				next.getEffects(activeChar, target, false, false);
			}
		}
	}
}