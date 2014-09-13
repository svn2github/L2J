package ai.hellbound;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.scripts.Functions;

/**
 * AI Tortured Native в городе-инстанте на Hellbound<br>
 * - периодически кричат
 *
 * @author SYS
 */
public class TorturedNative extends Fighter
{
	public TorturedNative(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if(actor.isDead())
		{
			return true;
		}

		if(Rnd.chance(1))
		{
			if(Rnd.chance(10))
			{
				Functions.npcSay(actor, "Eeeek... I feel sick... yow...!");
			}
			else
			{
				Functions.npcSay(actor, "It... will... kill... everyone...!");
			}
		}

		return super.thinkActive();
	}
}