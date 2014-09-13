package ai.residences.clanhall;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.tables.SkillTable;

/**
 * @author VISTALL
 * @date 16:38/22.04.2011
 */
public class MatchLeader extends MatchFighter
{
	public static final Skill ATTACK_SKILL = SkillTable.getInstance().getInfo(4077, 6);

	public MatchLeader(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtAttacked(Creature attacker, int dam)
	{
		super.onEvtAttacked(attacker, dam);

		if(Rnd.chance(10))
		{
			addTaskCast(attacker, ATTACK_SKILL);
		}
	}
}
