package l2next.gameserver.network.serverpackets;

/**
 * @author VISTALL
 */
public class ExPVPMatchUserDie extends L2GameServerPacket
{
	private int _blueKills, _redKills;

	public ExPVPMatchUserDie()
	{

	}

	@Override
	protected final void writeImpl()
	{
		writeD(_blueKills);
		writeD(_redKills);
	}
}