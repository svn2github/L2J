package l2next.gameserver.network.clientpackets;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.party.Party;

public class RequestHandOverPartyMaster extends L2GameClientPacket
{
	private String _name;

	@Override
	protected void readImpl()
	{
		_name = readS(16);
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		Party party = activeChar.getParty();

		if(party == null || !activeChar.getParty().isLeader(activeChar))
		{
			activeChar.sendActionFailed();
			return;
		}

		Player member = party.getPlayerByName(_name);

		if(member == activeChar)
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_TRANSFER_RIGHTS_TO_YOURSELF);
			return;
		}

		if(member == null)
		{
			activeChar.sendPacket(Msg.YOU_CAN_TRANSFER_RIGHTS_ONLY_TO_ANOTHER_PARTY_MEMBER);
			return;
		}

		activeChar.getParty().changePartyLeader(member);
	}
}