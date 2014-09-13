package l2next.gameserver.ai;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.entity.boat.Boat;

/**
 * Author: VISTALL Date: 16:56/28.12.2010
 */
public class BoatAI extends CharacterAI
{
	public BoatAI(Creature actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtArrived()
	{
		Boat actor = (Boat) getActor();
		if(actor == null)
		{
			return;
		}

		actor.onEvtArrived();
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
