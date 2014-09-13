package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.StatsSet;

public class DrainSoul extends Skill
{
	public DrainSoul(StatsSet set)
	{
		super(set);
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if(!target.isMonster())
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if(!activeChar.isPlayer())
		{
			return;
		}

		// This is just a dummy skill for the soul crystal skill condition,
		// since the Soul Crystal item handler already does everything.
	}
}
