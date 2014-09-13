package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseRemainTime extends L2GameServerPacket
{
	private int _time;

	public ExCuriousHouseRemainTime(int time)
	{
		_time = time;
	}
	
	@Override
	protected void writeImpl()
	{
		writeD(_time);
	}

	@Override
	public String getType()
	{
		return "[S] FE:129 ExCuriousHouseRemainTime";
	}
}
