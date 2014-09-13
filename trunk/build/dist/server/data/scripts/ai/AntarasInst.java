package ai;

import l2next.commons.collections.GArray;
import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;

public class AntarasInst extends Fighter
{
	public AntarasInst(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(actor.isDead())
		{
			return false;
		}

		GArray<NpcInstance> around = actor.getAroundNpc(2000, 1000);
		if(around != null && !around.isEmpty())
		{
			for(NpcInstance npc : around)
			{
				if(npc.getNpcId() != 19153 && npc.getNpcId() != 19152)
				{
					actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 3);
				}
			}
		}
		return true;
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if(attacker == null || attacker.isPlayable())
		{
			return;
		}

		super.onEvtAttacked(attacker, damage);
	}
}