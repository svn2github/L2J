package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _486_BeWell extends Quest implements ScriptFile
{
	//npc
	public static final int GUIDE = 33463;
	public static final int ANEKOBI = 31555;

	//mobs
	private static final int[] Mobs = {
		21508,
		21509,
		21510,
		21511,
		21512,
		21513,
		21514,
		21515,
		21516,
		21517,
		21518,
		21519
	};

	//q items
	public static final int STAKATO_PAN = 19498;

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

	public _486_BeWell()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(ANEKOBI);
		addKillId(Mobs);
		addQuestItem(STAKATO_PAN);
		addLevelCheck(70, 74);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(event.equalsIgnoreCase("33463-04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == GUIDE)
		{
			if(state == 1)
			{
				if(!st.isNowAvailableByTime())
				{
					return "33463-07.htm";
				}
				if(!isAvailableFor(st.getPlayer()))
				{
					return "33463-no.htm";
				}
				return "33463-01.htm";
			}
			if(state == 2)
			{
				if(cond == 1)
				{
					return "33463-05.htm";
				}
				if(cond == 2)
				{
					return "33463-06.htm";
				}

			}
		}
		if(npcId == ANEKOBI && state == 2)
		{
			if(!st.isNowAvailableByTime())
			{
				return "31555-03.htm";
			}
			if(cond == 1)
			{
				return "31555-02.htm";
			}
			else if(cond == 2)
			{
				st.addExpAndSp(9009000, 8997060);
				st.giveItems(57, 353160);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);
				return "31555-01.htm"; //no further html do here
			}
		}
		return "noquest";
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if(cond != 1 || npc == null)
		{
			return null;
		}
		if(ArrayUtils.contains(Mobs, npc.getNpcId()) && Rnd.chance(50))
		{
			st.giveItems(STAKATO_PAN, 2);
			st.playSound(SOUND_MIDDLE);
		}
		if(st.getQuestItemsCount(STAKATO_PAN) >= 80)
		{
			st.setCond(2);
		}

		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		boolean aval = true;
		if(!isAvailableFor(player))
			aval = false;
			
		return aval;
	}
}