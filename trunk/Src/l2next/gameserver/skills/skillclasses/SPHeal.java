package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.templates.StatsSet;

public class SPHeal extends Skill
{
	public SPHeal(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(final Creature activeChar, final Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if(!activeChar.isPlayer())
		{
			return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for(Creature target : targets)
		{
			if(target != null)
			{
				target.getPlayer().addExpAndSp(0, (long) _power);

				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}

		if(isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
