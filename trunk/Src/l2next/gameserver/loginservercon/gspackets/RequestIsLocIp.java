package l2next.gameserver.loginservercon.gspackets;

import l2next.gameserver.loginservercon.SendablePacket;

/**
 * @author ALF
 * @date 31.07.2012
 */
public class RequestIsLocIp extends SendablePacket
{
	private int _objId;
	private String _login;

	public RequestIsLocIp(int objId, String login)
	{
		_objId = objId;
		_login = login;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x13);
		writeD(_objId);
		writeS(_login);
	}

}
