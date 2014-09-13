package services;

import l2next.gameserver.dao.AccountDAO;
import l2next.gameserver.dao.CharacterDAO;
import l2next.gameserver.model.Player;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.Htm;
import l2next.gameserver.utils.Merchant;

/**
 * User: Samurai
 */
public class CharToAcc extends Functions
{
	public void charToAccPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Перенос персонажа на другой аккаунт</font>");
		sb.append("<br>");
		sb.append("<font color=3293F3>С помощью данного сервиса вы можете мгновенно перенести персонажа на другой аккаунт</font>");
		sb.append("<br>");
		sb.append("Аккаунт куда хотите перенести персонажа:");
		sb.append("<edit var=\"acc\" width=150>");
		sb.append("<br>");
		sb.append(Htm.button("Перенести за 1 CoL", "_bbsscripts:services.CharToAcc:charToAcc $acc", 150, 26));
		sb.append("<br>");
		show(sb.toString(), player);
	}

	public void charToAcc(String[] args)
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		if(!AccountDAO.getInstance().check(args[0]))
		{
			player.sendMessage("Такой аккаунт не существует");
			return;
		}
		if(CharacterDAO.getInstance().accountCharNumber(args[0]) >= 8)
		{
			player.sendMessage("Выбранный аккаунт переполнен");
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			int objId = player.getObjectId();
			player.kick();
			CharacterDAO.getInstance().setAccount(args[0], objId);
		}
	}
}