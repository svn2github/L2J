package l2next.gameserver.network.serverpackets;

public class AutoAttackStart extends L2GameServerPacket
{
	// dh
	private int _targetId;

	public AutoAttackStart(int targetId)
	{
		_targetId = targetId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_targetId);
	}
}