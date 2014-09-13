package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.TrapInstance;
import l2next.gameserver.templates.StatsSet;

public class DefuseTrap extends Skill
{
	public DefuseTrap(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if(target == null || !target.isTrap())
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		for(Creature target : targets)
		{
			if(target != null && target.isTrap())
			{

				TrapInstance trap = (TrapInstance) target;
				if(trap.getLevel() <= getPower())
				{
					trap.deleteMe();
				}
			}
		}

		if(isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}