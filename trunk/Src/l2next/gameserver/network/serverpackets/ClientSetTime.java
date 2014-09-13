package l2next.gameserver.network.serverpackets;

import l2next.gameserver.GameTimeController;

public class ClientSetTime extends L2GameServerPacket
{
	public static final L2GameServerPacket STATIC = new ClientSetTime();

	@Override
	protected final void writeImpl()
	{
		writeD(GameTimeController.getInstance().getGameTime()); // time in
		// client
		// minutes
		writeD(6); // constant to match the server time( this determines the
		// speed of the client clock)
	}
}