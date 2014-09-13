package ai.incubatorOfEvil;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.Functions;

/**
 * @author Iqman
 */
public class Avanguard_Xaok extends DefaultAI
{
	public Avanguard_Xaok(NpcInstance actor)
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
			Functions.npcSay(actor, NpcString.WHAT_DO_I_FEEL_WHEN_I_KILL_SHILENS_MONSTERS_RECOIL);
		}
		return false;
	}
}