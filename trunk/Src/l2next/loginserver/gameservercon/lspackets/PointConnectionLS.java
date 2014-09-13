package l2next.loginserver.gameservercon.lspackets;

import l2next.loginserver.accounts.Account;
import l2next.loginserver.accounts.SessionManager.Session;
import l2next.loginserver.gameservercon.SendablePacket;

public class PointConnectionLS extends SendablePacket
{
	private String login;
	private int point;

	public PointConnectionLS(Session session)
	{
		Account account = session.getAccount();
		this.login = account.getLogin();
		this.point = account.getPremiumPoint();
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x1d);
		writeS(login);
		writeD(point);
	}
}