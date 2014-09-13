package quests;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.party.Party;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;

/**
 * @author cruel
 * @name 499 - Налетчик измерений Каллиос
 * @category Daily quest. Solo
 * @see http://l2on.net/?c=quests&id=499
 */
public class _499_KalliosRaiderMeasurements extends Quest implements ScriptFile
{
	private static final int KARTIA_RESEARCHER = 33647;
	private static final int KARTIA_RB = 19255;
	private static final int KARTIA_BOX = 34932;
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

	public _499_KalliosRaiderMeasurements()
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
		if(event.equalsIgnoreCase("33647-07.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("33647-10.htm"))
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
						htmltext = "33647-03.htm";
					}
				}
				else
				{
					htmltext = "33647-02.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
			{
				htmltext = "33647-08.htm";
			}
			else if(cond == 2)
			{
				htmltext = "33647-09.htm";
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