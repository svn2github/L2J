package ai.hellbound;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.data.xml.holder.NpcHolder;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.SimpleSpawner;
import l2next.gameserver.model.instances.DoorInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.ReflectionUtils;

/**
 * RB Darion на крыше Tully Workshop
 *
 * @author pchayka
 */
public class Darion extends Fighter
{
	private static final int[] doors = {
		20250009,
		20250004,
		20250005,
		20250006,
		20250007
	};

	public Darion(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();

		NpcInstance actor = getActor();
		for(int i = 0; i < 5; i++)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(Rnd.get(25614, 25615)));
				sp.setLoc(Location.findPointToStay(actor, 400, 900));
				sp.doSpawn(true);
				sp.stopRespawn();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		//Doors
		for(int i = 0; i < doors.length; i++)
		{
			DoorInstance door = ReflectionUtils.getDoor(doors[i]);
			door.closeMe();
		}
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		//Doors
		for(int i = 0; i < doors.length; i++)
		{
			DoorInstance door = ReflectionUtils.getDoor(doors[i]);
			door.openMe();
		}

		for(NpcInstance npc : GameObjectsStorage.getAllByNpcId(25614, false))
		{
			npc.deleteMe();
		}

		for(NpcInstance npc : GameObjectsStorage.getAllByNpcId(25615, false))
		{
			npc.deleteMe();
		}

		super.onEvtDead(killer);
	}

}