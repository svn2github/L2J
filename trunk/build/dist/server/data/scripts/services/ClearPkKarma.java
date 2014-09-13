package services;

import l2next.gameserver.model.Player;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.Htm;
import l2next.gameserver.utils.Merchant;

/**
 * User: Samurai
 */
public class ClearPkKarma extends Functions
{
	public void clearPkPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Обнулить счетчик Pk</font>");
		sb.append("<br>");

		sb.append("<font color=3293F3>С помощью данного сервиса вы можете обнулить счетчик Pk</font>");
		sb.append(Htm.button("Обнулить за 1 CoL", "_bbsscripts:services.ClearPkKarma:clearPk", 150, 26));
		sb.append("<br>");

		show(sb.toString(), player);
	}

	public void clearPk()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			player.setPkKills(0);
			player.broadcastCharInfo();
		}
	}

	public void clearKarmaPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Очищение кармы</font>");
		sb.append("<br>");

		sb.append("<font color=3293F3>С помощью данного сервиса вы можете очистить карму</font>");
		sb.append(Htm.button("Очистить за 1 CoL", "_bbsscripts:services.ClearPkKarma:clearKarma", 150, 26));
		sb.append("<br>");

		show(sb.toString(), player);
	}

	public void clearKarma()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			player.setKarma(0);
			player.broadcastCharInfo();
		}
	}
}