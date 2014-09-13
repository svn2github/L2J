package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.matching.MatchingRoom;

/**
 * @author VISTALL
 */
public class RequestExWithdrawMpccRoom extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		//
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		MatchingRoom room = player.getMatchingRoom();
		if(room == null || room.getType() != MatchingRoom.CC_MATCHING)
		{
			return;
		}

		if(room.getLeader() == player)
		{
			return;
		}

		room.removeMember(player, false);
	}
}