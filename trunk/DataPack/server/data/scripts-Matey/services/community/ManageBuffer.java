package services.community;

import l2next.gameserver.Config;
import l2next.gameserver.dao.CommunityBufferDAO;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.handler.bbs.CommunityBoardHandler;
import l2next.gameserver.handler.bbs.ICommunityBoardHandler;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.ManageBbsBuffer;
import l2next.gameserver.model.ManageBbsBuffer.SBufferScheme;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Summon;
import l2next.gameserver.model.base.TeamType;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.network.serverpackets.ShowBoard;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.skills.effects.EffectTemplate;
import l2next.gameserver.stats.Env;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.utils.BbsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.StringTokenizer;

public class ManageBuffer extends Functions implements ScriptFile, ICommunityBoardHandler
{

	static final Logger _log = LoggerFactory.getLogger(ManageBuffer.class);

	@Override
	public void onLoad()
	{
		if(Config.COMMUNITYBOARD_ENABLED && Config.COMMUNITYBOARD_BUFFER_ENABLED)
		{
			_log.info("CommunityBoard: Buffer Community service loaded.");
			CommunityBufferDAO.getInstance().select();
			CommunityBoardHandler.getInstance().registerHandler(this);
		}
	}

	@Override
	public void onReload()
	{
		if(Config.COMMUNITYBOARD_ENABLED && Config.COMMUNITYBOARD_BUFFER_ENABLED)
		{
			ManageBbsBuffer.getSchemeList().clear();
			CommunityBoardHandler.getInstance().removeHandler(this);
		}
	}

	@Override
	public void onShutdown()
	{
	}

	@Override
	public String[] getBypassCommands()
	{
		return new String[]{
			"_bbsbuff",
			"_bbsbaim",
			"_bbsbsingle",
			"_bbsbsave",
			"_bbsbrestore",
			"_bbsbdelete",
			"_bbsbregen",
			"_bbsbcansel",
			"_bbsblist"
		};
	}

