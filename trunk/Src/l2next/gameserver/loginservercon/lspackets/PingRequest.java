package l2next.gameserver.loginservercon.lspackets;

import l2next.gameserver.loginservercon.LoginServerCommunication;
import l2next.gameserver.loginservercon.ReceivablePacket;
import l2next.gameserver.loginservercon.gspackets.PingResponse;

public class PingRequest extends ReceivablePacket
{
	@Override
	public void readImpl()
	{

	}

	@Override
	protected void runImpl()
	{
		LoginServerCommunication.getInstance().sendPacket(new PingResponse());
	}
}