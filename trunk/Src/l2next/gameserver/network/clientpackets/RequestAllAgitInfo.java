package l2next.gameserver.network.clientpackets;

import l2next.gameserver.network.serverpackets.ExShowAgitInfo;

public class RequestAllAgitInfo extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		getClient().getActiveChar().sendPacket(new ExShowAgitInfo());
	}
}