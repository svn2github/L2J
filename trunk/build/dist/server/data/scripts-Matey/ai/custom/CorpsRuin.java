package ai.custom;

import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;


public class CorpsRuin extends DefaultAI {


	public CorpsRuin(NpcInstance actor) {
		super(actor);
	}


	protected void onEvtDead(Creature killer) {
		return;
	}

}