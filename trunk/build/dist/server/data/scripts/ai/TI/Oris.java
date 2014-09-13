package ai.TI;

import l2next.commons.util.Rnd;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.network.serverpackets.SocialAction;

public class Oris extends DefaultAI
{

	private long _wait_timeout = 0;

	public Oris(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 1000;
	}

	@Override
	public boolean thinkActive()
	{
		NpcInstance actor = getActor();

		if(System.currentTimeMillis() > _wait_timeout)
		{
			_wait_timeout = System.currentTimeMillis() + Rnd.get(5, 13) * 1000L;
			actor.broadcastPacket(new L2GameServerPacket[]{new MagicSkillUse(actor, actor, 5965, 1, 500, 1500L)});
			actor.broadcastPacket(new SocialAction(actor.getObjectId(), 2));
		}
		return false;
	}
}