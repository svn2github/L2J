package l2next.gameserver.network.clientpackets;

import l2next.gameserver.Config;
import l2next.gameserver.handler.bbs.CommunityBoardHandler;
import l2next.gameserver.handler.bbs.ICommunityBoardHandler;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.SystemMsg;

public class RequestShowBoard extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _unknown;

	/**
	 * packet type id 0x5E
	 *
	 * sample
	 *
	 * 5E
	 * 01 00 00 00
	 *
	 * format:		cd
	 */
	@Override
	public void readImpl()
	{
		_unknown = readD();
	}

	@Override
	public void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		if(Config.COMMUNITYBOARD_ENABLED)
		{
			ICommunityBoardHandler handler = CommunityBoardHandler.getInstance().getCommunityHandler(Config.BBS_DEFAULT);
			if(handler != null)
			{
				handler.onBypassCommand(activeChar, Config.BBS_DEFAULT);
			}
		}
		else
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE));
		}
	}
}
