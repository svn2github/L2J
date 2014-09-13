package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.model.Player;

public class ExCuriousHouseMemberUpdate extends L2GameServerPacket
{

	private Player _player;

	public ExCuriousHouseMemberUpdate(Player player)
	{
		_player = player;
	}
	
	@Override
	protected void writeImpl()
	{
		writeD(_player.getObjectId());
		writeD(_player.getMaxHp());
		writeD(_player.getMaxCp());
		writeD((int)_player.getCurrentHp());
		writeD((int)_player.getCurrentCp());
	}
	
	@Override
	public String getType()
	{
		return "[S] FE:128 ExCuriousHouseMemberUpdate";
	}
}

