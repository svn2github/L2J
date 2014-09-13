package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseLeave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLeaveCuriousHouse extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestLeaveCuriousHouse.class);

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
		player.sendPacket(new ExCuriousHouseLeave());
		if (getClient().getActiveChar() != null)
		//ChaosFestival.getInstance().exitChallenge(getClient().getActiveChar());
		_log.info("[IMPLEMENT ME!] RequestLeaveCuriousHouse (maybe trigger)");
	}
}
