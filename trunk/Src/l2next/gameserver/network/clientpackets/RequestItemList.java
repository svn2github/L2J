package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.StatusUpdate;

public class RequestItemList extends L2GameClientPacket
{
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

		if(!activeChar.getPlayerAccess().UseInventory || activeChar.isBlocked())
		{
			activeChar.sendActionFailed();
			return;
		}

		activeChar.sendItemList(true);
		activeChar.sendStatusUpdate(false, false, StatusUpdate.CUR_LOAD);
	}
}