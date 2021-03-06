package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _490_DutyOfTheSurvivor extends Quest implements ScriptFile
{
	private static final int VOLLODOS = 30137;
	private static final int EXTRACT = 34059;
	private static final int BLOOD = 34060;
	private static final int DROP_CHANCE = 60;
	private static final int EXTRACT_MOBS[] = {
		23162,
		23163,
		23164,
		23165,
		23166,
		23167
	};
	private static final int BLOOD_MOBS[] = {
		23168,
		23169,
		23170,
		23171,
		23172,
		23173
	};

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

	public _490_DutyOfTheSurvivor()
	{
		super(false);
		addStartNpc(VOLLODOS);
		addTalkId(VOLLODOS);
		addKillId(EXTRACT_MOBS);
		addKillId(BLOOD_MOBS);
		addQuestItem((new int[]{
			34059,
			34060
		}));
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		if(event.equalsIgnoreCase("30137-05.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if(npcId == VOLLODOS)
		{
			if(st.getPlayer().getLevel() >= 85)
			{
				if(cond == 0)
				{
					if(st.isNowAvailableByTime())
					{
						htmltext = "30137-01.htm";
					}
					else
					{
						htmltext = "30137-08.htm";
					}
				}
				else if(cond == 1)
				{
					if(st.haveQuestItem(34059) || st.haveQuestItem(34060))
					{
						htmltext = "30137-09.htm";
					}
					else
					{
						htmltext = "30137-06.htm";
					}
				}
				if(cond == 2)
				{
					st.takeItems(34059, -1);
					st.takeItems(34060, -1);
					st.addExpAndSp(145557000, 58119840);
					st.giveItems(57, 505062);
					st.unset("cond");
					st.exitCurrentQuest(this);
					st.playSound(SOUND_FINISH);
					htmltext = "30137-07.htm";
				}
			}
			else
			{
				htmltext = "30137-00.htm";
			}
		}

		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if(ArrayUtils.contains(EXTRACT_MOBS, npc.getNpcId()) && st.getQuestItemsCount(34059) < 20 && Rnd.get(100) < 60)
		{
			st.giveItems(34059, 1);
			st.playSound(SOUND_ITEMGET);
		}
		if(ArrayUtils.contains(BLOOD_MOBS, npc.getNpcId()) && st.getQuestItemsCount(34060) < 20 && Rnd.get(100) < 60)
		{
			st.giveItems(34060, 1);
			st.playSound(SOUND_ITEMGET);
		}
		if(st.getQuestItemsCount(34059) == 20 && st.getQuestItemsCount(34060) == 20)
		{
			st.setCond(2);
			st.playSound("ItemSound.quest_middle");
		}
		return null;
	}
}