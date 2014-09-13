package l2next.gameserver.network.serverpackets;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 17.06.12 Time: 1:32
 */
public class AbnormalVisualEffectPacket extends L2GameServerPacket
{

	@Override
	protected final void writeImpl()
	{

		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}

}
