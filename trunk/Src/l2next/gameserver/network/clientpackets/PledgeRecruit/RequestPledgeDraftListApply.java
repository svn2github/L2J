package l2next.gameserver.network.clientpackets.PledgeRecruit;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestPledgeDraftListApply extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
		readD();
		readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
	}
}
