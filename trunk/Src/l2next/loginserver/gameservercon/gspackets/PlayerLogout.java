package l2next.loginserver.gameservercon.gspackets;

import l2next.loginserver.gameservercon.GameServer;
import l2next.loginserver.gameservercon.ReceivablePacket;

public class PlayerLogout extends ReceivablePacket
{
	private String account;

	@Override
	protected void readImpl()
	{
		account = readS();
	}

	@Override
	protected void runImpl()
	{
		GameServer gs = getGameServer();
		if(gs.isAuthed())
		{
			gs.removeAccount(account);
		}
	}
}
