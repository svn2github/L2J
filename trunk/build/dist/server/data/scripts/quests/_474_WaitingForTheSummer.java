package quests;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _474_WaitingForTheSummer extends Quest implements ScriptFile
{
	private static final int NPC_ADVENTURER_HELPER = 33463;
	private static final int NPC_VISHOTSKY = 31981;
	private static final int ITEM_BUFFALO_MEAT = 19490;
	private static final int ITEM_URSUS_MEAT = 19491;
	private static final int ITEM_YETI_MEAT = 19492;
	private static final int[] BUFFALOS = {
		22093,
		22094
	};
	private static final int[] URSUS = {
		22095,
		22096
	};
	private static final int[] YETIS = {
		22097,
		22098
	};

	public _474_WaitingForTheSummer()
	{
		super(false);

		addStartNpc(NPC_ADVENTURER_HELPER);
		addTalkId(NPC_VISHOTSKY);
		addKillId(BUFFALOS);
		addKillId(URSUS);
		addKillId(YETIS);
		addQuestItem(ITEM_BUFFALO_MEAT, ITEM_URSUS_MEAT, ITEM_YETI_MEAT);
		addLevelCheck(60, 64);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(st == null)
		{
			return "noquest";
		}

		if(!st.isNowAvailableByTime())
		{

		}
		if(event.equalsIgnoreCase("33463-04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
		}

		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";

		if(st == null)
		{
			return htmltext;
		}

		if(npc.getNpcId() == NPC_ADVENTURER_HELPER)
		{

			switch(st.getState())
			{
				case DELAYED:
					htmltext = "daily";
					break;
				case CREATED:
					htmltext = "33463-01.htm";
					break;
				case STARTED:
					if(st.getCond() == 1)
					{
						htmltext = "33463-05.htm";
					}
					else
					{
						htmltext = "33463-06.htm";
					}
			}
		}
		else if(npc.getNpcId() == NPC_VISHOTSKY)
		{
			if(st.isStarted())
			{
				if(st.getCond() == 1)
				{
					htmltext = "31981-02.htm";
				}
				else if(st.getCond() == 2)
				{
					htmltext = "31981-01.htm";

					st.addExpAndSp(1879400, 1782000);
					st.giveItems(57, 194000, true);    // Учитываем рейты
					st.exitCurrentQuest(this);
				}
			}
			else if(st.getState() == DELAYED)
			{
				htmltext = "31981-03.htm";
			}
		}

		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if((npc == null) || (st == null))
		{
			return null;
		}

		if(st.getCond() == 1)
		{
			int npcId = npc.getNpcId();

			if(ArrayUtils.contains(YETIS, npcId))
			{
				st.rollAndGive(ITEM_YETI_MEAT, 1, 3, 30, 50);
			}
			else if(ArrayUtils.contains(BUFFALOS, npcId))
			{
				st.rollAndGive(ITEM_BUFFALO_MEAT, 1, 3, 30, 50);
			}
			else if(ArrayUtils.contains(URSUS, npcId))
			{
				st.rollAndGive(ITEM_URSUS_MEAT, 1, 3, 30, 50);
			}

			if(st.getQuestItemsCount(ITEM_BUFFALO_MEAT, ITEM_BUFFALO_MEAT, ITEM_YETI_MEAT) >= 90)
			{
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
			}
		}

		return null;
	}

	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_474_WaitingForTheSummer.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
	}

	public boolean isDailyQuest()
	{
		return true;
	}

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
}
