package l2next.gameserver.loginservercon.gspackets;

import l2next.gameserver.loginservercon.SendablePacket;

/**
 * @author ALF
 * @date 31.07.2012 Привязка \ снятие привязки IP
 */
public class RequestLocIp extends SendablePacket
{
	private String _login;
	private String _ip;
	private boolean _loc;

	public RequestLocIp(String login, String ip, boolean loc)
	{
		_login = login;
		_ip = ip;
		_loc = loc;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x12);
		writeS(_login);
		writeS(_ip);
		writeC(_loc ? 1 : 0);
	}

}
