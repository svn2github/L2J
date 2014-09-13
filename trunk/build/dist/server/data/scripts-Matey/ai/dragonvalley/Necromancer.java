package ai.dragonvalley;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.ai.Mystic;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.NpcUtils;

/**
 * @author pchayka
 *         После смерти призывает одного из двух монстров
 */
public class Necromancer extends Mystic
{
	public Necromancer(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		if(Rnd.chance(30))
		{
			NpcInstance n = NpcUtils.spawnSingle(Rnd.chance(50) ? 22818 : 22819, getActor().getLoc());
			n.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, 2);
		}
	}
}