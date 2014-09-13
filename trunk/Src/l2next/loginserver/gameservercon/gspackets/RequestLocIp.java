package l2next.loginserver.gameservercon.gspackets;

import l2next.loginserver.accounts.Account;
import l2next.loginserver.gameservercon.ReceivablePacket;

/**
 * @author ALF
 * @date 31.07.2012 Привязка \ снятие привязки IP
 */
public class RequestLocIp extends ReceivablePacket
{
	private String _login;
	private String _ip;
	private boolean _loc;

	@Override
	protected void readImpl()
	{
		_login = readS();
		_ip = readS();
		_loc = readC() == 1 ? true : false;
	}

	@Override
	protected void runImpl()
	{
		Account acc = new Account(_login);
		acc.restore();
		if(_loc)
		{
			acc.setAllowedIP(_ip);
		}
		else
		{
			acc.setAllowedIP("");
		}
		acc.update();
	}

}
