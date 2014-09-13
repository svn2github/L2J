package services;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.Config;
import l2next.gameserver.dao.CharacterDAO;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.Race;
import l2next.gameserver.model.entity.events.impl.SiegeEvent;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.model.pledge.SubUnit;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.tables.ClanTable;
import l2next.gameserver.utils.Log;
import l2next.gameserver.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Rename extends Functions
{
	public void rename_page()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		String append = "!Rename";
		append += "<br>";
		append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Rename.RenameFor", getSelf()).addString(Util.formatAdena(Config.SERVICES_CHANGE_NICK_PRICE)).addItemName(Config.SERVICES_CHANGE_NICK_ITEM) + "</font>";
		append += "<table>";
		append += "<tr><td>" + new CustomMessage("scripts.services.Rename.NewName", getSelf()) + " <edit var=\"new_name\" width=80></td></tr>";
		append += "<tr><td></td></tr>";
		append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.Rename.RenameButton", getSelf()) + "\" action=\"bypass -h scripts_services.Rename:rename $new_name\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
		append += "</table>";
		show(append, player);
	}

	public void changesex_page()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		if(!player.isInPeaceZone())
		{
			show("You must be in peace zone to use this service.", player);
			return;
		}

		String append = "Sex changing";
		append += "<br>";
		append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.SexChange.SexChangeFor", player).addString(Util.formatAdena(Config.SERVICES_CHANGE_SEX_PRICE)).addItemName(Config.SERVICES_CHANGE_SEX_ITEM) + "</font>";
		append += "<table>";
		append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.SexChange.Button", player) + "\" action=\"bypass -h scripts_services.Rename:changesex\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
		append += "</table>";
		show(append, player);
	}

	public void rename(String[] args)
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		if(player.isHero())
		{
			sendMessage("Not available for heroes.", player);
			return;
		}

		if(args.length != 1)
		{
			show(new CustomMessage("scripts.services.Rename.incorrectinput", player), player);
			return;
		}

		if(player.getEvent(SiegeEvent.class) != null)
		{
			show(new CustomMessage("scripts.services.Rename.SiegeNow", player), player);
			return;
		}

		String name = args[0];
		if(!Util.isMatchingRegexp(name, Config.CNAME_TEMPLATE))
		{
			show(new CustomMessage("scripts.services.Rename.incorrectinput", player), player);
			return;
		}

		if(getItemCount(player, Config.SERVICES_CHANGE_NICK_ITEM) < Config.SERVICES_CHANGE_NICK_PRICE)
		{
			if(Config.SERVICES_CHANGE_NICK_ITEM == 57)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			return;
		}

		if(CharacterDAO.getInstance().getObjectIdByName(name) > 0)
		{
			show(new CustomMessage("scripts.services.Rename.Thisnamealreadyexists", player), player);
			return;
		}

		removeItem(player, Config.SERVICES_CHANGE_NICK_ITEM, Config.SERVICES_CHANGE_NICK_PRICE);

		String oldName = player.getName();
		player.reName(name, true);

		Log.rename("Character " + oldName + " renamed to " + name);

		show(new CustomMessage("scripts.services.Rename.changedname", player).addString(oldName).addString(name), player);
	}

	public void changesex()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		if(player.getRace() == Race.kamael)
		{
			show("Not available for Kamael.", player);
			return;
		}

		if(!player.isInPeaceZone())
		{
			show("You must be in peace zone to use this service.", player);
			return;
		}

		if(getItemCount(player, Config.SERVICES_CHANGE_SEX_ITEM) < Config.SERVICES_CHANGE_SEX_PRICE)
		{
			if(Config.SERVICES_CHANGE_SEX_ITEM == 57)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			return;
		}

		Connection con = null;
		PreparedStatement offline = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("UPDATE characters SET sex = ? WHERE obj_Id = ?");
			offline.setInt(1, player.getSex() == 1 ? 0 : 1);
			offline.setInt(2, player.getObjectId());
			offline.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			show(new CustomMessage("common.Error", player), player);
			return;
		}
		finally
		{
			DbUtils.closeQuietly(con, offline);
		}

		player.setHairColor(0);
		player.setHairStyle(0);
		player.setFace(0);
		removeItem(player, Config.SERVICES_CHANGE_SEX_ITEM, Config.SERVICES_CHANGE_SEX_PRICE);
		player.logout();
		Log.add("Character " + player + " sex changed to " + (player.getSex() == 1 ? "male" : "female"), "renames");
	}

	public void rename_clan_page()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		if(player.getClan() == null || !player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addName(player));
			return;
		}

		String append = "!Rename clan";
		append += "<br>";
		append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Rename.RenameFor", getSelf()).addString(Util.formatAdena(Config.SERVICES_CHANGE_CLAN_NAME_PRICE)).addItemName(Config.SERVICES_CHANGE_CLAN_NAME_ITEM) + "</font>";
		append += "<table>";
		append += "<tr><td>" + new CustomMessage("scripts.services.Rename.NewName", getSelf()) + ": <edit var=\"new_name\" width=80></td></tr>";
		append += "<tr><td></td></tr>";
		append += "<tr><td><button value=\"" + new CustomMessage("scripts.services.Rename.RenameButton", getSelf()) + "\" action=\"bypass -h scripts_services.Rename:rename_clan $new_name\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
		append += "</table>";
		show(append, player);
	}

	public void rename_clan(String[] param)
	{
		Player player = getSelf();
		if(player == null || param == null || param.length == 0)
		{
			return;
		}

		if(player.getClan() == null || !player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addName(player));
			return;
		}

		if(player.getEvent(SiegeEvent.class) != null)
		{
			show(new CustomMessage("scripts.services.Rename.SiegeNow", player), player);
			return;
		}

		if(!Util.isMatchingRegexp(param[0], Config.CLAN_NAME_TEMPLATE))
		{
			player.sendPacket(SystemMsg.CLAN_NAME_IS_INVALID);
			return;
		}
		if(ClanTable.getInstance().getClanByName(param[0]) != null)
		{
			player.sendPacket(SystemMsg.THIS_NAME_ALREADY_EXISTS);
			return;
		}

		if(getItemCount(player, Config.SERVICES_CHANGE_CLAN_NAME_ITEM) < Config.SERVICES_CHANGE_CLAN_NAME_PRICE)
		{
			if(Config.SERVICES_CHANGE_CLAN_NAME_ITEM == 57)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			return;
		}

		show(new CustomMessage("scripts.services.Rename.changedname", player).addString(player.getClan().getName()).addString(param[0]), player);
		SubUnit sub = player.getClan().getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
		sub.setName(param[0], true);

		removeItem(player, Config.SERVICES_CHANGE_CLAN_NAME_ITEM, Config.SERVICES_CHANGE_CLAN_NAME_PRICE);
		player.getClan().broadcastClanStatus(true, true, false);
	}
}