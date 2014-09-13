package ai.GuillotineFortress;

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
 * Location: Guillotine Fortress raid stage 1
 */
//TODO find the manument ID and their skill when they ressurect the raid boss
public class Mob_25888 extends Fighter
{
    public Mob_25888(NpcInstance actor)
	{
        super(actor);
    }

	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance npc = NpcUtils.spawnSingle(25885, getActor().getLoc(), getActor().getReflection());
		npc.getAggroList().addDamageHate(killer, 10000, 10000);	
		super.onEvtDead(killer);
	}	

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
