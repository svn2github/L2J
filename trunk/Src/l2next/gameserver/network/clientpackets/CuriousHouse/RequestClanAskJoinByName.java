package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestClanAskJoinByName extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestClanAskJoinByName.class);

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
		_log.info("[IMPLEMENT ME!] RequestClanAskJoinByName (maybe trigger)");
	}
}
