package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExBR_GamePoint;

public class RequestExBR_GamePoint extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();

		if(activeChar == null)
		{
			return;
		}

		activeChar.sendPacket(new ExBR_GamePoint(activeChar));
	}
}