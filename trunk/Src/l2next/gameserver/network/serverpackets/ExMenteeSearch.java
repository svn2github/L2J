package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Andrey A. Date: 11.11.12 Time: 23:50
 */
public class ExMenteeSearch extends L2GameServerPacket
{
	List<Player> mentees;
	int page, playersInPage;

	public ExMenteeSearch(Player activeChar, int _page, int minLevel, int maxLevel)
	{
		mentees = new ArrayList<Player>();
		//mentees = new ArrayList<>();
		page = _page;
		playersInPage = 64;
		for(Player player : World.getAroundPlayers(activeChar))
		{
			if(player.getLevel() >= minLevel && player.getLevel() <= maxLevel && player.isMentee())
			{
				mentees.add(player);
			}
		}
	}

	@Override
	protected void writeImpl()
	{
		writeD(page);
		if(!mentees.isEmpty())
		{
			writeD(mentees.size());
			writeD(mentees.size() % playersInPage);
			int i = 1;
			for(Player player : mentees)
			{
				if(i <= playersInPage * page && i > playersInPage * (page - 1))
				{
					writeS(player.getName());
					writeD(player.getClassId().getId());
					writeD(player.getLevel());
				}
			}
		}
		else
		{
			writeD(0x00);
			writeD(0x00);
		}
	}

}
