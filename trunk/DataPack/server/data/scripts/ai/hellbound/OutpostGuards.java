package ai.hellbound;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.instances.NpcInstance;

public class OutpostGuards extends Fighter
{
	public OutpostGuards(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
}