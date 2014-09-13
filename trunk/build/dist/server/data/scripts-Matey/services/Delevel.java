package services;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.Experience;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.Htm;
import l2next.gameserver.utils.Merchant;

/**
 * User: Samurai
 */
public class Delevel extends Functions
{
	public void delevelPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Понижение уровня</font>");
		sb.append("<br>");

		sb.append("<font color=3293F3>С помощью данного сервиса вы можете понизить уровень</font>");
		sb.append(Htm.button("-1 Уровень за 1 CoL", "_bbsscripts:services.Delevel:delevel", 150, 26));
		sb.append("<br>");

		show(sb.toString(), player);
	}

	public void delevel()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			int level = player.getLevel() - 1;
			if(level < 1 || level > Experience.getMaxLevel())
			{
				player.sendMessage("You must specify level 1 - " + Experience.getMaxLevel());
				return;
			}

			Long exp_add = Experience.LEVEL[level] - player.getExp();
			player.addExpAndSp(exp_add, 0);
			player.sendChanges();
		}
	}
}
