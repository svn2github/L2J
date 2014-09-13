package ai;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.SocialAction;

public class ArcanCat extends Fighter
{

	public ArcanCat(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 5000;
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();

		actor.broadcastPacket(new SocialAction(actor.getObjectId(), 2));

		return super.thinkActive();
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}

}