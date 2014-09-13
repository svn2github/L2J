package ai;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.geodata.GeoEngine;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;

public class RandomWalkingAI extends DefaultAI
{

	protected static final int AI_WALK_RANGE = Rnd.get(200, 700);

	private static final int Boy = 33224;
	private static final int Girl = 33217;
	private static final int Marsha = 33109;
	private static final int Rabbits = 33203;
	private static final int Rabbits2 = 32971;
	private static final int Rabbits3 = 33198;

	public RandomWalkingAI(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000; //2sec
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(actor.isMoving)
		{
			return false;
		}

		int val = Rnd.get(100);

		if(val < 80)
		{
			randomWalk();
		}
		else if(val < 20)
		{
			actor.onRandomAnimation();
		}

		return false;
	}

	@Override
	protected boolean randomWalk()
	{
		NpcInstance actor = getActor();
		if(actor == null)
		{
			return false;
		}

		Location sloc = actor.getSpawnedLoc();

		int x = sloc.x + Rnd.get(2 * AI_WALK_RANGE) - AI_WALK_RANGE;
		int y = sloc.y + Rnd.get(2 * AI_WALK_RANGE) - AI_WALK_RANGE;
		int z = GeoEngine.getHeight(x, y, sloc.z, actor.getGeoIndex());

		switch(actor.getNpcId())
		{
			case Boy:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Girl:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Rabbits:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Rabbits2:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Rabbits3:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Marsha:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			default:
				actor.moveToLocation(x, y, z, 0, true);
				break;
		}
		return true;
	}
}