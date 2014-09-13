package l2next.gameserver.loginservercon.gspackets;

import l2next.gameserver.loginservercon.SendablePacket;

public class PlayerInGame extends SendablePacket
{
	private String account;

	public PlayerInGame(String account)
	{
		this.account = account;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x03);
		writeS(account);
	}
}
