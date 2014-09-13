package l2next.gameserver.network.serverpackets;

public class PledgeReceiveUpdatePower extends L2GameServerPacket
{
	private int _privs;

	public PledgeReceiveUpdatePower(int privs)
	{
		_privs = privs;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_privs); // Filler??????
	}
}