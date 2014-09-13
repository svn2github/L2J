package l2next.gameserver.utils;

import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.model.Player;

public class BbsUtil
{
	public static String htmlAll(String htm, Player player)
	{
		String html_all = HtmCache.getInstance().getNotNull("block/allpages.htm", player);
		String html_menu = HtmCache.getInstance().getNotNull("block/menu.htm", player);
		String html_copy = HtmCache.getInstance().getNotNull("block/copyright.htm", player);
		html_all = html_all.replace("%main_menu%", html_menu);
		html_all = html_all.replace("%body_page%", htm);
		html_all = html_all.replace("%copyright%", html_copy);
		html_all = html_all.replace("%copyrightsym%", "©");
		return html_all;
	}

	public static String htmlBuff(String htm, Player player)
	{
		String html_option = HtmCache.getInstance().getNotNull("pages/buffer/block/option.htm", player);
		htm = htm.replace("%main_optons%", html_option);
		htm = htmlAll(htm, player);
		return htm;
	}
}