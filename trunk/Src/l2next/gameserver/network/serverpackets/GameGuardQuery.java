package l2next.gameserver.network.serverpackets;

public class GameGuardQuery extends L2GameServerPacket
{
	@Override
	protected final void writeImpl()
	{
		writeD(0x00); // ? - Меняется при каждом перезаходе.
		writeD(0x00); // ? - Меняется при каждом перезаходе.
		writeD(0x00); // ? - Меняется при каждом перезаходе.
		writeD(0x00); // ? - Меняется при каждом перезаходе.
	}
}