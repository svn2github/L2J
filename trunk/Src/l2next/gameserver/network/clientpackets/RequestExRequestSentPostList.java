package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExShowSentPostList;

/**
 * Нажатие на кнопку "sent mail",запрос списка исходящих писем. В ответ шлется {@link ExShowSentPostList}
 */
public class RequestExRequestSentPostList extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// just a trigger
	}

	@Override
	protected void runImpl()
	{
		Player cha = getClient().getActiveChar();
		if(cha != null)
		{
			cha.sendPacket(new ExShowSentPostList(cha));
		}
	}
}