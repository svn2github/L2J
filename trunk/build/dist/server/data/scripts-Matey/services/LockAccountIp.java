package services;

import l2next.gameserver.dao.AccountDAO;
import l2next.gameserver.model.Player;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.Htm;

/**
 * User: Samurai
 */
public class LockAccountIp extends Functions
{
	public void lockAccountIpPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Настройка безопасности</font>");
		sb.append("<br>");
		sb.append("<font color=00ff66>Предупреждение для предосторожности:</font>");
		sb.append("<br>");
		sb.append("- IP периодически изменяется");
		sb.append("<br>");
		sb.append("<font color=00ff66>Аккаунт привязан к IP адресу:</font> <font color=LEVEL>").append(AccountDAO.getInstance().getAllowIp(player.getAccountName())).append("</font>");
		sb.append("<br>");
		sb.append("<font color=00ff66>Текущий IP адрес:</font> <font color=LEVEL>").append(player.getIP()).append("</font>");
		sb.append("<br>");
		sb.append(Htm.button("Установить блокировку по IP", "_bbsscripts:services.LockAccountIp:lockIp", 175, 26));
		sb.append(Htm.button("Удалить блокировку по IP", "_bbsscripts:services.LockAccountIp:unlockIp", 175, 26));
		sb.append("<br>");
		show(sb.toString(), player);
	}

	public void lockIp()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		AccountDAO.getInstance().setAllowIp(player.getAccountName(), player.getIP());
		lockAccountIpPage();
	}

	public void unlockIp()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		AccountDAO.getInstance().setAllowIp(player.getAccountName(), "");
		lockAccountIpPage();
	}
}