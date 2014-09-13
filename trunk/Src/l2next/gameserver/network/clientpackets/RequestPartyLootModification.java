package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.party.Party;

public class RequestPartyLootModification extends L2GameClientPacket
{
	private byte _mode;

	@Override
	protected void readImpl()
	{
		_mode = (byte) readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		if(_mode < 0 || _mode > Party.ITEM_ORDER_SPOIL)
		{
			return;
		}

		Party party = activeChar.getParty();
		if(party == null || _mode == party.getLootDistribution() || party.getPartyLeader() != activeChar)
		{
			return;
		}

		party.requestLootChange(_mode);
	}
}
