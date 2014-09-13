package ai.hellbound;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.data.xml.holder.NpcHolder;
import l2next.gameserver.model.SimpleSpawner;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;

/**
 * @author pchayka
 */
public class Pylon extends Fighter
{
	public Pylon(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();

		NpcInstance actor = getActor();
		for(int i = 0; i < 7; i++)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(22422));
				sp.setLoc(Location.findPointToStay(actor, 150, 550));
				sp.doSpawn(true);
				sp.stopRespawn();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}