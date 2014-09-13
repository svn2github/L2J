/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2next.gameserver.network.serverpackets.PledgeRecruit;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeRecruitInfo extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeS("");
		writeS("");
		writeD(0);
		writeD(0);
		writeD(0);
	}
}
