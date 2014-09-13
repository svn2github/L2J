package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;

import java.util.HashMap;
import java.util.Map;

public class _619_RelicsOfTheOldEmpire extends Quest implements ScriptFile
{
	// Items
	private static final int Entrance_Pass_to_the_Sepulcher = 7075;
	private static final int Broken_Relic_Part = 7254;
	// NPCs
	private static final int GHOST = 31538;

	private static final Map<Integer, Integer> drops = new HashMap<Integer, Integer>();

	static
	{

		drops.put(23186, 53);
		drops.put(23187, 36);
		drops.put(23188, 39);
		drops.put(23189, 48);
		drops.put(23190, 62);
		drops.put(23191, 42);
		drops.put(23192, 48);
		drops.put(23193, 47);
		drops.put(23194, 49);
		drops.put(23195, 34);
		drops.put(23196, 36);
		drops.put(23197, 61);
		drops.put(23198, 60);
		
	}

	public static final int[] Recipes = {
		6881,
		6883,
		6885,
		6887,
		7580,
		6891,
		6893,
		6895,
		6897,
		6899
	};

	public _619_RelicsOfTheOldEmpire()
	{
		super(true);
		addStartNpc(GHOST);
		addKillId(drops.keySet());
		addQuestItem(Broken_Relic_Part);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(event.equals("explorer_ghost_a_q0619_03.htm"))
		{
			if(st.getPlayer().getLevel() < 74)
			{
				st.exitCurrentQuest(true);
				return "explorer_ghost_a_q0619_02.htm";
			}
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equals("explorer_ghost_a_q0619_09.htm"))
		{
			if(st.getQuestItemsCount(Broken_Relic_Part) < 1000)
			{
				return st.getQuestItemsCount(Entrance_Pass_to_the_Sepulcher) > 0 ? "explorer_ghost_a_q0619_06.htm" : "explorer_ghost_a_q0619_07.htm";
			}
			st.takeItems(Broken_Relic_Part, 1000);
			st.giveItems(Recipes[Rnd.get(Recipes.length)], 1);
			return "explorer_ghost_a_q0619_09.htm";
		}
		else if(event.equals("explorer_ghost_a_q0619_10.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		if(st.getState() == CREATED)
		{
			if(st.getPlayer().getLevel() < 74)
			{
				st.exitCurrentQuest(true);
				return "explorer_ghost_a_q0619_02.htm";
			}
			st.setCond(0);
			return "explorer_ghost_a_q0619_01.htm";
		}

		if(st.getQuestItemsCount(Broken_Relic_Part) >= 1000)
		{
			return "explorer_ghost_a_q0619_04.htm";
		}

		return st.getQuestItemsCount(Entrance_Pass_to_the_Sepulcher) > 0 ? "explorer_ghost_a_q0619_06.htm" : "explorer_ghost_a_q0619_07.htm";
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		Integer Broken_Relic_Part_chance = drops.get(npcId);
		if(Broken_Relic_Part_chance == null)
		{
			return null;
		}

		st.rollAndGive(Broken_Relic_Part, 1, Broken_Relic_Part_chance);
		if(npcId > 20000) // npcId < 20000 тут это мобы из Four Goblets, из них билет в Sepulcher не должен падать
		{
			st.rollAndGive(Entrance_Pass_to_the_Sepulcher, 1, 3);
		}
		return null;
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