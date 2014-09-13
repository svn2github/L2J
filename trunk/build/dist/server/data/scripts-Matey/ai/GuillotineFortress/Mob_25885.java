package ai.GuillotineFortress;

import l2next.commons.threading.RunnableImpl;

import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.NpcUtils;

/*
 * User: Iqman
 * Date: 21.05.13
 * Time: 19:39
 * Location: Guillotine Fortress raid stage two
 */

public class Mob_25885 extends Fighter
{
    public Mob_25885(NpcInstance actor)
	{
        super(actor);
    }
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		ThreadPoolManager.getInstance().schedule(new Despawn(), 7200000L); //2 hr for raid should be enough
	}	

	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance npc = NpcUtils.spawnSingle(25892, getActor().getLoc(), getActor().getReflection());
		npc.getAggroList().addDamageHate(killer, 10000, 10000);	
		getActor().decayMe();
		super.onEvtDead(killer);
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
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
