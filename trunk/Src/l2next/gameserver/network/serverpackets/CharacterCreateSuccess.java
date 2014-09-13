package l2next.gameserver.network.serverpackets;

public class CharacterCreateSuccess extends L2GameServerPacket
{
	public static final L2GameServerPacket STATIC = new CharacterCreateSuccess();

	@Override
	protected final void writeImpl()
	{
		writeD(0x01);
	}
}