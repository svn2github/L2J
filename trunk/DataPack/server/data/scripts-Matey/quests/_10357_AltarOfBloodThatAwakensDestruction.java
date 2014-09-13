/*     */
package quests;
/*     */ 
/*     */

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;

/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public class _10357_AltarOfBloodThatAwakensDestruction extends Quest
/*     */ implements ScriptFile
/*     */
{
	/*     */   private static final int JORJINO = 33515;
	/*     */   private static final int ELKARDIA = 32798;
	/*     */   private static final String A_LIST = "a_list";
	/*     */   private static final String B_LIST = "b_list";

	/*     */
/*     */
	public _10357_AltarOfBloodThatAwakensDestruction()
/*     */
	{
/*  32 */
		super(true);
/*  33 */
		addStartNpc(33515);
/*  34 */
		addTalkId(new int[]{33515});
/*  35 */
		addTalkId(new int[]{32798});
/*     */ 
/*  37 */
		addKillNpcWithLog(2, "a_list", 1, new int[]{25876});
/*  38 */
		addKillNpcWithLog(2, "b_list", 1, new int[]{25877});
/*     */ 
/*  40 */
		addLevelCheck(95, 99);
/*     */
	}

	/*     */
/*     */
	public void onShutdown()
/*     */
	{
/*     */
	}

	/*     */
/*     */
	public String onKill(NpcInstance npc, QuestState st)
/*     */
	{
/* 123 */
		int cond = st.getCond();
/* 124 */
		if(cond != 2)
/* 125 */
		{
			return null;
		}
/* 126 */
		boolean doneKill = updateKill(npc, st);
/* 127 */
		if(doneKill)
/*     */
		{
/* 129 */
			st.unset("a_list");
/* 130 */
			st.unset("b_list");
/* 131 */
			st.setCond(3);
/*     */
		}
/* 133 */
		return null;
/*     */
	}

	/*     */
/*     */
	public void onLoad()
/*     */
	{
/*     */
	}

	/*     */
/*     */
	public void onReload()
/*     */
	{
/*     */
	}

	/*     */
/*     */
	public String onEvent(String event, QuestState st, NpcInstance npc)
/*     */
	{
/*  46 */
		Player player = st.getPlayer();
/*  47 */
		String htmltext = event;
/*  48 */
		if(event.equalsIgnoreCase("33515-4.htm"))
/*     */
		{
/*  50 */
			st.setCond(1);
/*  51 */
			st.setState(2);
/*  52 */
			st.playSound("ItemSound.quest_accept");
/*     */
		}
/*  54 */
		if(event.equalsIgnoreCase("32798-1.htm"))
/*     */
		{
/*  56 */
			st.setCond(2);
/*  57 */
			st.playSound("ItemSound.quest_middle");
/*     */
		}
/*  59 */
		if(event.startsWith("give"))
/*     */
		{
/*  61 */
			if(event.equalsIgnoreCase("givematerials"))
/*     */
			{
/*  63 */
				st.giveItems(19305, 1L);
/*  64 */
				st.giveItems(19306, 1L);
/*  65 */
				st.giveItems(19307, 1L);
/*  66 */
				st.giveItems(19308, 1L);
/*     */
			}
/*  68 */
			else if(event.equalsIgnoreCase("giveenchants"))
/*     */
			{
/*  70 */
				st.giveItems(22561, 2L);
/*     */
			}
/*  72 */
			else if(event.equalsIgnoreCase("givesacks"))
/*     */
			{
/*  74 */
				st.giveItems(34861, 2L);
/*     */
			}
/*  76 */
			st.addExpAndSp(11000000L, 5000000L);
/*  77 */
			st.playSound("ItemSound.quest_finish");
/*  78 */
			st.exitCurrentQuest(false);
/*  79 */
			return "33515-7.htm";
/*     */
		}
/*  81 */
		return event;
/*     */
	}

	/*     */
/*     */
	public String onTalk(NpcInstance npc, QuestState st)
/*     */
	{
/*  87 */
		Player player = st.getPlayer();
/*  88 */
		int npcId = npc.getNpcId();
/*  89 */
		int state = st.getState();
/*  90 */
		int cond = st.getCond();
/*     */ 
/*  92 */
		if(state == 3)
		{
/*  93 */
			return "33515-comp.htm";
/*     */
		}
/*  95 */
		if(st.getPlayer().getLevel() < 95)
		{
/*  96 */
			return "33515-lvl.htm";
/*     */
		}
/*  98 */
		if(npcId == 33515)
/*     */
		{
/* 100 */
			if(cond == 0)
/* 101 */
			{
				return "33515.htm";
			}
/* 102 */
			if(cond == 1)
/* 103 */
			{
				return "33515-5.htm";
			}
/* 104 */
			if(cond == 2)
/* 105 */
			{
				return "33515-5.htm";
			}
/* 106 */
			if(cond == 3)
/* 107 */
			{
				return "33515-6.htm";
			}
/*     */
		}
/* 109 */
		else if(npcId == 32798)
/*     */
		{
/* 111 */
			if(cond == 1)
/* 112 */
			{
				return "32798.htm";
			}
/* 113 */
			if(cond == 2)
/* 114 */
			{
				return "32798-2.htm";
			}
/* 115 */
			if(cond == 3)
/* 116 */
			{
				return "32798-5.htm";
			}
/*     */
		}
/* 118 */
		return "noquest";
/*     */
	}
/*     */
}

/* Location:           D:\lorien\dist-game-1458M\game\libs\ёmmo-scripts.jar
 * Qualified Name:     quests._10357_AltarOfBloodThatAwakensDestruction
 * JD-Core Version:    0.6.2
 */