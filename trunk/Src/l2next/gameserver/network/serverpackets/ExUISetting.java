package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

public class ExUISetting extends L2GameServerPacket
{
	private final byte data[];

	public ExUISetting(Player player)
	{
		data = player.getKeyBindings();
	}

	@Override
	protected void writeImpl()
	{
		writeD(data.length);
		writeB(data);
	}
}
