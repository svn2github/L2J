/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2next.gameserver.network.clientpackets.PledgeRecruit;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestPledgeJoinSys extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
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