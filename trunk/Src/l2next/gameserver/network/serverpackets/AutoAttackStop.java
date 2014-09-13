package l2next.gameserver.network.serverpackets;

public class AutoAttackStop extends L2GameServerPacket
{
	// dh
	private int _targetId;

	/**
	 * @param _characters
	 */
	public AutoAttackStop(int targetId)
	{
		_targetId = targetId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_targetId);
	}
}