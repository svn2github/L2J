package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.model.pledge.RankPrivs;
import l2next.gameserver.network.serverpackets.PledgePowerGradeList;

public class RequestPledgePowerGradeList extends L2GameClientPacket
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
		Clan clan = activeChar.getClan();
		if(clan != null)
		{
			RankPrivs[] privs = clan.getAllRankPrivs();
			activeChar.sendPacket(new PledgePowerGradeList(privs));
		}
	}
}