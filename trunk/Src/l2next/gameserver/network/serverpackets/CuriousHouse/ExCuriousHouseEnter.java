package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

//пир отправке этого пакета на экране появляется иконка получения письма
public class ExCuriousHouseEnter extends L2GameServerPacket
{
	public void ExCuriousHouseEnter()
	{
	}

	@Override
	protected void writeImpl()
	{
	}

	@Override
	public String getType()
	{
		return "[S] FE:125 ExCuriousHouseEnter";
	}
}
