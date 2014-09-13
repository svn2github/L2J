package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _470_Divinity_Protector extends Quest implements ScriptFile
{
	//npc
	public static final int GUIDE = 33463;
	public static final int APRIGEL = 31348;

	//mobs
	private final int[] Mobs = {
		21520,
		21521,
		21522,
		21523,
		21524,
		21525,
		21526,
		21542,
		21543,
		21527,
		21528,
		21529,
		21541,
		21530,
		21531,
		21532,
		21533,
		21534,
		21535,
		21536,
		21545,
		21546,
		21537,
		21538,
		21539,
		21540,
		21544
	};

	//q items
	public static final int COLORLESS_SOUL = 19489;

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

	public _470_Divinity_Protector()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(APRIGEL);
		addKillId(Mobs);
		addQuestItem(COLORLESS_SOUL);
		addLevelCheck(60, 64);
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
		if(npcId == APRIGEL && state == 2)
		{
			if(!st.isNowAvailableByTime())
			{
				return "31348-03.htm";
			}

			if(cond == 1)
			{
				return "31348-01.htm";
			}
			if(cond == 2)
			{
				st.giveItems(57, 194000);
				st.addExpAndSp(1879400, 1782000);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);
				return "31348-02.htm"; //no further html do here
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
			st.giveItems(COLORLESS_SOUL, 1);
			st.playSound(SOUND_MIDDLE);
		}
		if(st.getQuestItemsCount(COLORLESS_SOUL) >= 20)
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