package services;

import l2next.commons.net.PremiumIp;
import l2next.gameserver.instancemanager.PremiumIpManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.Htm;
import l2next.gameserver.utils.Merchant;

import java.util.Calendar;

/**
 * User: Samurai
 */
public class PremiumWindow extends Functions
{
	public void premiumWindowPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Дополнительные окна</font>");
		sb.append("<br>");
		sb.append("<font color=3293F3>С помощью данного сервиса вы можете купить возможность играть в несколько окон, на 1 месяц</font>");
		sb.append("<br>");
		sb.append(Htm.button("Купить 1 окно за 1 CoL", "_bbsscripts:services.PremiumWindow:premiumWindow", 150, 26));
		sb.append("<br>");
		sb.append("<font color=LEVEL>Купленные окна:</font> ").append(PremiumIpManager.getInstance().getCountPremiumIps(player.getIP()));
		sb.append("<br>");
		sb.append("<font color=LEVEL>Даты окончания:</font>");
		sb.append("<br>");
		for(PremiumIp premiumIp : PremiumIpManager.getInstance().getPremiumIpsFromIp(player.getIP()))
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(premiumIp.getTime() * 1000);
			sb.append(calendar.getTime().toString());
			sb.append("<br>");
		}
		show(sb.toString(), player);
	}

	public void premiumWindow()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			PremiumIpManager.getInstance().addIp(player.getIP(), System.currentTimeMillis() / 1000 + 24 * 60 * 60);
			premiumWindowPage();
		}
	}
}