	@Override
	public void onBypassCommand(Player player, String bypass)
	{

		if(!CheckCondition(player))
		{
			return;
		}

		if(bypass.startsWith("_bbsbuff"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			ShowHtml(mBypass.length == 1 ? "index" : mBypass[1], player);
		}
		if(bypass.startsWith("_bbsblist"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			int pice = Config.COMMUNITYBOARD_BUFF_PICE * (mBypass[1].startsWith("mage") ? Config.COMMUNITI_LIST_MAGE_SUPPORT.size() : Config.COMMUNITI_LIST_FIGHTER_SUPPORT.size());

			if(player.getAdena() < pice)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Недостаточно сердств!");
				}
				else
				{
					player.sendMessage("It is not enough money!");
				}
				ShowHtml(mBypass[2], player);
				return;
			}

			GroupBuff(player, mBypass[1].startsWith("mage") ? Config.COMMUNITI_LIST_MAGE_SUPPORT : Config.COMMUNITI_LIST_FIGHTER_SUPPORT);
			player.reduceAdena(pice);
			ShowHtml(mBypass[2], player);
		}
		else if(bypass.startsWith("_bbsbsingle"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			int id = Integer.parseInt(mBypass[1]);
			int lvl = Integer.parseInt(mBypass[2]);
			int time = Config.COMMUNITYBOARD_BUFF_TIME;
			int pice = Config.COMMUNITYBOARD_BUFF_PICE;
			String page = mBypass[3];

			if(player.getAdena() < pice)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Недостаточно сердств!");
				}
				else
				{
					player.sendMessage("It is not enough money!");
				}
				ShowHtml(page, player);
				return;
			}

			if(!Config.COMMUNITYBOARD_BUFF_ALLOW.contains(id))
			{
				if(player.isLangRus())
				{
					player.sendMessage("Недопустимый эффект!");
				}
				else
				{
					player.sendMessage("Invalid effect!");
				}
				ShowHtml(page, player);
				return;
			}

			Skill skill = SkillTable.getInstance().getInfo(id, lvl);

			if(!player.getVarB("isPlayerBuff"))
			{
				if(player.getFirstPet() != null)
				{
					Summon pet = player.getFirstPet();
					for(EffectTemplate et : skill.getEffectTemplates())
					{
						Env env = new Env(pet, pet, skill);
						Effect effect = et.getEffect(env);
						effect.setPeriod(time);
						pet.getEffectList().addEffect(effect);
					}
				}
			}
			else
			{
				for(EffectTemplate et : skill.getEffectTemplates())
				{
					Env env = new Env(player, player, skill);
					Effect effect = et.getEffect(env);
					effect.setPeriod(time);
					player.getEffectList().addEffect(effect);
				}
			}

			player.reduceAdena(pice);
			player.broadcastPacket(new MagicSkillUse(player, player, id, lvl, 1, 0));
			ShowHtml(page, player);
		}
		else if(bypass.startsWith("_bbsbaim"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			player.setVar("isPlayerBuff", player.getVarB("isPlayerBuff") ? "0" : "1", -1);

			ShowHtml(mBypass[1], player);
		}
		else if(bypass.startsWith("_bbsbregen"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			int pice = Config.COMMUNITYBOARD_BUFF_PICE;

			if(player.getAdena() < pice * 10)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Недостаточно сердств!");
				}
				else
				{
					player.sendMessage("It is not enough money!");
				}
				ShowHtml(mBypass[1], player);
				return;
			}

			if(!player.getVarB("isPlayerBuff"))
			{
				if(player.getFirstPet() != null)
				{
					Summon pet = player.getFirstPet();
					pet.setCurrentHpMp(pet.getMaxHp(), pet.getMaxMp());
					pet.setCurrentCp(pet.getMaxCp(), true);
				}
			}
			else
			{
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
				player.setCurrentCp(player.getMaxCp());
			}

			player.reduceAdena(pice * 10);
			ShowHtml(mBypass[1], player);
		}
		else if(bypass.startsWith("_bbsbcansel"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			if(player.getVarB("isPlayerBuff") && player.getEffectList().getEffectsBySkillId(Skill.SKILL_RAID_CURSE) == null)
			{
				player.getEffectList().stopAllEffects();
			}
			else if(!player.getVarB("isPlayerBuff") && player.getFirstPet() != null)
			{
				Summon pet = player.getFirstPet();
				pet.getEffectList().stopAllEffects();
			}

			ShowHtml(mBypass[1], player);
		}
		else if(bypass.startsWith("_bbsbsave"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			String name = "";
			try
			{
				if(mBypass[2].length() > 1)
				{
					name = mBypass[2].substring(1);
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Вы не ввели имя для сохранения!");
				}
				else
				{
					player.sendMessage("You did not enter a name to save!");
				}
				return;
			}

			SBufferScheme scheme = new SBufferScheme();

			if(ManageBbsBuffer.getCountOnePlayer(player.getObjectId()) >= 3)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Превышено максимально допустимое количество схем!");
				}
				else
				{
					player.sendMessage("Exceeded the number of schemes!");
				}
				ShowHtml(mBypass[1], player);
				return;
			}
			if(ManageBbsBuffer.existName(player.getObjectId(), name))
			{
				if(player.isLangRus())
				{
					player.sendMessage("Схема с таким названием уже существует!");
				}
				else
				{
					player.sendMessage("Scheme with that name already exists!");
				}
				ShowHtml(mBypass[1], player);
				return;
			}

			if(name.length() > 15)
			{
				name = name.substring(0, 15);
			}

			if(name.length() > 0)
			{
				scheme.obj_id = player.getObjectId();
				scheme.name = name;

				Effect skill[] = player.getEffectList().getAllFirstEffects();
				if(skill != null)
				{
					for(int i = 0; i < skill.length; i++)
					{
						if(Config.COMMUNITYBOARD_BUFF_ALLOW.contains(skill[i].getSkill().getId()))
						{
							scheme.skills_id.add(skill[i].getSkill().getId());
						}
					}
					if(scheme.skills_id != null)
					{
						CommunityBufferDAO.getInstance().insert(scheme);
					}
				}
			}

			ShowHtml(mBypass[1], player);
		}
		else if(bypass.startsWith("_bbsbdelete"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			CommunityBufferDAO.getInstance().delete(ManageBbsBuffer.getScheme(Integer.parseInt(mBypass[1]), player.getObjectId()));

			ShowHtml(mBypass[3], player);
		}
		else if(bypass.startsWith("_bbsbrestore"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			int pice = Config.COMMUNITYBOARD_BUFF_SAVE_PICE;

			if(player.getAdena() < pice)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Недостаточно сердств!");
				}
				else
				{
					player.sendMessage("It is not enough money!");
				}
				ShowHtml(mBypass[3], player);
				return;
			}

			SBufferScheme scheme = ManageBbsBuffer.getScheme(Integer.parseInt(mBypass[1]), player.getObjectId());
			GroupBuff(player, scheme.skills_id);
			player.reduceAdena(pice);
			ShowHtml(mBypass[3], player);
		}
	}

	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}

	private void ShowHtml(String name, Player player)
	{
		// Костыль
		if(player.getVar("isPlayerBuff") == null)
		{
			player.setVar("isPlayerBuff", "1", -1);
		}

		String html = HtmCache.getInstance().getNotNull("pages/buffer/" + name + ".htm", player);
		if(player.isLangRus())
		{
			html = html.replaceFirst("%aim%", player.getVarB("isPlayerBuff") ? "Персонаж" : "Питомец");
		}
		else
		{
			html = html.replaceFirst("%aim%", player.getVarB("isPlayerBuff") ? "Character" : "Pet");
		}
		html = html.replace("%pice%", GetStringCount(Config.COMMUNITYBOARD_BUFF_PICE));
		html = html.replace("%group_pice%", GetStringCount(Config.COMMUNITYBOARD_BUFF_SAVE_PICE));

		StringBuilder content = new StringBuilder("");
		content.append("<table width=120>");
		for(SBufferScheme sm : ManageBbsBuffer.getSchemePlayer(player.getObjectId()))
		{
			content.append("<tr>");
			content.append("<td>");
			content.append("<button value=\"" + sm.name + "\" action=\"bypass _bbsbrestore:" + sm.id + ":" + sm.name + ":" + name + ";\" width=105 height=20 back=\"L2UI_ct1.Button_DF_Down\" fore=\"L2UI_ct1.Button_DF\">");
			content.append("</td>");
			content.append("<td>");
			content.append("<button value=\"-\" action=\"bypass _bbsbdelete:" + sm.id + ":" + sm.name + ":" + name + ";\" width=20 height=20 back=\"L2UI_ct1.Button_DF_Down\" fore=\"L2UI_ct1.Button_DF\">");
			content.append("</td>");
			content.append("</tr>");
		}
		content.append("</table>");

		html = html.replace("%list_sheme%", content.toString());
		html = BbsUtil.htmlBuff(html, player);
		ShowBoard.separateAndSend(html, player);
	}

	private void GroupBuff(Player player, List<Integer> list)
	{

		int time = Config.COMMUNITYBOARD_BUFF_TIME;

		Skill skill;

		for(int i : list)
		{
			int lvl = SkillTable.getInstance().getBaseLevel(i);

			if(!Config.COMMUNITYBOARD_BUFF_ALLOW.contains(i))
			{
				continue;
			}

			skill = SkillTable.getInstance().getInfo(i, lvl);
			if(!player.getVarB("isPlayerBuff") && player.getFirstPet() != null)
			{
				for(EffectTemplate et : skill.getEffectTemplates())
				{
					Summon pet = player.getFirstPet();
					Env env = new Env(pet, pet, skill);
					Effect effect = et.getEffect(env);
					effect.setPeriod(time);
					pet.getEffectList().addEffect(effect);
				}
			}
			else
			{
				for(EffectTemplate et : skill.getEffectTemplates())
				{
					Env env = new Env(player, player, skill);
					Effect effect = et.getEffect(env);
					effect.setPeriod(time);
					player.getEffectList().addEffect(effect);
				}
			}
		}

	}

	private static boolean CheckCondition(Player player)
	{
		if(player == null)
		{
			return false;
		}

		if(!Config.USE_BBS_BUFER_IS_COMBAT && (player.getPvpFlag() != 0 || player.isInDuel() || player.isInCombat() || player.isAttackingNow()))
		{
			if(player.isLangRus())
			{
				player.sendMessage("Во время боя нельзя использовать данную функцию.");
			}
			else
			{
				player.sendMessage("During combat, you can not use this feature.");
			}
			return false;
		}

		if(player.isInOlympiadMode())
		{
			if(player.isLangRus())
			{
				player.sendMessage("Во время Олимпиады нельзя использовать данную функцию.");
			}
			else
			{
				player.sendMessage("During the Olympics you can not use this feature.");
			}
			return false;
		}

		if(player.getReflection().getId() != 0 && !Config.COMMUNITYBOARD_INSTANCE_ENABLED)
		{
			if(player.isLangRus())
			{
				player.sendMessage("Бафф доступен только в обычном мире.");
			}
			else
			{
				player.sendMessage("Buff is only available in the real world.");
			}
			return false;
		}

		if(!Config.COMMUNITYBOARD_BUFFER_ENABLED)
		{
			if(player.isLangRus())
			{
				player.sendMessage("Функция баффа отключена.");
			}
			else
			{
				player.sendMessage("Buff off function.");
			}
			return false;
		}

		if(!Config.COMMUNITYBOARD_EVENTS_ENABLED)
		{
			if(player.getTeam() != TeamType.NONE)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Нельзя использовать бафф во время эвентов.");
				}
				else
				{
					player.sendMessage("You can not use the buff during Events.");
				}
				return false;
			}
		}
		return true;
	}
}