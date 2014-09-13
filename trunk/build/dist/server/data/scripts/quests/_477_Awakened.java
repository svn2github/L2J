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

import l2next.commons.util.Rnd;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

public class _477_Awakened extends Quest implements ScriptFile
{
	public static final int GUIDE = 33463;
	public static final int JASTIN = 31282;
	private final int[] Mobs = {
		21294,
		21295,
		21296,
		21297,
		21298,
		21299,
		21300,
		21301,
		21302,
		21303,
		21304,
		21305,
		21307,
		21312,
		21313
	};
	public static final int BLOOD_TEARS = 19496;

	public _477_Awakened()
	{
		super(true);
		addStartNpc(33463);
		addTalkId(new int[]{
			31282
		});
		addKillId(Mobs);
		addQuestItem(new int[]{
			19496
		});
		addLevelCheck(70, 74);
	}

	@Override
	public void onShutdown()
	{
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
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
		st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == 33463)
		{
			if(state == 1)
			{
				if(!st.isNowAvailableByTime())
				{
					return "33463-07.htm";
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
		if((npcId == 31282) && (state == 2))
		{
			if(!st.isNowAvailableByTime())
			{
				return "30855-03.htm";
			}

			if(cond == 1)
			{
				return "31282-02.htm";
			}
			if(cond == 2)
			{
				st.giveItems(57, 334560L);
				st.addExpAndSp(8534700L, 8523390L);
				st.takeItems(19496, -1L);
				st.unset("cond");
				st.playSound("SOUND_FINISH");
				st.exitCurrentQuest(this);
				return "31282-01.htm";
			}
		}
		return "noquest";
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
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if((cond != 1) || (npc == null))
		{
			return null;
		}
		if((ArrayUtils.contains(Mobs, npc.getNpcId())) && (Rnd.chance(10)))
		{
			st.giveItems(19496, 1L);
		}
		if(st.getQuestItemsCount(19496) >= 45L)
		{
			st.setCond(2);
		}
		return null;
	}
}