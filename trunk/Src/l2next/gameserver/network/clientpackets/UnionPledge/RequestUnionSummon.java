package l2next.gameserver.network.clientpackets.UnionPledge;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestUnionSummon extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// this is just a trigger packet. it has no content
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
