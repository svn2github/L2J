package ai;

import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.instances.NpcInstance;

public class Generator extends DefaultAI
{
	public Generator(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}