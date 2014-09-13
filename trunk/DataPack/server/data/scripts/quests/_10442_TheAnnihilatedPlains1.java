package quests;

import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;

//By ChipWar

public class _10442_TheAnnihilatedPlains1 extends Quest implements ScriptFile
{
	private static final int tuska = 33839;
	private static final int mathias = 31340;

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

	public _10442_TheAnnihilatedPlains1()
	{
		super(false);
		addStartNpc(mathias);
		addTalkId(tuska);
		addTalkId(mathias);

		addLevelCheck(99, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;

		if(event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-4.htm";
		}

		if(event.equalsIgnoreCase("qet_rev"))
		{
			st.getPlayer().addExpAndSp(15436575, 154365);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";

		if(npcId == mathias)
		{
			if(st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if(cond == 0 && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if(cond == 1)
			{
				htmltext = "0-4.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if(npcId == tuska)
		{
			if(st.isCompleted())
			{
				htmltext = "1-c.htm";
			}
			else if(cond == 0)
			{
				htmltext = "1-nc.htm";
			}
			else if(cond == 1)
			{
				htmltext = "1-1.htm";
			}
		}
		return htmltext;

	}
}