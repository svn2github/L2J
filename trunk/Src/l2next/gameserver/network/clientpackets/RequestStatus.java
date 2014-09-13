package l2next.gameserver.network.clientpackets;

import l2next.gameserver.network.serverpackets.SendStatus;

public final class RequestStatus extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		getClient().close(new SendStatus());
	}
}