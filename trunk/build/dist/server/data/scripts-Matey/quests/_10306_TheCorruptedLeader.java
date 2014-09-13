package quests;

import instances.KimerianNormal;
import l2next.commons.util.Rnd;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.ReflectionUtils;

public class _10306_TheCorruptedLeader extends Quest implements ScriptFile
{
	private static final int NPC_NAOMI_KASHERON = 32896;
	private static final int MOB_KIMERIAN = 25747;
	private static final int[] CRYSTALS = {
		9552,
		9553,
		9554,
		9555,
		9556,
		9557
	};

	public _10306_TheCorruptedLeader()
	{
		super(false);
		addStartNpc(NPC_NAOMI_KASHERON);
		addKillId(MOB_KIMERIAN);
		addQuestCompletedCheck(_10305_UnstoppableFutileEfforts.class);
		addLevelCheck(90, 99);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if(st == null)
		{
			return event;
		}
		if(event.equalsIgnoreCase("32896-05.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("enter_instance"))
		{
			st.setCond(2);
			enterInstance(st.getPlayer());
		}
		else if(event.equalsIgnoreCase("32896-08.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.addExpAndSp(9479594, 4104484);
			st.giveItems(CRYSTALS[Rnd.get(CRYSTALS.length)], 1);
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
		Player player = st.getPlayer();
		QuestState prevst = player.getQuestState(_10305_UnstoppableFutileEfforts.class);
		if(npc.getNpcId() == NPC_NAOMI_KASHERON)
		{
			switch(st.getState())
			{
				case COMPLETED:
					htmltext = "32896-02.htm";
					break;
				case CREATED:
					if(player.getLevel() >= 90)
					{
						if((prevst != null) && (prevst.isCompleted()))
						{
							htmltext = "32896-01.htm";
						}
						else
						{
							//st.exitCurrentQuest(true);
							htmltext = "32896-03.htm";
						}
					}
					else
					{
						//st.exitCurrentQuest(true);
						htmltext = "32896-03.htm";
					}
					break;
				case STARTED:
					if(st.getCond() == 1)
					{
						htmltext = "32896-06.htm";
					}
					else
					{
						if(st.getCond() != 3)
						{
							break;
						}
						htmltext = "32896-07.htm";
					}
			}
		}
		return htmltext;
	}

	@Override
	public boolean isVisible(Player player)
	{
		QuestState qs = player.getQuestState(_10306_TheCorruptedLeader.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if((qs.getCond() == 2) && (npc.getNpcId() == MOB_KIMERIAN))
		{
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(3);
		}

		return null;
	}
	
	private void enterInstance(Player player)
	{
		Reflection reflection = player.getActiveReflection();
		if(reflection != null)
		{
			if(player.canReenterInstance(161))
			{
				player.teleToLocation(reflection.getTeleportLoc(), reflection);
			}
		}
		else if(player.canEnterInstance(161))
		{
			ReflectionUtils.enterReflection(player, new KimerianNormal(), 161);
		}
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
