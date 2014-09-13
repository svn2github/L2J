package ai.hellbound;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.instancemanager.naia.NaiaCoreManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;

/**
 * @author pchayka
 */
public class Epidos extends Fighter
{

	public Epidos(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		NaiaCoreManager.removeSporesAndSpawnCube();
		super.onEvtDead(killer);
	}
}