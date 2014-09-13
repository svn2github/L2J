package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;

public class RequestChangeAttributeCancel extends L2GameClientPacket
{

	@Override
	public void readImpl()
	{
	}

	@Override
	public void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		activeChar.sendActionFailed();

	}

}