package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Summon;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.stats.Formulas;
import l2next.gameserver.templates.StatsSet;

public class DestroySummon extends Skill
{
	public DestroySummon(StatsSet set)
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

				if(getActivateRate() > 0 && !Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate()))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RESISTED_YOUR_S2).addString(target.getName()).addSkillName(getId(), getLevel()));
					continue;
				}

				if(target.isSummon())
				{
					((Summon) target).saveEffects();
					((Summon) target).unSummon();
					getEffects(activeChar, target, getActivateRate() > 0, false);
				}
			}
		}

		if(isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}