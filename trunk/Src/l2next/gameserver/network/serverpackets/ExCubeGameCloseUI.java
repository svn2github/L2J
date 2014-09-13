package l2next.gameserver.network.serverpackets;

/**
 * Format: (chd)
 */
public class ExCubeGameCloseUI extends L2GameServerPacket
{
	int _seconds;

	public ExCubeGameCloseUI()
	{
	}

	@Override
	protected void writeImpl()
	{
		writeD(0xffffffff);
	}
}