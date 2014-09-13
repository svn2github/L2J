package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.FeedableBeastInstance;
import l2next.gameserver.templates.StatsSet;

public class BeastFeed extends Skill
{
	public BeastFeed(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(final Creature activeChar, GArray<Creature> targets)
	{
		for(final Creature target : targets)
		{
			ThreadPoolManager.getInstance().execute(new RunnableImpl()
			{
				@Override
				public void runImpl() throws Exception
				{
					if(target instanceof FeedableBeastInstance)
					{
						((FeedableBeastInstance) target).onSkillUse((Player) activeChar, _id);
					}
				}
			});
		}
	}
}
