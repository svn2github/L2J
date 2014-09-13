package services.community;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.Config;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.handler.bbs.CommunityBoardHandler;
import l2next.gameserver.handler.bbs.ICommunityBoardHandler;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.TeamType;
import l2next.gameserver.network.serverpackets.ShowBoard;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.BbsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

public class ManageTeleport extends Functions implements ScriptFile, ICommunityBoardHandler
{

	static final Logger _log = LoggerFactory.getLogger(ManageTeleport.class);

	@Override
	public void onLoad()
	{
		if(Config.COMMUNITYBOARD_ENABLED && Config.COMMUNITYBOARD_TELEPORT_ENABLED)
		{
			_log.info("CommunityBoard: Teleport Community service loaded.");
			CommunityBoardHandler.getInstance().registerHandler(this);
		}
	}

	@Override
	public void onReload()
	{
		if(Config.COMMUNITYBOARD_ENABLED && Config.COMMUNITYBOARD_TELEPORT_ENABLED)
		{
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
			"_bbsteleport",
			"_bbsgotoxyz",
			"_bbstsave",
			"_bbstrestore",
			"_bbstdelete"
		};
	}

	@Override
	public void onBypassCommand(Player player, String bypass)
	{

		if(!CheckCondition(player))
		{
			return;
		}

		if(bypass.startsWith("_bbsteleport"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			ShowHtml(mBypass.length == 1 ? "index" : mBypass[1], player);
		}
		else if(bypass.startsWith("_bbsgotoxyz"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			int cX = Integer.parseInt(mBypass[2]);
			int cY = Integer.parseInt(mBypass[3]);
			int cZ = Integer.parseInt(mBypass[4]);
			int pice = Config.COMMUNITYBOARD_TELE_PICE;
			String page = mBypass[1];

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

			player.teleToLocation(cX, cY, cZ, 0);

			player.reduceAdena(pice);
			ShowHtml(page, player);
		}
		else if(bypass.startsWith("_bbstsave"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			String name = mBypass[2].substring(1);
			int pice = Config.COMMUNITYBOARD_SAVE_TELE_PICE;

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
				ShowHtml(mBypass[1], player);
				return;
			}

			if(getTeleCount(player) >= 7)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Превышено максимално допустимое количество точек возвращения!");
				}
				else
				{
					player.sendMessage("Exceeded the maximum number of return points!");
				}
				ShowHtml(mBypass[1], player);
				return;
			}
			if(!CheckTeleName(player, name))
			{
				if(player.isLangRus())
				{
					player.sendMessage("Точка с таким названием уже существует!");
				}
				else
				{
					player.sendMessage("The point with this name already exists!");
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
				Connection con = null;
				PreparedStatement stmt = null;
				try
				{

					con = DatabaseFactory.getInstance().getConnection();
					stmt = con.prepareStatement("INSERT INTO bbs_pointsave (charId,name,xPos,yPos,zPos) VALUES(?,?,?,?,?)");
					stmt.setInt(1, player.getObjectId());
					stmt.setString(2, name);
					stmt.setInt(3, player.getX());
					stmt.setInt(4, player.getY());
					stmt.setInt(5, player.getZ());
					stmt.execute();

				}
				catch(Exception e)
				{
				}
				finally
				{
					DbUtils.closeQuietly(con, stmt);
				}
			}
			player.reduceAdena(pice);
			ShowHtml(mBypass[1], player);
		}
		else if(bypass.startsWith("_bbstdelete"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");

			Connection con = null;
			PreparedStatement statement = null;

			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("DELETE FROM bbs_pointsave WHERE charId=? AND TpId=?;");
				statement.setInt(1, player.getObjectId());
				statement.setInt(2, Integer.parseInt(mBypass[2]));
				statement.execute();
			}
			catch(Exception e)
			{
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
			ShowHtml(mBypass[1], player);
		}
	}

	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}

	private static int getTeleCount(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		int count = 0;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT count(*) as cnt FROM bbs_pointsave WHERE `charId` = ?");
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();
			if(rset.next())
			{
				count = rset.getInt("cnt");
			}
		}
		catch(Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return count;
	}

	private static boolean CheckTeleName(Player player, String name)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT count(*) as cnt FROM bbs_pointsave WHERE `charId` = ? AND `name` = ?");
			statement.setInt(1, player.getObjectId());
			statement.setString(2, name);
			rset = statement.executeQuery();
			if(rset.next() && rset.getInt("cnt") == 0)
			{
				return true;
			}
		}
		catch(Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		return false;
	}

	private void ShowHtml(String name, Player player)
	{
		String html = HtmCache.getInstance().getNotNull("pages/teleport/" + name + ".htm", player);
		html = html.replace("%pice%", GetStringCount(Config.COMMUNITYBOARD_TELE_PICE));
		html = html.replace("%save_pice%", GetStringCount(Config.COMMUNITYBOARD_SAVE_TELE_PICE));
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM bbs_pointsave WHERE charId=?;");
			statement.setLong(1, player.getObjectId());
			rs = statement.executeQuery();
			StringBuilder content = new StringBuilder("");
			content.append("<table width=220>");
			while(rs.next())
			{
				content.append("<tr>");
				content.append("<td>");
				content.append("<button value=\"" + rs.getString("name") + "\" action=\"bypass _bbsgotoxyz:index:" + rs.getInt("xPos") + ":" + rs.getInt("yPos") + ":" + rs.getInt("zPos") + ";\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				content.append("</td>");
				content.append("<td>");
				content.append("<button value=\"Удалить\" action=\"bypass _bbstdelete:index:" + rs.getInt("TpId") + ";\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				content.append("</td>");
				content.append("</tr>");
			}
			content.append("</table>");

			html = html.replace("%list_teleport%", content.toString());
			ShowBoard.separateAndSend(BbsUtil.htmlBuff(html, player), player);
		}
		catch(Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}

	private static boolean CheckCondition(Player player)
	{
		if(player == null)
		{
			return false;
		}

		if(!Config.USE_BBS_TELEPORT_IS_COMBAT && (player.getPvpFlag() != 0 || player.isInDuel() || player.isInCombat() || player.isAttackingNow()))
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
			player.sendMessage("Телепорт доступен только в обычном мире.");
			return false;
		}

		if(!Config.COMMUNITYBOARD_BUFFER_ENABLED)
		{
			if(player.isLangRus())
			{
				player.sendMessage("Функция телепорта отключена.");
			}
			else
			{
				player.sendMessage("Teleport function is disabled.");
			}
			return false;
		}

		if(!Config.COMMUNITYBOARD_EVENTS_ENABLED)
		{
			if(player.getTeam() != TeamType.NONE)
			{
				if(player.isLangRus())
				{
					player.sendMessage("Нельзя использовать телепорт во время эвентов.");
				}
				else
				{
					player.sendMessage("You can not use Teleport during Events.");
				}
				return false;
			}
		}
		return true;
	}
}