package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCuriousHouseRecord extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestCuriousHouseRecord.class);

	@Override
	protected void readImpl()
	{

	}

	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}
		//player.sendPacket(new ExCuriousHouseResult());
		_log.info("[IMPLEMENT ME!] RequestCuriousHouseRecord (maybe trigger)");
	}
}

