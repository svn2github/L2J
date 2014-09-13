package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.ExInzoneWaitingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestInzoneWaitingTime extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestInzoneWaitingTime.class);

	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		player.sendPacket(new ExInzoneWaitingInfo(player));
		_log.info("[IMPLEMENT ME!] RequestInzoneWaitingTime (maybe trigger)");
	}
}
