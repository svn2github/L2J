package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.model.Player;
import java.util.List;

public class ExCuriousHouseMemberList extends L2GameServerPacket
{
	private final List<Player> _players;

	public ExCuriousHouseMemberList(List<Player> players)
	{
		_players = players;
	}

	@Override
	protected void writeImpl()
	{

		writeD(1);
		writeD(2);//Кол-во народа в матче
		writeD(_players.size());
		int number = 0;
		
		for (Player player : _players)
		{
			writeD(player.getObjectId());
			writeD(++number);
			writeD(player.getMaxHp());
			writeD(player.getMaxCp());
			writeD((int)player.getCurrentHp());
			writeD((int)player.getCurrentCp());
		}
	}

	@Override
	public String getType()
	{
		return "[S] FE:127 ExCuriousHouseMemberList";
	}
}
