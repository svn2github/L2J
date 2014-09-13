package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.SSQStatus;

/**
 * Seven Signs Record Update Request packet type id 0xc8 format: cc
 */
public class RequestSSQStatus extends L2GameClientPacket
{
	private int _page;

	@Override
	protected void readImpl()
	{
		_page = readC();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		activeChar.sendPacket(new SSQStatus(activeChar, _page));
	}
}