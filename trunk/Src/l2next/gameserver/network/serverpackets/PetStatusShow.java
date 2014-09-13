package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Summon;

public class PetStatusShow extends L2GameServerPacket
{
	private int _summonType;
	private int _summonId;

	public PetStatusShow(Summon summon)
	{
		_summonType = summon.getSummonType();
		_summonId = summon.getObjectId();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_summonType);
		writeD(_summonId);// L2WT GOD
	}
}