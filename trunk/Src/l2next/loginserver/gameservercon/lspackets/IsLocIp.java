package l2next.loginserver.gameservercon.lspackets;

import l2next.loginserver.gameservercon.SendablePacket;

/**
 * @author ALF
 * @date 31.07.2012
 */
public class IsLocIp extends SendablePacket
{
	private int _objId;
	private String _ip;

	public IsLocIp(int objId, String ip)
	{
		_objId = objId;
		_ip = ip;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x05);
		writeD(_objId);
		writeS(_ip);
	}

}
