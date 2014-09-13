package l2next.gameserver.network.serverpackets;

/**
 * User: Samurai
 */
public class ExSetPledgeEmblemAck extends L2GameServerPacket
{
	private int _part;

	public ExSetPledgeEmblemAck(int part)
	{
		_part = part;
	}

	protected void writeImpl()
	{
		writeD(_part);
	}
}
