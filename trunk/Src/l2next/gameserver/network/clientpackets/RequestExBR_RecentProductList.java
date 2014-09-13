package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExBR_RecentProductListPacket;

public class RequestExBR_RecentProductList extends L2GameClientPacket
{
	@Override
	public void readImpl()
	{
		// триггер
	}

	@Override
	public void runImpl()
	{
		Player activeChar = getClient().getActiveChar();

		if(activeChar != null)
		{
			activeChar.sendPacket(new ExBR_RecentProductListPacket(activeChar));
		}
	}
}