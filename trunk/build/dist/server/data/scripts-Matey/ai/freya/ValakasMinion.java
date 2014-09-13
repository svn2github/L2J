package ai.freya;

import bosses.ValakasManager;
import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.ai.Mystic;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;

/**
 * @author pchayka
 */

public class ValakasMinion extends Mystic
{
	public ValakasMinion(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		for(Player p : ValakasManager.getZone().getInsidePlayers())
		{
			notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 5000);
		}
	}
}