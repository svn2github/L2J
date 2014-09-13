package ai.residences.fortress.siege;

import ai.residences.SiegeGuardFighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.Functions;

/**
 * @author VISTALL
 * @date 20:10/19.04.2011
 */
public class RebelCommander extends SiegeGuardFighter
{
	public RebelCommander(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);

		Functions.npcSay(getActor(), NpcString.DONT_THINK_THAT_ITS_GONNA_END_LIKE_THIS);
	}
}
