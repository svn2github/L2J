package services.community;

import l2next.gameserver.Config;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.data.xml.holder.BuyListHolder;
import l2next.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import l2next.gameserver.data.xml.holder.MultiSellHolder;
import l2next.gameserver.handler.bbs.CommunityBoardHandler;
import l2next.gameserver.handler.bbs.ICommunityBoardHandler;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExBuySellList;
import l2next.gameserver.network.serverpackets.ShowBoard;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.scripts.Scripts;
import l2next.gameserver.utils.BbsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringTokenizer;

public class CommunityBoard implements ScriptFile, ICommunityBoardHandler
{
	private static final Logger _log = LoggerFactory.getLogger(CommunityBoard.class);

	@Override
	public void onLoad()
	{
		if(Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: service loaded.");
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

	@Override
	public String[] getBypassCommands()
	{
		return new String[]{
			"_bbshome",
			"_bbslink",
			"_bbsmultisell",
			"_bbssell",
			"_bbspage",
			"_bbsscripts"
		};
	}

	@Override
	public void onBypassCommand(Player player, String bypass)
	{
		if(!Config.ALLOW_COMMUNITYBOARD_IN_COMBAT && player.isInCombat())
		{
			player.sendMessage(new CustomMessage("scripts.services.community.CommunityBoard.NotUse", player));
			return;
		}

		StringTokenizer st = new StringTokenizer(bypass, "_");
		String cmd = st.nextToken();
		String html = "";
		if("bbshome".equals(cmd))
		{
			StringTokenizer p = new StringTokenizer(Config.BBS_DEFAULT, "_");
			String dafault = p.nextToken();
			if(dafault.equals(cmd))
			{
                if (Config.BBSHOME_PAGE_WESTEROS)
                {
                    html = HtmCache.getInstance().getNotNull("pages/stats/stats.htm", player);
                }
                else
                {
                    html = HtmCache.getInstance().getNotNull("pages/main.htm", player);
                }
				html = BbsUtil.htmlAll(html, player);
			}
			else
			{
				onBypassCommand(player, Config.BBS_DEFAULT);
				return;
			}
		}
		else if("bbslink".equals(cmd))
		{
			html = HtmCache.getInstance().getNotNull("bbs_homepage.htm", player);
			html = BbsUtil.htmlAll(html, player);
		}
		else if(bypass.startsWith("_bbspage"))
		{
			//Example: "bypass _bbspage:index".
			String[] b = bypass.split(":");
			String page = b[1];
			html = HtmCache.getInstance().getNotNull("pages/" + page + ".htm", player);
			html = BbsUtil.htmlAll(html, player);
		}
		else if(bypass.startsWith("_bbsmultisell"))
		{
			if(!CheckCondition(player))
			{
				return;
			}
			//Example: "_bbsmultisell:10000;_bbspage:index" or "_bbsmultisell:10000;_bbshome" or "_bbsmultisell:10000"...
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
			if(pBypass != null)
			{
				ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
				if(handler != null)
				{
					handler.onBypassCommand(player, pBypass);
				}
			}

			int listId = Integer.parseInt(mBypass[1]);
			MultiSellHolder.getInstance().SeparateAndSend(listId, player, 0);
			return;
		}
		else if(bypass.startsWith("_bbssell"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			//	String[] mBypass = st2.nextToken().split(":");
			String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
			if(pBypass != null)
			{
				ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
				if(handler != null)
				{
					handler.onBypassCommand(player, pBypass);
				}
			}
			player.setIsBBSUse(true);
			NpcTradeList list = BuyListHolder.getInstance().getBuyList(-1);
			player.sendPacket(new ExBuySellList.BuyList(list, player, 0.), new ExBuySellList.SellRefundList(player, false));
			player.sendChanges();
			return;
		}
		else if(bypass.startsWith("_bbsscripts"))
		{
			//Example: "_bbsscripts:events.GvG.GvG:addGroup;_bbspage:index" or "_bbsscripts:events.GvG.GvG:addGroup;_bbshome" or "_bbsscripts:events.GvG.GvG:addGroup"...
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String sBypass = st2.nextToken().substring(12);
			String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
			if(pBypass != null)
			{
				ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(pBypass);
				if(handler != null)
				{
					handler.onBypassCommand(player, pBypass);
				}
			}

			String[] word = sBypass.split("\\s+");
			String[] args = sBypass.substring(word[0].length()).trim().split("\\s+");
			String[] path = word[0].split(":");
			if(path.length != 2)
			{
				return;
			}

			Scripts.getInstance().callScripts(player, path[0], path[1], word.length == 1 ? new Object[]{} : new Object[]{args});
			return;
		}

		ShowBoard.separateAndSend(html, player);
	}

	private boolean CheckCondition(Player player)
	{
		if(!Config.ALLOW_COMMUNITYBOARD_IN_COMBAT && (player.getPvpFlag() != 0 || player.isInDuel() || player.isInCombat() || player.isAttackingNow()))
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

		return true;
	}

	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}
}
