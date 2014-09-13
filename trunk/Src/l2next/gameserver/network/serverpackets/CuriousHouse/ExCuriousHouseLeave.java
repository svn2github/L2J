package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

//пропадает почти весь интерфейс и пооявляется кнопка отказ
//связан с пакетом RequestLeaveCuriousHouse
public class ExCuriousHouseLeave extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
	}

	@Override
	public String getType()
	{
		return "[S] FE:126 ExCuriousHouseLeave";
	}
}
