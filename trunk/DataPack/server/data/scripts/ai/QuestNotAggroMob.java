package ai;

import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.QuestEventType;
import l2next.gameserver.model.quest.QuestState;

import java.util.List;

/**
 * @author VISTALL
 * @date 8:44/10.06.2011
 */
public class QuestNotAggroMob extends DefaultAI
{
	public QuestNotAggroMob(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public boolean thinkActive()
	{
		return false;
	}

	@Override
	public void onEvtAttacked(Creature attacker, int dam)
	{
		NpcInstance actor = getActor();
		Player player = attacker.getPlayer();

		if(player != null)
		{
			List<QuestState> quests = player.getQuestsForEvent(actor, QuestEventType.ATTACKED_WITH_QUEST, false);
			if(quests != null)
			{
				for(QuestState qs : quests)
				{
					qs.getQuest().notifyAttack(actor, qs);
				}
			}
		}
	}

	@Override
	public void onEvtAggression(Creature attacker, int d)
	{
		//
	}
}
