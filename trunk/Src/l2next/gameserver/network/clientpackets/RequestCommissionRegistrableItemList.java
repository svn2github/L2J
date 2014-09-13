package l2next.gameserver.network.clientpackets;

import l2next.gameserver.instancemanager.commission.CommissionShopManager;
import l2next.gameserver.model.Player;

/**
 * @author : Darvin
 *         <p/>
 *         Приходит при нажатия вкладки "Регистрация", запращивает список вещей, которые можно положить в коммиссионный магазин Отправляет
 *         {@link l2next.gameserver.network.serverpackets.ExResponseCommissionItemList}
 */
public class RequestCommissionRegistrableItemList extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
		// Do nothing
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}
		CommissionShopManager.getInstance().showRegistrableItems(player);
	}
}
