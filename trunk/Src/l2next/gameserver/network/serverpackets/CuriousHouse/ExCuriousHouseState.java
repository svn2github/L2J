package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseState extends L2GameServerPacket
{
	private ChaosFestivalInviteState _state;

	public ExCuriousHouseState(ChaosFestivalInviteState state)
	{
		_state = state;
	}
	
	public static enum ChaosFestivalInviteState
	{
		IDLE, 
		INVITE, 
		PREPARE;
	}

	@Override
	protected void writeImpl()
	{
		
		writeD(_state.ordinal());
	}

	@Override
	public String getType()
	{
		return "[S] FE:125 ExCuriousHouseState";
	}
}
