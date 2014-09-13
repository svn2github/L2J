package l2next.gameserver.network.clientpackets.PledgeRecruit;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.PledgeRecruit.ExPledgeDraftListSearch;

public class RequestPledgeDraftListSearch extends L2GameClientPacket
{
	private int _minLevel;
	private int _maxLevel;
	private int _role;
	private String _charName;
	private int _sortType;
	private int _sortOrder;
	
	@Override
	protected void readImpl() throws Exception
	{
		_minLevel = readD();
		_maxLevel = readD();
		_role = readD();
		_charName = readS();
		_sortType = readD();
		_sortOrder = readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		
	}
}
