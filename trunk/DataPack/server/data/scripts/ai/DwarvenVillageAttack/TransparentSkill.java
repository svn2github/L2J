package ai.DwarvenVillageAttack;

import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.tables.SkillTable;

public class TransparentSkill extends DefaultAI
{
	private static final int SKILL_ID = 14649;

	public TransparentSkill(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		addTimer(1, 100);
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		if(timerId == 1)
		{
			Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
			addTaskBuff(getActor(), skill);
			doTask();
		}
	}

	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if(skill_id == SKILL_ID)
		{
			getActor().deleteMe();
		}
	}
}
