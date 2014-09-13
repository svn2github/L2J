package ai.incubatorOfEvil;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.Functions;

/**
 * @author Iqman
 */
public class Avanguard_Barton extends DefaultAI
{
	public Avanguard_Barton(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public boolean isGlobalAI()
	{
		return false;
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(Rnd.chance(8))
		{
			Functions.npcSay(actor, NpcString.I_HIT_THINGS_THEY_FALL_DEAD);
		}
		return false;
	}
}