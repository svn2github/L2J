package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseObserveList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestObservingListCuriousHouse extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestObservingListCuriousHouse.class);
	
	private int _houseID = 0;
	 
	@Override
	protected void readImpl()
	{
		_houseID = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}
		player.sendPacket(new ExCuriousHouseObserveList());
		_log.info("[IMPLEMENT ME!] RequestObservingListCuriousHouse (maybe trigger)");
	}
}