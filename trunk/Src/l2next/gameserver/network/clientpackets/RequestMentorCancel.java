package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.network.serverpackets.ExMentorList;
import l2next.gameserver.utils.MentorUtil;

public class RequestMentorCancel extends L2GameClientPacket
{
	private int _mtype;
	private String _charName;

	@Override
	protected void readImpl()
	{
		_mtype = readD(); // 00 приходит если ученик разрывает контракт с
		// наставником. 01 приходит, когда наставник разрывает
		// контракт с учеником.
		_charName = readS(); // Name
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		Player menteeChar = World.getPlayer(_charName);

		activeChar.getMentorSystem().remove(_charName, _mtype == 1, true);
		activeChar.sendPacket(new ExMentorList(activeChar));
		if(menteeChar != null && menteeChar.isOnline())
		{
			menteeChar.getMentorSystem().remove(activeChar.getName(), _mtype != 1, false);
			menteeChar.sendPacket(new ExMentorList(menteeChar));
		}
		MentorUtil.removeConditions(activeChar);
		MentorUtil.setTimePenalty(_mtype == 1 ? activeChar.getObjectId() : activeChar.getMentorSystem().getMentor(), System.currentTimeMillis() + 7 * 24 * 3600 * 1000L, -1);
	}
}
