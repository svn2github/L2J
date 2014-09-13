package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.pledge.SubUnit;

public class PledgeReceiveSubPledgeCreated extends L2GameServerPacket
{
	private int type;
	private String _name, leader_name;

	public PledgeReceiveSubPledgeCreated(SubUnit subPledge)
	{
		type = subPledge.getType();
		_name = subPledge.getName();
		leader_name = subPledge.getLeaderName();
	}

	@Override
	protected final void writeImpl()
	{

		writeD(0x01);
		writeD(type);
		writeS(_name);
		writeS(leader_name);
	}
}