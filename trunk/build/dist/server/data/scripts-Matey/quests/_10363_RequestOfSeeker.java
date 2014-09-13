package quests;

import l2next.commons.lang.ArrayUtils;
import l2next.commons.util.Rnd;
import l2next.gameserver.listener.actor.player.OnSocialActionListener;
import l2next.gameserver.model.GameObject;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.actor.listener.CharListenerList;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.network.clientpackets.RequestActionUse;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.NpcUtils;

public class _10363_RequestOfSeeker extends Quest implements ScriptFile, OnSocialActionListener
{
	private static final int nagel = 33450;
	private static final int celin = 33451;

	private static final int[] souls = {
		19157,
		19158
	};
	private static final int[] corps = {
		32961,
		32962,
		32963,
		32964
	};

	private static final NpcString[] messages = {
		NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_ONE_CORPSE,
		NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_SECOND_CORPSE,
		NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_THIRD_CORPSE,
		NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_FOURTH_CORPSE,
		NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_FIFTH_CORPSE,
		NpcString.GRUDGE_OF_YE_SAGIRA_VICTIMS_HAVE_BEEN_RELIEVED_WITH_YOUR_TEARS
	};

	public void onLoad()
	{
		CharListenerList.addGlobal(this);
	}

	public void onReload()
	{
		CharListenerList.removeGlobal(this);
		CharListenerList.addGlobal(this);
	}

	@Override
	public void onShutdown()
	{
	}

	public _10363_RequestOfSeeker()
	{
		super(false);

		addStartNpc(nagel);
		addTalkId(celin);
		addTalkId(nagel);
		addLevelCheck(12, 20);
		addQuestCompletedCheck(_10362_CertificationOfSeeker.class);
	}

	@Override
	public void onSocialAction(Player player, GameObject target, RequestActionUse.Action action)
	{
		QuestState st = player.getQuestState(getName());
		if(st == null)
		{
			return;
		}
		int cond = st.getCond();
		if(cond < 1 || cond > 6)
		{
			return;
		}

		if(player.getTarget() == null || !player.getTarget().isNpc())
		{
			return;
		}

		NpcInstance npc = target instanceof NpcInstance ? (NpcInstance) target : null;
		if(npc == null)
		{
			return;
		}

		if(!ArrayUtils.contains(corps, npc.getNpcId()))
		{
			return;
		}

		if(player.getDistance(npc) > 70)
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.YOU_ARE_TOO_FAR_FROM_THE_CORPSE_TO_DO_THAT, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
			return;
		}

		if(action == RequestActionUse.Action.ACTION31 || action == RequestActionUse.Action.ACTION34)
		{
			dontFun(player, npc);
		}
		else if(action == RequestActionUse.Action.ACTION35)
		{
			if(cond < 6)
			{
				st.setCond(cond + 1);
				st.playSound(SOUND_MIDDLE);
				npc.doDie(player);
			}
			else
			{
				npc.deleteMe();
			}

			player.sendPacket(new ExShowScreenMessage(messages[cond - 1], 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
		}
	}

	private void dontFun(Player player, NpcInstance npc)
	{
		player.sendPacket(new ExShowScreenMessage(NpcString.DONT_TOY_WITH_DEAD, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
		NpcInstance soul;
		for(int soulId : souls)
		{
			soul = NpcUtils.spawnSingle(soulId, new Location(player.getX() - Rnd.get(100), player.getY() - Rnd.get(100), player.getZ(), 0));
			soul.getAggroList().addDamageHate(player, 0, 10000);
			soul.setAggressionTarget(player);
		}
		npc.doDie(player);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;

		if(event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-3.htm";
		}

		if(event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "1-3.htm";

			st.getPlayer().addExpAndSp(70200, 8100);
			st.giveItems(57, 48000);
			st.giveItems(1060, 100);
			st.giveItems(43, 1);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}

		return htmltext;
	}

	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";

		if(npcId == nagel)
		{
			if(st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if(cond == 1)
			{
				htmltext = "0-4.htm";
			}
			else if(cond == 6)
			{
				htmltext = "0-5.htm";

				st.setCond(7);
			}
			else if(cond == 7)
			{
				htmltext = "0-6.htm";
			}
			else
			{
				htmltext = TODO_FIND_HTML;
			}
		}
		if(npcId == celin)
		{
			if(st.isCompleted())
			{
				htmltext = "1-c.htm";
			}
			else if(cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if(cond == 7)
			{
				htmltext = "1-1.htm";
			}
		}
		return htmltext;
	}
}