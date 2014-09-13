package quests;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.party.Party;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;

/**
 * @author cruel
 * @name 496 - Воплощение ревности Каллиос
 * @category Daily quest. Party
 * @see http://l2on.net/?c=quests&id=496
 */
public class _496_KalliosTheEmbodimentOfGreed extends Quest implements ScriptFile
{
	private static final int KARTIA_RESEARCHER = 33647;
	private static final int KARTIA_RB = 25884;
	private static final int KARTIA_BOX = 34929;
	private static final String KARTIA_KILL = "KartiaRb";

	@Override
	public void onLoad()
	{
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	public _496_KalliosTheEmbodimentOfGreed()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCHER);
		addTalkId(KARTIA_RESEARCHER);
		addKillId(KARTIA_RB);
		addKillNpcWithLog(1, KARTIA_KILL, 1, KARTIA_RB);
		addLevelCheck(95, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("33647-04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("33647-7.htm"))
		{
			st.giveItems(KARTIA_BOX, 1);
			st.unset(KARTIA_KILL);
			st.setState(COMPLETED);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if(npcId == KARTIA_RESEARCHER)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 95)
				{
					if(st.isNowAvailableByTime())
					{
						htmltext = "33647-01.htm";
					}
					else
					{
						htmltext = "33647-08.htm";
					}
				}
				else
				{
					htmltext = "33647-01a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
			{
				htmltext = "33647-05.htm";
			}
			else if(cond == 2)
			{
				htmltext = "33647-06.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		boolean doneKill = updateKill(npc, st);
		Party party = st.getPlayer().getParty();
		if(party != null)
		{
			for(Player member : party.getPartyMembers())
			{
				QuestState qs = member.getQuestState(getClass());
				if(qs != null && qs.isStarted())
				{
					if(qs.getCond() == 1 && npc.getNpcId() == KARTIA_RB && doneKill)
					{
						qs.setCond(2);
					}
				}
			}
		}
		else
		{
			if(cond == 1 && npc.getNpcId() == KARTIA_RB)
			{
				if(doneKill)
				{
					st.setCond(2);
				}
			}
		}
		return null;
	}
}