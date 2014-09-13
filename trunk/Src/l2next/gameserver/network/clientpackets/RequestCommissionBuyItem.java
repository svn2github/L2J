package l2next.gameserver.network.clientpackets;

import l2next.gameserver.instancemanager.commission.CommissionShopManager;
import l2next.gameserver.model.Player;

/**
 * @author : Darvin
 */
public class RequestCommissionBuyItem extends L2GameClientPacket
{
	private long auctionId;
	private int exItemType;

	@Override
	protected void readImpl() throws Exception
	{
		auctionId = readQ();
		exItemType = readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}
		CommissionShopManager.getInstance().requestBuyItem(player, auctionId, exItemType);
	}
}
