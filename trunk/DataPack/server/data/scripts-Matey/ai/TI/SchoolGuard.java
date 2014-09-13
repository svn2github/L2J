package ai.TI;

import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;

public class SchoolGuard extends Fighter
{
	public SchoolGuard(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 5000;
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();

		if(target.getNpcId() != 23113)
		{
			return false;
		}

		if(getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			actor.getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}

		return super.checkAggression(target);
	}

	@Override
	protected boolean thinkActive()
	{
        NpcInstance actor = getActor();
        doAttack(actor);

		return false;
	}

    private void doAttack(NpcInstance actor)
    {
        for(NpcInstance npc : actor.getAroundNpc(100,250))
        {
            if(npc.getNpcId() == 23113)
                actor.doAttack(npc);
        }
    }
    
    @Override
    protected boolean randomWalk()
    {
        return false;
    }
}