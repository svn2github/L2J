package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExMentorList;

/**
 * Приходит при нажатии кнопки Friends в клиенте. Не имеет структуры, ответом на этот запрос является пакет
 * {@link l2next.gameserver.network.serverpackets.ExMentorList}
 */
public class RequestMentorList extends L2GameClientPacket
{

	@Override
	protected void runImpl()
	{
		// triggger
	}

	@Override
	protected void readImpl()
	{
		Player activeChar = getClient().getActiveChar();
		sendPacket(new ExMentorList(activeChar));
	}
}
