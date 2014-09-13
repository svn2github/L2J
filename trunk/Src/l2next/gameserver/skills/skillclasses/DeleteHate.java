package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.commons.util.Rnd;
import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.templates.StatsSet;

public class DeleteHate extends Skill
{
	public DeleteHate(StatsSet set)
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

				if(target.isRaid())
				{
					continue;
				}

				if(getActivateRate() > 0)
				{
					if(activeChar.isPlayer() && ((Player) activeChar).isGM())
					{
						activeChar.sendMessage(new CustomMessage("l2next.gameserver.skills.Formulas.Chance", (Player) activeChar).addString(getName()).addNumber(getActivateRate()));
					}

					if(!Rnd.chance(getActivateRate()))
					{
						return;
					}
				}

				if(target.isNpc())
				{
					NpcInstance npc = (NpcInstance) target;
					npc.getAggroList().clear(false);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				}

				getEffects(activeChar, target, false, false);
			}
		}
	}
}
