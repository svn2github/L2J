package l2next.gameserver.network.clientpackets;

import l2next.gameserver.instancemanager.commission.CommissionShopManager;
import l2next.gameserver.model.Player;

/**
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатии вкладки "Регистрация", запрашивает список вещей, находящихся в коммиссионном магазине Отправляет
 *         {@link l2next.gameserver.network.serverpackets.ExResponseCommissionList}
 */
public class RequestCommissionRegisteredItem extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
		// Trigger
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}
		CommissionShopManager.getInstance().showPlayerRegisteredItems(player);
	}
}
