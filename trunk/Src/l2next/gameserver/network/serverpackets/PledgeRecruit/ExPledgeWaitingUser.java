/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2next.gameserver.network.serverpackets.PledgeRecruit;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeWaitingUser extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeD(0);
		writeS("");
	}
}