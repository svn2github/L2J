package l2next.gameserver.network.clientpackets;

import l2next.gameserver.instancemanager.WorldStatisticsManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.worldstatistics.CharacterStatisticElement;
import l2next.gameserver.network.serverpackets.ExLoadStatUser;

import java.util.List;

/**
 * @author ALF
 * @modified KilRoy
 * @data 08.08.2012
 */
public class RequestUserStatistics extends L2GameClientPacket
{

	@Override
	protected void readImpl() throws Exception
	{
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player player = getClient().getActiveChar();

		if(player == null)
		{
			return;
		}

		List<CharacterStatisticElement> stat = WorldStatisticsManager.getInstance().getCurrentStatisticsForPlayer(player.getObjectId());
		player.sendPacket(new ExLoadStatUser(stat));
	}
}