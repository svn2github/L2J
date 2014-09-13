package quests;

import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;

/**
 * @author GodWorld & Bonux
**/
public class _10377_TheInvadedExecutionGrounds extends Quest implements ScriptFile 
{
	// NPC's
	private static final int SYLVAIN = 30070;
	private static final int HARLAN = 30074;
	private static final int RODERIK = 30631;
	private static final int ENDRIGO = 30632;

	// Monster's
	private static final int HOUPON_THE_WARDEN_OVERSEER = 25886;
	private static final int CROOK_THE_MAD = 25887;
	private static final int EXECUTION_GROUNDS_WATCHMAN_GUILLOTINE = 25888;

	// Item's
	private static final int SOE_GUILLOTINE_FORTRESS = 35292;
	private static final int HARLANS_ORDERS = 34972;
	private static final int ENDRIGOS_REPORT = 34973;

	public _10377_TheInvadedExecutionGrounds()
	{
		super(true);
		addStartNpc(SYLVAIN);
		addTalkId(SYLVAIN, HARLAN, RODERIK, ENDRIGO);
		addKillId(HOUPON_THE_WARDEN_OVERSEER, CROOK_THE_MAD, EXECUTION_GROUNDS_WATCHMAN_GUILLOTINE);
		addQuestItem(HARLANS_ORDERS, ENDRIGOS_REPORT);
		addLevelCheck(95,99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc) 
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("sylvain_q10377_06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("hitsran_q10377_03.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			st.giveItems(HARLANS_ORDERS, 1);
		}
		else if(event.equalsIgnoreCase("warden_roderik_q10377_02.htm"))
			st.takeItems(HARLANS_ORDERS, -1L);
		else if(event.equalsIgnoreCase("warden_roderik_q10377_03.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
			st.giveItems(ENDRIGOS_REPORT, 1);
		}
		else if(event.equalsIgnoreCase("warden_endrigo_q10377_02.htm"))
		{
			st.addExpAndSp(756106110, 338608890);
			st.giveItems(ADENA_ID, 2970560, true);
			st.giveItems(SOE_GUILLOTINE_FORTRESS, 2);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st) 
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		String htmltext = "noquest";
		if(npcId == SYLVAIN)
		{
			if(st.isCompleted())
				htmltext = "sylvain_q10377_03.htm";
			else if(st.isStarted())
				htmltext = "sylvain_q10377_07.htm";
			else
			{
				if(isAvailableFor(st.getPlayer()))
					htmltext = "sylvain_q10377_01.htm";
				else
					htmltext = "sylvain_q10377_02.htm";
			}
		}
		else if(npcId == HARLAN)
		{
			if(cond == 1)
				htmltext = "hitsran_q10377_01.htm";
			else if(cond == 2)
				htmltext = "hitsran_q10377_04.htm";
		}
		else if(npcId == RODERIK)
		{
			if(cond == 2)
				htmltext = "warden_roderik_q10377_01.htm";
			else if(cond == 3)
				htmltext = "warden_roderik_q10377_04.htm";
			else if(cond == 6)
				htmltext = "warden_roderik_q10377_05.htm";
		}
		else if(npcId == ENDRIGO)
		{
			if(cond == 6)
			{
				st.takeItems(ENDRIGOS_REPORT, -1);
				htmltext = "warden_endrigo_q10377_01.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st) 
	{
		int cond = st.getCond();
		if(cond > 2)
		{
			switch(npc.getNpcId())
			{
				case HOUPON_THE_WARDEN_OVERSEER:
					if(cond != 3)
						break;
					st.setCond(4);
					st.playSound(SOUND_MIDDLE);
					break;
				case CROOK_THE_MAD:
					if(cond != 4)
						break;
					st.setCond(5);
					st.playSound(SOUND_MIDDLE);
					break;
				case EXECUTION_GROUNDS_WATCHMAN_GUILLOTINE:
					if(cond != 5)
						break;
					st.setCond(6);
					st.playSound(SOUND_MIDDLE);
					break;
			}
		}
		return null;
	}

	@Override
	public void onLoad()
	{
		//
	}

	@Override
	public void onReload()
	{
		//
	}

	@Override
	public void onShutdown()
	{
		//
	}
}