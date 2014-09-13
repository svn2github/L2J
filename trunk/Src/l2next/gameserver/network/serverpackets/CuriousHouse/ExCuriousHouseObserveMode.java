package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseObserveMode extends L2GameServerPacket
{
	private final boolean _isInObserveMode;

	public ExCuriousHouseObserveMode(boolean isInObserveMode)
	{
		_isInObserveMode = isInObserveMode;
	}
	
	@Override
	protected void writeImpl()
	{
    writeC(_isInObserveMode ? 0 : 1);
	}

	@Override
	public String getType()
	{
		return "[S] FE:12C ExCuriousHouseObserveMode";
	}
}
