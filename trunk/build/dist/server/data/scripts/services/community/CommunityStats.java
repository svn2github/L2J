package services.community;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.Config;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.handler.bbs.CommunityBoardHandler;
import l2next.gameserver.handler.bbs.ICommunityBoardHandler;
import l2next.gameserver.instancemanager.CastleManorManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ShowBoard;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.BbsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityStats implements ScriptFile, ICommunityBoardHandler
{
	private static final Logger _log = LoggerFactory.getLogger(CommunityStats.class);
	public long lUpdateTime = 0;

	/**
	 * Имплементированые методы скриптов
	 */
	@Override
	public void onLoad()
	{
		if(Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: Stats service loaded.");
			CommunityBoardHandler.getInstance().registerHandler(this);
		}
	}

	@Override
	public void onReload()
	{
		if(Config.COMMUNITYBOARD_ENABLED)
		{
			CommunityBoardHandler.getInstance().removeHandler(this);
		}
	}

	@Override
	public void onShutdown()
	{
	}

	/**
	 * Регистратор команд
	 */
	@Override
	public String[] getBypassCommands()
	{
		return new String[]{
			"_bbsstat;pvp",
			"_bbsstat;pk",
			"_bbsstat;online",
			"_bbsstat;clan",
			"_bbsstat;castle"
		};
	}

	private static Map<Integer, CBStatPkPvp> CBStatPvpId = new HashMap<Integer, CBStatPkPvp>();
	private static Map<Integer, CBStatPkPvp> CBStatPkId = new HashMap<Integer, CBStatPkPvp>();
	private static Map<Integer, CBStatPkPvp> CBStatOnId = new HashMap<Integer, CBStatPkPvp>();

	public static class CBStatPkPvp
	{
		public String ChName;
		public int ChSex;    //	0 - male;	1 - female
		public int ChGameTime;
		public int ChPk;
		public int ChPvP;
		public int ChOnOff;    // 1 - in game
	}

	private static Map<Integer, CBStatClan> CBStatClanId = new HashMap<Integer, CBStatClan>();

	public static class CBStatClan
	{
		public String ClanName;
		public String AllyName;
		public int ReputationClan;
		public int ClanLevel;
		public int hasCastle;
	}

	private static Map<Integer, CBStatCastle> CBStatCastleId = new HashMap<Integer, CBStatCastle>();

	public static class CBStatCastle
	{
		public String NameCastl;
		public int Percent;
		public Long SiegeDate;
	}

	/**
	 * Класс общих пер-х
	 */
	public class CBStatMan
	{
		public int PlayerId = 0; // obj_id Char
		public String ChName = ""; // Char name
		public int ChGameTime = 0; // Time in game
		public int ChPk = 0; // Char PK
		public int ChPvP = 0; // Char PVP
		public int ChOnOff = 0; // Char offline/online cure time
		public int ChSex = 0; // Char sex
		public String NameCastl;
		public Object siegeDate;
		public String Percent;
		public Object id2;
		public int id;
		public int ClanLevel;
		public int hasCastle;
		public int ReputationClan;
		public String AllyName;
		public String ClanName;
		public String Owner;
	}

	/**
	 * Обработчик команд класса
	 * @param Player - плеер (Call'er)
	 * @param command - команда обработки
	 */
	@Override
	public void onBypassCommand(Player player, String command)
	{
		if(lUpdateTime + Config.BBS_STAT_UPDATE_TIME * 60 < System.currentTimeMillis() / 1000)
		{
			loadAllPvp();
			loadAllPk();
			loadAllCastle();
			loadAllClan();
			loadAllOnline();
			lUpdateTime = System.currentTimeMillis() / 1000;
		}
		if(command.startsWith("_bbsstat;pk"))
		{
			showPk(player);
		}
		else if(command.startsWith("_bbsstat;pvp"))
		{
			showPvp(player);
		}
		else if(command.startsWith("_bbsstat;clan"))
		{
			showClan(player);
		}
		else if(command.startsWith("_bbsstat;castle"))
		{
			showCastle(player);
		}
		else if(command.startsWith("_bbsstat;online"))
		{
			showOnline(player);
		}
		else if(player.isLangRus())
		{
			ShowBoard.separateAndSend("<html><body><br><br><center>На данный момент функция: " + command + " пока не реализована</center><br><br></body></html>", player);
		}
		else
		{
			ShowBoard.separateAndSend("<html><body><br><br><center>At the moment the function: " + command + " not implemented yet</center><br><br></body></html>", player);
		}
	}

	private void loadAllPvp()
	{
		CBStatPvpId.clear();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pvpkills DESC LIMIT 10;");
			rs = statement.executeQuery();

			while(rs.next())
			{
				CBStatPkPvp tp = new CBStatPkPvp();
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");

				int _index = CBStatPvpId.keySet().size() + 1;
				CBStatPvpId.put(_index, tp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}

	private void loadAllOnline()
	{
		CBStatOnId.clear();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY onlinetime DESC LIMIT 10;");
			rs = statement.executeQuery();

			while(rs.next())
			{
				CBStatPkPvp tp = new CBStatPkPvp();
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");

				int _index = CBStatOnId.keySet().size() + 1;
				CBStatOnId.put(_index, tp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}

	private void loadAllPk()
	{
		CBStatPkId.clear();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pkkills DESC LIMIT 10;");
			rs = statement.executeQuery();

			while(rs.next())
			{
				CBStatPkPvp tp = new CBStatPkPvp();
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");

				int _index = CBStatPkId.keySet().size() + 1;
				CBStatPkId.put(_index, tp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}

	private void loadAllCastle()
	{
		CBStatCastleId.clear();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM castle ORDER BY id DESC LIMIT 10;");
			rs = statement.executeQuery();

			while(rs.next())
			{
				CBStatCastle tp = new CBStatCastle();
				tp.NameCastl = rs.getString("name");
				tp.Percent = rs.getInt("tax_percent");
				tp.SiegeDate = rs.getLong("siege_date");

				CBStatCastleId.put(rs.getInt("id"), tp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}

	private void loadAllClan()
	{
		CBStatClanId.clear();

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT clan_subpledges.name,clan_data.clan_level,clan_data.reputation_score,clan_data.hasCastle,ally_data.ally_name FROM clan_data LEFT JOIN ally_data ON clan_data.ally_id = ally_data.ally_id LEFT JOIN `clan_subpledges` ON clan_data.clan_id = clan_subpledges.clan_id WHERE clan_data.clan_level>0 AND clan_subpledges.leader_id != '' order by clan_data.clan_level desc limit 10;");
			rs = statement.executeQuery();

			while(rs.next())
			{
				CBStatClan tp = new CBStatClan();
				tp.ClanName = rs.getString("name");
				tp.AllyName = rs.getString("ally_name");
				tp.ReputationClan = rs.getInt("reputation_score");
				tp.ClanLevel = rs.getInt("clan_level");
				tp.hasCastle = rs.getInt("hasCastle");

				int _index = CBStatClanId.keySet().size() + 1;
				CBStatClanId.put(_index, tp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}

	private void showPvp(Player player)
	{
		StringBuilder html = new StringBuilder();
		html.append("<table width=570>");

		for(int i : CBStatPvpId.keySet())
		{
			String sex = CBStatPvpId.get(i).ChSex == 1 ? "Ж" : "М";
			String color;
			String OnOff;
			if(CBStatPvpId.get(i).ChOnOff == 1)
			{
				OnOff = "В игре.";
				color = "00CC00";
			}
			else
			{
				OnOff = "Оффлайн.";
				color = "D70000";
			}
			html.append("<tr>");
			html.append("<td width=250>" + CBStatPvpId.get(i).ChName + "</td>");
			html.append("<td width=50>" + sex + "</td>");
			html.append("<td width=100>" + OnlineTime(CBStatPvpId.get(i).ChGameTime) + "</td>");
			html.append("<td width=50>" + CBStatPvpId.get(i).ChPk + "</td>");
			html.append("<td width=50><font color=00CC00>" + CBStatPvpId.get(i).ChPvP + "</font></td>");
			html.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
			html.append("</tr>");
		}
		html.append("</table>");

		String content = HtmCache.getInstance().getNotNull("pages/stats/stats_top_pvp.htm", player);
		content = content.replace("%stats_top_pvp%", html.toString());
		content = BbsUtil.htmlBuff(content, player);
		ShowBoard.separateAndSend(content, player);
	}

	private void showPk(Player player)
	{
		StringBuilder html = new StringBuilder();
		html.append("<table width=570>");
		for(int i : CBStatPkId.keySet())
		{
			String sex = CBStatPkId.get(i).ChSex == 1 ? "Ж" : "М";
			String color;
			String OnOff;
			if(CBStatPkId.get(i).ChOnOff == 1)
			{
				OnOff = "В игре.";
				color = "00CC00";
			}
			else
			{
				OnOff = "Оффлайн.";
				color = "D70000";
			}
			html.append("<tr>");
			html.append("<td width=250>" + CBStatPkId.get(i).ChName + "</td>");
			html.append("<td width=50>" + sex + "</td>");
			html.append("<td width=100>" + OnlineTime(CBStatPkId.get(i).ChGameTime) + "</td>");
			html.append("<td width=50><font color=00CC00>" + CBStatPkId.get(i).ChPk + "</font></td>");
			html.append("<td width=50>" + CBStatPkId.get(i).ChPvP + "</td>");
			html.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
			html.append("</tr>");
		}
		html.append("</table>");

		String content = HtmCache.getInstance().getNotNull("pages/stats/stats_top_pk.htm", player);
		content = content.replace("%stats_top_pk%", html.toString());
		content = BbsUtil.htmlBuff(content, player);
		ShowBoard.separateAndSend(content, player);
	}

	private void showClan(Player player)
	{
		StringBuilder html = new StringBuilder();
		html.append("<table width=570>");

		for(int i : CBStatClanId.keySet())
		{
			String hasCastle = "";
			String castleColor = "D70000";

			switch(CBStatClanId.get(i).hasCastle)
			{
				case 1:
					hasCastle = "Gludio";
					castleColor = "00CC00";
					break;
				case 2:
					hasCastle = "Dion";
					castleColor = "00CC00";
					break;
				case 3:
					hasCastle = "Giran";
					castleColor = "00CC00";
					break;
				case 4:
					hasCastle = "Oren";
					castleColor = "00CC00";
					break;
				case 5:
					hasCastle = "Aden";
					castleColor = "00CC00";
					break;
				case 6:
					hasCastle = "Innadril";
					castleColor = "00CC00";
					break;
				case 7:
					hasCastle = "Goddard";
					castleColor = "00CC00";
					break;
				case 8:
					hasCastle = "Rune";
					castleColor = "00CC00";
					break;
				case 9:
					hasCastle = "Schuttgart";
					castleColor = "00CC00";
					break;
				default:
					hasCastle = "Нету";
					castleColor = "D70000";
					break;
			}
			html.append("<tr>");
			html.append("<td width=150>" + CBStatClanId.get(i).ClanName + "</td>");
			if(CBStatClanId.get(i).AllyName != null)
			{
				html.append("<td width=150>" + CBStatClanId.get(i).AllyName + "</td>");
			}
			else
			{
				html.append("<td width=150>Нет альянса</td>");
			}
			html.append("<td width=100>" + CBStatClanId.get(i).ReputationClan + "</td>");
			html.append("<td width=50>" + CBStatClanId.get(i).ClanLevel + "</td>");
			html.append("<td width=100><font color=" + castleColor + ">" + hasCastle + "</font></td>");
			html.append("</tr>");
		}
		html.append("</table>");

		String content = HtmCache.getInstance().getNotNull("pages/stats/stats_clan.htm", player);
		content = content.replace("%stats_clan%", html.toString());
		content = BbsUtil.htmlBuff(content, player);
		ShowBoard.separateAndSend(content, player);
	}

	private void showCastle(Player player)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		StringBuilder html = new StringBuilder();
		html.append("<table width=570>");
		String Owner = null;
		String color = "FFFFFF";
		for(int i : CBStatCastleId.keySet())
		{
			Owner = CastleManorManager.getInstance().getOwner(i);

			if(Owner != null)
			{
				color = "00CC00";
			}
			else
			{
				color = "FFFFFF";
				Owner = "Нет владельца";
			}
			html.append("<tr>");
			html.append("<td width=150>" + CBStatCastleId.get(i).NameCastl + "</td>");
			html.append("<td width=100>" + CBStatCastleId.get(i).Percent + "%" + "</td>");
			html.append("<td width=200><font color=" + color + ">" + Owner + "</font></td>");
			html.append("<td width=150>" + sdf.format(new Date(CBStatCastleId.get(i).SiegeDate)) + "</td>");
			html.append("</tr>");
		}
		html.append("</table>");

		String content = HtmCache.getInstance().getNotNull("pages/stats/stats_castle.htm", player);
		content = content.replace("%stats_castle%", html.toString());
		content = BbsUtil.htmlBuff(content, player);
		ShowBoard.separateAndSend(content, player);
	}

	private void showOnline(Player player)
	{
		StringBuilder html = new StringBuilder();
		html.append("<table width=570>");
		for(int i : CBStatOnId.keySet())
		{
			String sex = CBStatOnId.get(i).ChSex == 1 ? "Ж" : "М";
			String color;
			String OnOff;
			if(CBStatOnId.get(i).ChOnOff == 1)
			{
				OnOff = "В игре.";
				color = "00CC00";
			}
			else
			{
				OnOff = "Оффлайн.";
				color = "D70000";
			}
			html.append("<tr>");
			html.append("<td width=250>" + CBStatOnId.get(i).ChName + "</td>");
			html.append("<td width=50>" + sex + "</td>");
			html.append("<td width=100><font color=00CC00>" + OnlineTime(CBStatOnId.get(i).ChGameTime) + "</font></td>");
			html.append("<td width=50>" + CBStatOnId.get(i).ChPk + "</td>");
			html.append("<td width=50>" + CBStatOnId.get(i).ChPvP + "</td>");
			html.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
			html.append("</tr>");
		}
		html.append("</table>");

		String content = HtmCache.getInstance().getNotNull("pages/stats/stats_online.htm", player);
		content = content.replace("%stats_online%", html.toString());
		content = BbsUtil.htmlBuff(content, player);
		ShowBoard.separateAndSend(content, player);
	}

	String OnlineTime(int time)
	{
		long onlinetimeH;
		int onlinetimeM;
		if(time / 60 / 60 - 0.5 <= 0)
		{
			onlinetimeH = 0;
		}
		else
		{
			onlinetimeH = Math.round(time / 60 / 60 - 0.5);
		}
		onlinetimeM = Math.round((time / 60 / 60 - onlinetimeH) * 60);
		return "" + onlinetimeH + " ч. " + onlinetimeM + " м.";
	}

	/**
	 * Не используемый, но вызываемый метод имплемента
	 */
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}
}