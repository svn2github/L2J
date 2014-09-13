package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExGetBookMarkInfo;

public class RequestBookMarkSlotInfo extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// just trigger
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		activeChar.sendPacket(new ExGetBookMarkInfo(activeChar));
	}
}