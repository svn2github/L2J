package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.instancemanager.AwakingManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.SubClass;
import l2next.gameserver.model.base.ProfessionRewards;
import l2next.gameserver.model.base.SubClassType;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.network.serverpackets.ExSubjobInfo;
import l2next.gameserver.network.serverpackets.SocialAction;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Unknow + GW
 * @modified KilRoy
 * @name 177 - Split Destiny
 * @category One quest. Solo
 * @see http://l2wiki.com/Split_Destiny
 */
public class _177_SplitDestiny extends Quest implements ScriptFile
{
	private static final int HADEL = 33344;
	private static final int ISHUMA = 32615;
	private static final int RED_SOUL_CRYSTAL_STAGE15 = 10480;
	private static final int BLUE_SOUL_CRYSTAL_STAGE15 = 10481;
	private static final int GREEN_SOUL_CRYSTAL_STAGE15 = 10482;
	private static final int UNIDENTIFIED_TWILIGHT_NECKLACE = 18168;
	private static final int PETRIFIED_GIGANTS_HAND = 17718;
	private static final int PETRIFIED_GIGANTS_FOOT = 17719;
	private static final int PETRIFIET_GIGANTS_HAND_PIECE = 17720;
	private static final int PETRIFIET_GIGANTS_FOOT_PIECE = 17721;
	private static final int[] MOBS_1 = {
		21549,
		21550,
		21547,
		21548,
		21587
	};
	private static final int[] MOBS_2 = {
		22257,
		22258,
		22259,
		22260
	};

