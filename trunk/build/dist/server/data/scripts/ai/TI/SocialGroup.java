package ai.TI;

import l2next.commons.collections.GArray;
import l2next.commons.util.Rnd;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.SocialAction;
import org.apache.commons.lang3.ArrayUtils;

public class SocialGroup extends DefaultAI
{
	//33007
	private static final int[] groups = {
		33018,
		33000
	};
	private long _wait_timeout = 0;

	public SocialGroup(NpcInstance actor)
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
			_wait_timeout = System.currentTimeMillis() + Rnd.get(10, 30) * 1000L;
			GArray<NpcInstance> around = actor.getAroundNpc(700, 100);
			if(around != null && !around.isEmpty())
			{
				actor.broadcastPacket(new SocialAction(actor.getObjectId(), 3));
				for(final NpcInstance group : around)
				{
					if(ArrayUtils.contains(groups, group.getNpcId()))
					{
						ThreadPoolManager.getInstance().schedule(new Runnable()
						{
							@Override
							public void run()
							{
								group.broadcastPacket(new SocialAction(group.getObjectId(), 3));
							}
						}, 2000);
					}
				}
			}
		}
		return false;
	}
}