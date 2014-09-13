package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExBR_ProductList;

public class RequestExBR_ProductList extends L2GameClientPacket
{
	private int unkown = 0;

	@Override
	protected void readImpl()
	{
		unkown = readD();
	}

	@Override
	protected void runImpl()
	{
		if(unkown != 0)
		{
			return;
		}

		Player activeChar = getClient().getActiveChar();

		if(activeChar == null)
		{
			return;
		}

		activeChar.sendPacket(new ExBR_ProductList());
	}
}