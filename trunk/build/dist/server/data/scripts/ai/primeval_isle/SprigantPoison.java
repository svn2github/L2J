package ai.primeval_isle;

import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.tables.SkillTable;

/**
 * @author VISTALL
 * @date 4:57/16.06.2011
 */
public class SprigantPoison extends DefaultAI
{
	private final Skill SKILL = SkillTable.getInstance().getInfo(5086, 1);
	private long _waitTime;

	private static final int TICK_IN_MILISECONDS = 15000;

	public SprigantPoison(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		if(System.currentTimeMillis() > _waitTime)
		{
			NpcInstance actor = getActor();

			actor.doCast(SKILL, actor, false);

			_waitTime = System.currentTimeMillis() + TICK_IN_MILISECONDS;
			return true;
		}

		return false;
	}
}
