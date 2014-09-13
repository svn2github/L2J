package l2next.gameserver.loginservercon.gspackets;

import l2next.gameserver.loginservercon.SendablePacket;

public class PingResponse extends SendablePacket
{
	@Override
	protected void writeImpl()
	{
		writeC(0xff);
		writeQ(System.currentTimeMillis());
	}
}