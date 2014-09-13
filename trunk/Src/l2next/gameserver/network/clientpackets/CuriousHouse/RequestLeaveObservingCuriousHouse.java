package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseObserveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLeaveObservingCuriousHouse extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestLeaveObservingCuriousHouse.class);

	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		
		if (activeChar != null)// && (activeChar.getObserverController().isObserving()))
		{
			activeChar.leaveObserverMode();
			activeChar.sendPacket(new ExCuriousHouseObserveMode(false));
		}
		_log.info("[IMPLEMENT ME!] RequestLeaveObservingCuriousHouse (maybe trigger)");
	}
}
