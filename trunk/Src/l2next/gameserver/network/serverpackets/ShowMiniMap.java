package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

public class ShowMiniMap extends L2GameServerPacket
{
	private int _mapId;

	public ShowMiniMap(Player player, int mapId)
	{
		_mapId = mapId;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_mapId);
		writeC(0x00);
	}
}