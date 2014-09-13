package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.network.serverpackets.SocialAction;
import l2next.gameserver.scripts.ScriptFile;

/**
 * User: Mangol
 * Date: 24.08.12
 * Time: 19:11
 * P.S. Оригинал GodWorld Перелопатил под овераподобные Mangol
 */
public class _465_WeAreFriends extends Quest implements ScriptFile
{

	private static final int _fairy = 32921;
	private static final int _fairyFromCocone = 32923;
	private static final int littleCocone = 32919;
	private static final int _gratitudeSign = 17377;
	private static final int _forestFairyHorn = 17378;
	private static final int _proofPromises = 30384;

	public _465_WeAreFriends()
	{
		super(false);
		addStartNpc(_fairy);
		addTalkId(_fairy, _fairyFromCocone);
		addSkillUseId(littleCocone);
		addKillId(littleCocone);
		addQuestItem(_gratitudeSign);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("32921-02.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equals("32921-05.htm"))
		{
			st.giveItems(_forestFairyHorn, 1);
			st.giveItems(_proofPromises, Rnd.get(2, 4));
			st.setState(COMPLETED);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		else if(event.equals("32923-01.htm"))
		{
			st.playSound(SOUND_ITEMGET);
			st.giveItems(_gratitudeSign, 1);
			if(st.getQuestItemsCount(17377) >= 2)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
			npc.broadcastPacket(new SocialAction(npc.getObjectId(), 2));
			npc.deleteMe();
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		if(npcId == _fairy)
		{
			switch(st.getState())
			{
				case CREATED:
					if(st.isNowAvailableByTime())
					{
						if(st.getPlayer().getLevel() >= 88)
						{
							htmltext = "32921-00.htm";
						}
						else
						{
							htmltext = "32921-nolvl.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "32921-noday.htm";
					}
					break;
				case STARTED:
					if(st.getCond() == 1)
					{
						htmltext = "32921-03.htm";
					}
					else if(st.getCond() == 2)
					{
						htmltext = "32921-04.htm";
					}
					break;
			}

		}
		else if(npcId == _fairyFromCocone)
		{
			if(st.isStarted())
			{
				htmltext = "32923-00.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if(npc.getNpcId() == littleCocone)
		{
			if(Rnd.chance(5))
			{
				st.addSpawn(_fairyFromCocone, 30000);
			}
		}
		return null;
	}

	@Override
	public String onSkillUse(NpcInstance npc, Skill skill, QuestState st)
	{
		if(npc.getNpcId() == littleCocone)
		{
			if(skill.getId() == 12002)
			{
				if(Rnd.chance(10))
				{
					st.addSpawn(_fairyFromCocone, 30000);
				}
			}
		}
		return super.onSkillUse(npc, skill, st);
	}

	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}
