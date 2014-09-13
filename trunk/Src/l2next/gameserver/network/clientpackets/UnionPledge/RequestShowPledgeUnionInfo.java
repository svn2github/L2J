package l2next.gameserver.network.clientpackets.UnionPledge;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;

/**
*
*@Author Awakeninger
*@Этот пакет шлет клиент при нажатии на кнопку в меню кланового заказа в окне клана
*/

public class RequestShowPledgeUnionInfo extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player cha = getClient().getActiveChar();

		if(cha != null)
		{
			//TODO:
		}
	}
}
