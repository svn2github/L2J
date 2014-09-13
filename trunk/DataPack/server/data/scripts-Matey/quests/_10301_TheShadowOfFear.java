/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests;

import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.ScriptFile;

public class _10301_TheShadowOfFear extends Quest implements ScriptFile
{
	private static final int _rada = 33100;
	private static final int _slaki = 32893;
	@SuppressWarnings("unused")
	private static final int _bigForest = 33489;
	private static final int _summonedSpirit = 32915;
	private static final int _shimmeringCrystal = 17604;
	private static final int _bottleWithCapturedSpirit = 17588;
	private static final int _agathion = 17380;
	@SuppressWarnings("unused")
	private static final int CON1 = 17725;
	private static final int CON2 = 17819;
	@SuppressWarnings("unused")
	private static final int _crystallSkill = 12011;

	public _10301_TheShadowOfFear()
	{
		super(false);
		addStartNpc(_rada);
		addTalkId(_rada, _slaki);
		addKillId(_summonedSpirit);
		addQuestItem(_bottleWithCapturedSpirit, _shimmeringCrystal);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(st == null)
		{
			return event;
		}
		if(event.equals("33100-03.htm"))
		{
			st.setState(STARTED);
			st.setCond(2);
			st.playSound("ItemSound.quest_accept");
			st.giveItems(_shimmeringCrystal, 10L);
		}
		else if(event.equals("33100-05.htm"))
		{
			st.playSound("ItemSound.quest_middle");
			st.giveItems(_shimmeringCrystal, 5L);
		}
		else if(event.equals("32893-02.htm"))
		{
			st.playSound("ItemSound.quest_middle");
			st.takeItems(_bottleWithCapturedSpirit, -1L);
		}
		else if(event.equals("32893-04.htm"))
		{
			st.addExpAndSp(26920620, 11389320);
			st.getPlayer().addAdena(1863420);
			st.giveItems(_agathion, 1);
			st.playSound("ItemSound.quest_finish");
			st.exitCurrentQuest(false);
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
		if(npc.getNpcId() == _rada)
		{
			switch(st.getState())
			{
				case 2:
					htmltext = HtmCache.getInstance().getNotNull("data/html/completed-quest.htm", st.getPlayer());
					break;
				case 0:
					if(st.getPlayer().getLevel() >= 88)
					{
						htmltext = "33100-00.htm";
					}
					else
					{
						htmltext = "33100-nolvl.htm";
						st.exitCurrentQuest(true);
					}
					break;
				case 1:
					if(st.getCond() == 1)
					{
						htmltext = "33100-00.htm";
						st.takeItems(CON2, -1L);
					}
					else if(st.getCond() == 2)
					{
						htmltext = "33100-04.htm";
					}
					else
					{
						if(st.getCond() != 3)
						{
							break;
						}
						htmltext = "33100-06.htm";
					}
			}
		}
		else if(npc.getNpcId() == 32893)
		{
			switch(st.getState())
			{
				case 2:
					htmltext = "32893-05.htm";
					break;
				case 1:
					if(st.getCond() != 3)
					{
						break;
					}
					htmltext = "32893-00.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if((npc.getNpcId() == 32915) && (st.getCond() == 2))
		{
			st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.They_managed_to_do_everything_on_the_first_try, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false));
			st.takeItems(17604, -1);
			st.giveItems(17588, 1);
			st.setCond(3);
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