	public _177_SplitDestiny()
	{
		super(false);
		addStartNpc(HADEL);
		addTalkId(HADEL);
		addTalkId(ISHUMA);
		addQuestItem(PETRIFIED_GIGANTS_HAND, PETRIFIED_GIGANTS_FOOT, PETRIFIET_GIGANTS_HAND_PIECE, PETRIFIET_GIGANTS_FOOT_PIECE);
		addKillId(MOBS_1);
		addKillId(MOBS_2);

		addLevelCheck(80, 99);
		addClassLevelCheck(4);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "33344-14.htm";
		}
		else if(event.equalsIgnoreCase("quest_next"))
		{
			st.setCond(4);
			st.playSound(SOUND_MIDDLE);
			htmltext = "33344-19.htm";
		}
		else if(event.equalsIgnoreCase("quest_next1"))
		{
			st.setCond(7);
			st.playSound(SOUND_MIDDLE);
			htmltext = "33344-22.htm";
		}
		else if(event.equalsIgnoreCase("quest_next2"))
		{
			st.takeAllItems(PETRIFIET_GIGANTS_HAND_PIECE);
			st.takeAllItems(PETRIFIET_GIGANTS_FOOT_PIECE);
			st.setCond(8);
			st.playSound(SOUND_MIDDLE);
			htmltext = "32615-03.htm";
		}
		else if(event.equalsIgnoreCase("quest_next3"))
		{
			st.takeAllItems(PETRIFIED_GIGANTS_HAND);
			st.takeAllItems(PETRIFIED_GIGANTS_FOOT);
			st.set("accepted", 1);
			htmltext = "33344-25.htm";
		}
		else if(event.equalsIgnoreCase("red"))
		{
			st.giveItems(RED_SOUL_CRYSTAL_STAGE15, 1);
			st.giveItems(UNIDENTIFIED_TWILIGHT_NECKLACE, 1);
			st.addExpAndSp(175739575, 2886300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			st.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			AwakingManager.getInstance().onPlayerEnter(st.getPlayer());
			st.getPlayer().sendPacket(new ExSubjobInfo(st.getPlayer(), true));
			st.getPlayer().broadcastPacket(new SocialAction(st.getPlayer().getObjectId(), 2122));
			st.getPlayer().sendPacket(new SystemMessage(SystemMessage.SUBCLASS_S1_HAS_BEEN_UPGRADED_TO_DUAL_CLASS_S2_CONGRATULATION).addClassName(st.getPlayer().getActiveClassId()).addClassName(st.getPlayer().getActiveClassId()));
			return "33344-29.htm";
		}
		else if(event.equalsIgnoreCase("blue"))
		{
			st.giveItems(BLUE_SOUL_CRYSTAL_STAGE15, 1);
			st.giveItems(UNIDENTIFIED_TWILIGHT_NECKLACE, 1);
			st.addExpAndSp(175739575, 2886300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			st.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			AwakingManager.getInstance().onPlayerEnter(st.getPlayer());
			st.getPlayer().sendPacket(new ExSubjobInfo(st.getPlayer(), true));
			st.getPlayer().broadcastPacket(new SocialAction(st.getPlayer().getObjectId(), 2122));
			st.getPlayer().sendPacket(new SystemMessage(SystemMessage.SUBCLASS_S1_HAS_BEEN_UPGRADED_TO_DUAL_CLASS_S2_CONGRATULATION).addClassName(st.getPlayer().getActiveClassId()).addClassName(st.getPlayer().getActiveClassId()));
			return "33344-29.htm";
		}
		else if(event.equalsIgnoreCase("green"))
		{
			st.giveItems(GREEN_SOUL_CRYSTAL_STAGE15, 1);
			st.giveItems(UNIDENTIFIED_TWILIGHT_NECKLACE, 1);
			st.addExpAndSp(175739575, 2886300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			st.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			AwakingManager.getInstance().onPlayerEnter(st.getPlayer());
			st.getPlayer().sendPacket(new ExSubjobInfo(st.getPlayer(), true));
			st.getPlayer().broadcastPacket(new SocialAction(st.getPlayer().getObjectId(), 2122));
			st.getPlayer().sendPacket(new SystemMessage(SystemMessage.SUBCLASS_S1_HAS_BEEN_UPGRADED_TO_DUAL_CLASS_S2_CONGRATULATION).addClassName(st.getPlayer().getActiveClassId()).addClassName(st.getPlayer().getActiveClassId()));
			return "33344-29.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();

		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();

		if(npcId == HADEL)
		{
			if(st.isCompleted())
			{
				htmltext = "33344-12.htm";
			}
			if(cond == 0 && isAvailableFor(player))
			{
				if(player.getActiveSubClass().isSub())
				{
					if(player.getBaseClassId() >= 139)
					{
						int checkForFutureClass = ProfessionRewards.getAwakenedClassForId(player.getActiveClassId());

						if(checkForFutureClass == -1)
						{
							return "33344-16.htm";
						}

						if(checkForFutureClass == player.getBaseClassId())
						{
							return "33344-noid-" + checkForFutureClass + ".htm";
						}

						boolean alreadyHaveDualClass = false;

						for(SubClass sub : player.getSubClassList().values())
						{
							if(sub.isDouble())
							{
								alreadyHaveDualClass = true;
							}
						}

						if(!alreadyHaveDualClass)
						{
							htmltext = "33344-13.htm";
						}
						else
						{
							htmltext = "33344-12.htm";
						}
					}
					else
					{
						htmltext = "33344-03.htm";
					}
				}
				else
				{
					htmltext = "33344-02.htm";
				}
			}
			else if(cond == 1 || cond == 2)
			{
				htmltext = "33344-15.htm";
			}
			else if(cond == 3)
			{
				htmltext = "33344-17.htm";
			}
			else if(cond == 4 || cond == 5)
			{
				htmltext = "33344-20.htm";
			}
			else if(cond == 6)
			{
				htmltext = "33344-21.htm";
			}
			else if(cond == 7 || cond == 8)
			{
				htmltext = "33344-23.htm";
			}
			else if(cond == 9)
			{
				if(st.get("accepted") != null)
				{
					htmltext = "33344-25.htm";
				}
				else
				{
					htmltext = "33344-24.htm";
				}
			}
			else
			{
				htmltext = "33344-02.htm";
			}
		}
		if(npcId == ISHUMA)
		{
			if(cond == 7)
			{
				htmltext = "32615-01.htm";
			}
			else if(cond == 8)
			{
				st.setCond(9);
				st.giveItems(PETRIFIED_GIGANTS_HAND, 2);
				st.giveItems(PETRIFIED_GIGANTS_FOOT, 2);
				htmltext = "32615-04.htm";
			}
			else
			{
				htmltext = "32615-05.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();

		if(cond == 1 && ArrayUtils.contains(MOBS_1, npcId) && Rnd.chance(30))
		{
			if(st.getQuestItemsCount(PETRIFIET_GIGANTS_HAND_PIECE) < 1)
			{
				st.giveItems(PETRIFIET_GIGANTS_HAND_PIECE, 1);
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
		}
		else if(cond == 2 && ArrayUtils.contains(MOBS_1, npcId) && Rnd.chance(30))
		{
			if(st.getQuestItemsCount(PETRIFIET_GIGANTS_HAND_PIECE) < 10)
			{
				st.giveItems(PETRIFIET_GIGANTS_HAND_PIECE, 1);
				st.playSound(SOUND_ITEMGET);
				if(st.getQuestItemsCount(PETRIFIET_GIGANTS_HAND_PIECE) >= 10)
				{
					st.playSound(SOUND_MIDDLE);
					st.setCond(3);
				}
			}
		}
		else if(cond == 4 && ArrayUtils.contains(MOBS_2, npcId) && Rnd.chance(30))
		{
			if(st.getQuestItemsCount(PETRIFIET_GIGANTS_FOOT_PIECE) < 1)
			{
				st.giveItems(PETRIFIET_GIGANTS_FOOT_PIECE, 1);
				st.playSound(SOUND_MIDDLE);
				st.setCond(5);
			}
		}
		else if(cond == 5 && ArrayUtils.contains(MOBS_2, npcId) && Rnd.chance(30))
		{
			if(st.getQuestItemsCount(PETRIFIET_GIGANTS_FOOT_PIECE) < 10)
			{
				st.giveItems(PETRIFIET_GIGANTS_FOOT_PIECE, 1);
				st.playSound(SOUND_ITEMGET);
				if(st.getQuestItemsCount(PETRIFIET_GIGANTS_FOOT_PIECE) >= 10)
				{
					st.playSound(SOUND_MIDDLE);
					st.setCond(6);
				}
			}
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