package l2next.gameserver.network.serverpackets;

public class PledgeShowMemberListDelete extends L2GameServerPacket
{
	private String _player;

	public PledgeShowMemberListDelete(String playerName)
	{
		_player = playerName;
	}

	@Override
	protected final void writeImpl()
	{
		writeS(_player);
	}
}