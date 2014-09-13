package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;

public class _478_NightmareOfDwarves extends Quest implements ScriptFile
{
	public static final int DAICHIR = 30537;
	public static final int TRASKEN = 33159;
	public static final String WPL = "WPL";
	public static final String BWPL = "BWPL";
	public static final String LCK = "LCK";

	public _478_NightmareOfDwarves()
	{
		super(true);
		addStartNpc(30537);
		addKillId(new int[]{33159});
		addKillNpcWithLog(2, "WPL", 10, new int[]{29199});
		addKillNpcWithLog(2, "BWPL", 10, new int[]{29198});
		addKillNpcWithLog(2, "LCK", 10, new int[]{29204});
		addLevelCheck(85, 99);
	}

	public void onShutdown()
	{
	}

	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if(cond == 2)
		{
			boolean doneKill = updateKill(npc, st);
			if(doneKill)
			{
				st.unset("WPL");
				st.unset("BWPL");
				st.unset("LCK");
				st.setCond(5);
			}
		}
		else if(cond == 3)
		{
			if(npc.getNpcId() == 33159)
			{
				st.setCond(4);
			}
		}
		return null;
	}

	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	private static int getReward()
	{
		int chance = Rnd.get(100);
		if((chance > 0) && (chance <= 15))
		{
			return 4342;
		}
		if((chance > 15) && (chance <= 30))
		{
			return 4343;
		}
		if((chance > 30) && (chance <= 45))
		{
			return 4344;
		}
		if((chance > 45) && (chance <= 60))
		{
			return 4345;
		}
		if((chance > 60) && (chance <= 75))
		{
			return 4346;
		}
		if((chance > 75) && (chance <= 90))
		{
			return 4347;
		}
		if((chance > 90) && (chance <= 95))
		{
			return 17623;
		}
		if((chance > 95) && (chance <= 100))
		{
			return 15559;
		}
		return 0;
	}

	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		String htmltext = event;
		if(event.equalsIgnoreCase("30537-6.htm"))
		{
			if((player.getLevel() >= 85) && (player.getLevel() <= 94))
			{
				st.setCond(2);
			}
			else
			{
				st.setCond(3);
			}
			st.setState(2);
			st.playSound("ItemSound.quest_accept");
		}
		return event;
	}

	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == 30537)
		{
			if(state == 1)
			{
				if(player.getLevel() < 85)
				{
					return "30537-lvl.htm";
				}
				if(!isAvailableFor(st.getPlayer()))
				{
					return "30537-comp.htm";
				}
				if(player.getLevel() < 85)
				{
					return "30537-lvl.htm";
				}
				return "30537.htm";
			}
			if(state == 2)
			{
				if((cond == 2) || (cond == 3))
				{
					return "30537-7.htm";
				}
				if(cond == 4)
				{
					int reward = getReward();
					st.giveItems(reward, 1L);
					st.unset("cond");
					st.playSound("ItemSound.quest_finish");
					st.exitCurrentQuest(this);
					return "30537-16.htm";
				}

				if(cond == 5)
				{
					st.giveItems(57, 550000L);
					st.unset("cond");
					st.playSound("ItemSound.quest_finish");
					st.exitCurrentQuest(this);
					return "30537-16.htm";
				}
			}
		}
		return "noquest";
	}
}