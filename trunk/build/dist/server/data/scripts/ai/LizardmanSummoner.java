package ai;

import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.ai.Mystic;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.SimpleSpawner;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.PositionUtils;

/**
 * Author: Bonux
 * При ударе монстра спавнятся 2 х Tanta Lizardman Scout и они агрятся на игрока.
 */
public class LizardmanSummoner extends Mystic
{
	private final int TANTA_LIZARDMAN_SCOUT = 22768;
	private final int SPAWN_COUNT = 2;
	private boolean spawnedMobs = false;

	public LizardmanSummoner(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		spawnedMobs = false;
		super.onEvtSpawn();
	}

	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if(!spawnedMobs && attacker.isPlayable())
		{
			NpcInstance actor = getActor();
			for(int i = 0; i < SPAWN_COUNT; i++)
			{
				try
				{
					SimpleSpawner sp = new SimpleSpawner(TANTA_LIZARDMAN_SCOUT);
					int radius = (i % 2 == 0 ? -1 : 1) * 16000;
					sp.setLoc(actor.getLoc());
					NpcInstance npc = sp.doSpawn(true);
					npc.setHeading(PositionUtils.calculateHeadingFrom(npc, attacker));
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 1000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			spawnedMobs = true;
		}
		super.onEvtAttacked(attacker, damage);
	}
}
