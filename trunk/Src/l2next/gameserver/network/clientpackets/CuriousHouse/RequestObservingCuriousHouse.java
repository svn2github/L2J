package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.CuriousHouse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestObservingCuriousHouse extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestObservingCuriousHouse.class);

	@Override
	protected void readImpl()
	{
		readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}
		//_log.info("[IMPLEMENT ME!] RequestObservingCuriousHouse (maybe trigger)");
		if (player != null)// && (ChaosFestival.getInstance().isFightingNow(activeChar))) Добавить вторую часть проверки, 
		{
			player.enterObserverMode(player.getLoc());
			player.sendPacket(new ExCuriousHouseObserveMode(true));
		}
	}
}

