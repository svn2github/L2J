package quests;

import gnu.trove.map.hash.TIntIntHashMap;
import l2next.commons.lang.ArrayUtils;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.network.serverpackets.ExQuestNpcLogList;
import l2next.gameserver.scripts.ScriptFile;

/**
 * @author JustForFun
 * @date 22.03.2013
 */

public class _479_DestroyTraskenEggs extends Quest implements ScriptFile
{
	private static final int NPC_DAICHIR = 30537;
	private static final int MOB_EGG = 19080;
	private static final int ITEM_SPRAY1 = 17620;
	private static final int ITEM_SPRAY2 = 17621;
	private static final int ITEM_SPRAY3 = 17622;
	Integer[] skills = new Integer[]{
		12005,
		12006,
		12007
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

	public _479_DestroyTraskenEggs()
	{
		super(PARTY_ONE);

		addStartNpc(NPC_DAICHIR);
		addKillId(MOB_EGG);
		addQuestItem(ITEM_SPRAY1, ITEM_SPRAY2, ITEM_SPRAY3);
		addSkillUseId(MOB_EGG);
		addLevelCheck(85, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30537-06.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.giveItems(ITEM_SPRAY1, 30);
			st.giveItems(ITEM_SPRAY2, 30);
			st.giveItems(ITEM_SPRAY3, 30);
		}
		else if(event.equalsIgnoreCase("30537-12.htm"))
		{
			if(st.getInt("_1") >= 5)
			{
				st.giveItems(57, 993824, true);    // Учитываем рейты
				st.playSound("ItemSound.quest_finish");
				st.exitCurrentQuest(false);
			}
			else
			{
				return "30537-11.htm";
			}
		}

		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";

		if(npc.getNpcId() == 30537)
		{
			switch(st.getState())
			{
				case DELAYED:
					htmltext = "30537-noday.htm";
					break;
				case CREATED:
					if(st.getPlayer().getLevel() >= 85)
					{
						htmltext = "30537-00.htm";
					}
					else
					{
						htmltext = "30537-nolvl.htm";

						st.exitCurrentQuest(true);
					}

					break;
				case STARTED:
					if(st.getCond() == 1)
					{
						htmltext = "30537-07.htm";
					}
					else
					{
						if(st.getCond() != 2)
						{
							break;
						}

						htmltext = "30537-10.htm";
					}
			}
		}

		return htmltext;
	}

	@Override
	public String onSkillUse(NpcInstance npc, Skill skill, QuestState qs)
	{
		if((qs.getCond() == 1) && (ArrayUtils.contains(skills, skill.getId())))
		{
			TIntIntHashMap moblist = new TIntIntHashMap();
			int count = qs.getInt("_1");

			if(npc.getNpcId() == 19080)
			{
				count++;

				npc.doDie(qs.getPlayer());
				qs.set("_1", String.valueOf(count));
				qs.playSound("ItemSound.quest_itemget");
				moblist.put(1019080, count);
				// qs.getPlayer().sendPacket(new ExQuestNpcLogList(479, moblist));
				qs.getPlayer().sendPacket(new ExQuestNpcLogList(qs));

				if(count >= 5)
				{
					qs.playSound("ItemSound.quest_middle");
					qs.setCond(2);
				}
			}
		}

		return "";
	}
}
