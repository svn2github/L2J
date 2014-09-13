package ai.GuillotineFortress;

import java.util.List;

import l2next.commons.threading.RunnableImpl;
import l2next.commons.collections.GArray;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.NpcUtils;

/*
 * User: Iqman
 * Date: 21.05.13
 * Time: 19:39
 * Location: Guillotine Fortress raid stage 3 mass attack
 */
public class Slave extends Fighter
{
	
    public Slave(NpcInstance actor)
	{
        super(actor);
    }

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		NpcInstance npc = GameObjectsStorage.getByNpcId(25892);
		if(npc == null)
			return; //dead
		GArray<Player> list = World.getAroundPlayers(npc, 1200, 1200);
		if(list == null || list.isEmpty())
			return; //everyone died
		for(Player player : list)
		{
			getActor().getAggroList().addDamageHate(player, 10000, 10000);	//full hate
			break; //only the first for his side.
		}
		ThreadPoolManager.getInstance().schedule(new Despawn(), 300000L); //250 mobs is too much for us for server in one place
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	private class Despawn extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			if(getActor() != null)
				getActor().deleteMe();
		}
	}	
}
