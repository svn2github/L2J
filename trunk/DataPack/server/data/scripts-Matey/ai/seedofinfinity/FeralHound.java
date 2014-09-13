package ai.seedofinfinity;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.instances.NpcInstance;

/**
 * @author pchayka
 */

public class FeralHound extends Fighter
{
	public FeralHound(NpcInstance actor)
	{
		super(actor);
		actor.setIsInvul(true);
	}

	@Override
	protected boolean randomAnimation()
	{
		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}