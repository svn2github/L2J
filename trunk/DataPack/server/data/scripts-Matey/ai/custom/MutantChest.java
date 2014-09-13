package ai.custom;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.scripts.Functions;

public class MutantChest extends Fighter
{

	public MutantChest(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		if(Rnd.chance(30))
		{
			Functions.npcSay(actor, "Враги! Всюду враги! Все сюда, враги здесь!");
		}

		actor.deleteMe();
	}
}