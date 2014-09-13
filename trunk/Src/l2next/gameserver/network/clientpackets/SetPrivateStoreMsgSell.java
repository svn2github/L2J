package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;

public class SetPrivateStoreMsgSell extends L2GameClientPacket
{
	private String _storename;

	@Override
	protected void readImpl()
	{
		_storename = readS(32);
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		activeChar.setSellStoreName(_storename);
	}
}