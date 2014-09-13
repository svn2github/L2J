package l2next.gameserver.network.clientpackets;

import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.model.Player;
import l2next.gameserver.utils.Location;

public class CannotMoveAnymore extends L2GameClientPacket
{
	private Location _loc = new Location();

	/**
	 * packet type id 0x47
	 * <p/>
	 * sample
	 * <p/>
	 * 36 a8 4f 02 00 // x 17 85 01 00 // y a7 00 00 00 // z 98 90 00 00 // heading?
	 * <p/>
	 * format: cdddd
	 *
	 * @param decrypt
	 */
	@Override
	protected void readImpl()
	{
		_loc.x = readD();
		_loc.y = readD();
		_loc.z = readD();
		_loc.h = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		activeChar.getAI().notifyEvent(CtrlEvent.EVT_ARRIVED_BLOCKED, _loc, null);
	}
}