package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.events.impl.KrateisCubeEvent;

/**
 * @author VISTALL
 */
public class RequestExStopShowCrataeCubeRank extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
		//
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		KrateisCubeEvent cubeEvent = player.getEvent(KrateisCubeEvent.class);
		if(cubeEvent == null)
		{
			return;
		}

		cubeEvent.closeRank(player);
	}
}