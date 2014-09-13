package l2next.gameserver.loginservercon.gspackets;

import l2next.gameserver.loginservercon.SendablePacket;

public class PointConnectionGS extends SendablePacket
{
	private String acc;
	private int point;

	public PointConnectionGS(String acc, int point)
	{
		this.acc = acc;
		this.point = point;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0x1d);
		writeS(acc);
		writeD(point);
	}
}