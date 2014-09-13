package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.entity.events.impl.DuelEvent;

public class ExDuelReady extends L2GameServerPacket
{
	private int _duelType;

	public ExDuelReady(DuelEvent event)
	{
		_duelType = event.getDuelType();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_duelType);
	}
}