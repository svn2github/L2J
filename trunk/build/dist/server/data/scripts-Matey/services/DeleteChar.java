package services;

import l2next.gameserver.dao.CharacterDAO;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.Htm;
import l2next.gameserver.utils.Merchant;

/**
 * User: Samurai
 */
public class DeleteChar extends Functions
{
	public void deleteCharPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Быстрое удаление персонажа</font>");
		sb.append("<br>");

		sb.append("<font color=3293F3>С помощью данного сервиса вы можете мгновенно удалить персонажа</font>");
		sb.append(Htm.button("Удалить за 1 CoL", "_bbsscripts:services.DeleteChar:deleteChar", 150, 26));
		sb.append("<br>");

		show(sb.toString(), player);
	}

	public void deleteChar()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		Clan clan = player.getClan();
		if(clan != null)
		{
			player.sendMessage("Сначала выйдите из клана");
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			int objId = player.getObjectId();
			player.kick();
			CharacterDAO.getInstance().deleteCharByObjId(objId);
		}
	}
}