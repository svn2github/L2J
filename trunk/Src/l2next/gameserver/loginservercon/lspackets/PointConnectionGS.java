package l2next.gameserver.loginservercon.lspackets;

import l2next.gameserver.loginservercon.LoginServerCommunication;
import l2next.gameserver.loginservercon.ReceivablePacket;
import l2next.gameserver.network.GameClient;

public class PointConnectionGS extends ReceivablePacket
{

	private int point;
	private String acc;

	@Override
	protected void readImpl()
	{
		acc = readS();
		point = readD();
	}

	@Override
	protected void runImpl()
	{
		GameClient client = LoginServerCommunication.getInstance().getAuthedClient(acc);
		if(client != null)
		{
			client.setPremiumPoint(point);
		}
	}
}