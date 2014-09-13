package l2next.gameserver.network.serverpackets;

/**
 * @author : Darvin
 */
public class ExResponseCommissionRegister extends L2GameServerPacket
{

	@Override
	protected void writeImpl()
	{

		writeD(0x01);
		writeD(0x00);
		writeQ(0x00);
		writeQ(0x00);
		writeD(-1);

	}

}
