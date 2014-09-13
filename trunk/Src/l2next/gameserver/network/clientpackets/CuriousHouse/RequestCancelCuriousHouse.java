package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCancelCuriousHouse extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestCancelCuriousHouse.class);

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
		if ((getClient()).getActiveChar() != null)
			//ChaosFestival.getInstance().exitChallenge(((L2GameClient)getClient()).getActiveChar());
		_log.info("[IMPLEMENT ME!] RequestCancelCuriousHouse (maybe trigger)");
	}
}