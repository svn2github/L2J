package quests;

import l2next.commons.util.Rnd;
import l2next.gameserver.handler.items.IItemHandler;
import l2next.gameserver.handler.items.ItemHandler;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.entity.olympiad.Olympiad;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.network.serverpackets.ExStartScenePlayer;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * User: Samurai
 */
public class _10369_NoblesseTheTestOfSoul extends Quest implements ScriptFile, IItemHandler
{
	private static final int Керенас = 31281;
	private static final int ВоплощениеЕвы = 33686;
	private static final int Лания = 33696;
	private static final int[] cond8mobs = {
		21322,
		21320,
		21323
	};
	private static final int[] cond12mobs = {
		22261,
		22262,
		22263,
		22264,
		22265,
		22266
	};

	@Override
	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	public _10369_NoblesseTheTestOfSoul()
	{
		super(false);
		addStartNpc(Керенас);
		addTalkId(Керенас, ВоплощениеЕвы, Лания);
		addKillId(27482);
		addKillId(cond8mobs);
		addKillId(cond12mobs);
		addQuestItem(34886);
		addLevelCheck(75, 99);
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if(qs.getPlayer().isBaseClassActive())
		{
			return "Subclass only!";
		}

		int cond = qs.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";

		switch(npcId)
		{
			case Керенас:
				if(!qs.isStarted())
				{
					htmltext = "31281.htm";
				}
				switch(cond)
				{
					case 1:
						htmltext = "31281-1.htm";
						break;
					case 2:
						htmltext = "31281-3.htm";
						break;
					case 3:
						htmltext = "31281-4.htm";
						break;
					case 4:
						htmltext = "31281-6.htm";
						break;
					case 5:
						htmltext = "31281-7.htm";
						break;
					case 14:
						qs.setCond(15);
						enterInstance(qs.getPlayer());
						htmltext = null;
						break;
					case 17:
						qs.setCond(18);
						enterInstance(qs.getPlayer());
						htmltext = null;
						break;
				}
				break;
			case ВоплощениеЕвы:
				switch(cond)
				{
					case 5:
						htmltext = "33686-1.htm";
						break;
					case 6:
						htmltext = "33686-4.htm";
						break;
					case 15:
						htmltext = "33686-5.htm";
						qs.takeItems(34913, 1);
						qs.giveItems(34961, 1);
						qs.giveItems(34981, 1);
						qs.setCond(16);
						break;
					case 18:
						qs.giveItems(7694, 1);
						qs.giveItems(7562, 10);
						qs.addExpAndSp(12625440, 0);
						htmltext = "33686-7.htm";
						Olympiad.addNoble(qs.getPlayer());
						qs.getPlayer().setNoble(true);
						qs.getPlayer().updatePledgeClass();
						qs.getPlayer().updateNobleSkills();
						qs.getPlayer().sendSkillList();
						qs.getPlayer().broadcastUserInfo(true);
						qs.exitCurrentQuest(false);
						qs.playSound(SOUND_FINISH);
						break;
				}
				break;
			case Лания:
				switch(cond)
				{
					case 7:
						htmltext = "33696-1.htm";
						break;
					case 8:
						htmltext = "33696-3.htm";
						break;
					case 9:
						htmltext = "33696-4.htm";
						break;
					case 10:
						htmltext = "33696-6.htm";
						break;
					case 11:
						htmltext = "33696-7.htm";
						break;
					case 12:
						htmltext = "33696-9.htm";
						break;
					case 13:
						htmltext = "33696-10.htm";
						qs.setCond(14);
						qs.giveItems(34913, 1);
						qs.takeAllItems(34892);
						break;
					case 14:
						htmltext = "33696-11.htm";
						break;
				}
				break;
		}

		return htmltext;
	}

	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("Start"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			qs.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_SC_NOBLE_OPENING);
			htmltext = null;
		}
		else if(event.equalsIgnoreCase("31281-2.htm"))
		{
			qs.setCond(2);
			htmltext = "31281-3.htm";
		}
		else if(event.equalsIgnoreCase("31281-5.htm"))
		{
			qs.takeItems(34886, 1); // Фрагмент пророчества, квестовый итем
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(4);
			qs.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_SC_NOBLE_ENDING);
			htmltext = null;
		}
		else if(event.equalsIgnoreCase("31281-7.htm"))
		{
			qs.setCond(5);
			htmltext = "31281-7.htm";
		}
		else if(event.equalsIgnoreCase("31281-8.htm"))
		{
			enterInstance(qs.getPlayer());
			htmltext = null;
		}
		else if(event.equalsIgnoreCase("33686-3.htm"))
		{
			qs.giveItems(34887, 1); // Бутылка для Воды из Источники 34887
			qs.giveItems(34912, 1); // Камень Призыва 34912
			qs.giveItems(34978, 1); // Свиток Телепорта: Горячие Источники 34978
			qs.setCond(6);
			htmltext = null;
		}
		else if(event.equalsIgnoreCase("33696-2.htm"))
		{
			qs.takeItems(34888, 1);
			qs.setCond(8);
		}
		else if(event.equalsIgnoreCase("33696-5.htm"))
		{
			qs.takeAllItems(34889);
			qs.giveItems(34890, 1);
			qs.giveItems(34979, 1);
			qs.setCond(10);
		}
		else if(event.equalsIgnoreCase("33696-8.htm"))
		{
			qs.takeAllItems(34891);
			qs.takeAllItems(34890);
			qs.giveItems(34980, 1);
			qs.setCond(12);
		}
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		int cond = qs.getCond();
		if(cond == 2 && npc.getNpcId() == 27482 && Rnd.chance(50))
		{
			qs.giveItems(34886, 1); // Фрагмент пророчества, квестовый итем
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(3);
		}
		else if(cond == 8 && ArrayUtils.contains(cond8mobs, npc.getNpcId()))
		{
			qs.giveItems(34889, 1);
			qs.playSound(SOUND_MIDDLE);
			if(qs.getQuestItemsCount(34889) >= 10)
			{
				qs.setCond(9);
			}
		}
		else if(cond == 12 && ArrayUtils.contains(cond12mobs, npc.getNpcId()))
		{
			qs.giveItems(34892, 1);
			qs.playSound(SOUND_MIDDLE);
			if(qs.getQuestItemsCount(34892) >= 10)
			{
				qs.setCond(13);
			}
		}
		return null;
	}

	private void enterInstance(Player player)
	{
		Reflection r = player.getActiveReflection();
		if(r != null)
		{
			if(player.canReenterInstance(217))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if(player.canEnterInstance(217))
		{
			ReflectionUtils.enterReflection(player, 217);
		}
	}

	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		Player player = playable.getPlayer();
		QuestState qs = player.getQuestState(_10369_NoblesseTheTestOfSoul.class);
		if(qs == null)
		{
			return false;
		}
		int cond = qs.getCond();
		switch(item.getItemId())
		{
			case 34887:
				if(cond == 6)
				{
					Zone zone = ReflectionUtils.getZone("[spa_51]");
					if(zone.checkIfInZone(player))
					{
						qs.takeItems(34887, 1);
						qs.giveItems(34888, 1);
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(7);
					}
				}
				break;
			case 34912:
				if(GameObjectsStorage.getAsNpc(qs.getLong("Lanya")) != null)
				{
					return false;
				}
				NpcInstance npc = qs.addSpawn(Лания, player.getX(), player.getY(), player.getZ(), 16384, 0, 30 * 1000);
				qs.set("Lanya", npc.getStoredId());
				break;
			case 34890:
				if(cond == 10)
				{
					if(player.getTarget().isNpc())
					{
						NpcInstance flower = (NpcInstance) player.getTarget();
						if(flower.getNpcId() == 33735)
						{
							qs.giveItems(34891, 1);
							if(qs.getQuestItemsCount(34891) >= 5)
							{
								qs.setCond(11);
							}
						}
					}
				}
				break;
			case 34961:
				if(cond == 16)
				{
					qs.setCond(17);
					qs.takeItems(34961, 1);
					qs.addSpawn(27486, player.getX(), player.getY(), player.getZ(), 16384, 0, 30 * 1000);
				}
				break;
		}
		return false;
	}

	@Override
	public void dropItem(Player player, ItemInstance item, long count, Location loc)
	{
	}

	@Override
	public boolean pickupItem(Playable playable, ItemInstance item)
	{
		return false;
	}

	@Override
	public int[] getItemIds()
	{
		return new int[]{
			34887,
			34912,
			34890,
			34961
		};
	}
}