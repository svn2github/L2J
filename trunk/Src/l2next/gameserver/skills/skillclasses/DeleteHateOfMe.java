package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.stats.Formulas;
import l2next.gameserver.templates.StatsSet;

public class DeleteHateOfMe extends Skill
{
	public DeleteHateOfMe(StatsSet set)
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
				if(activeChar.isPlayer() && ((Player) activeChar).isGM())
				{
					activeChar.sendMessage(new CustomMessage("l2next.gameserver.skills.Formulas.Chance", (Player) activeChar).addString(getName()).addNumber(getActivateRate()));
				}

				if(target.isNpc() && Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate()))
				{
					NpcInstance npc = (NpcInstance) target;
					npc.getAggroList().remove(activeChar, true);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				}
				getEffects(activeChar, target, true, false);
			}
		}
	}
}