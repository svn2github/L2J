package l2next.gameserver.network.serverpackets.UnionPledge;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPledgeUnionState extends L2GameServerPacket
{
	private int _npc;
	private int _union;

	public ExPledgeUnionState(int npc, int union)
	{
		_npc = npc;
		_union = union;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_npc);
		writeD(_union);
	}
}