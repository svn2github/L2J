package l2next.gameserver.network.serverpackets.UnionPledge;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExUnionPoint extends L2GameServerPacket
{
	private int _point;

	public ExUnionPoint(int point)
	{
		_point = point;
	}
	
	@Override
	protected void writeImpl()
	{
		writeD(_point);
	}
}