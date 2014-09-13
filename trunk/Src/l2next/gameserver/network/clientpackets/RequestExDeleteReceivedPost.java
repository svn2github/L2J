package l2next.gameserver.network.clientpackets;

import l2next.gameserver.dao.MailDAO;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.mail.Mail;
import l2next.gameserver.network.serverpackets.ExShowReceivedPostList;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Запрос на удаление полученных сообщений. Удалить можно только письмо без вложения. Отсылается при нажатии на "delete" в списке полученных писем.
 *
 * @see ExShowReceivedPostList
 * @see RequestExDeleteSentPost
 */
public class RequestExDeleteReceivedPost extends L2GameClientPacket
{
	private int _count;
	private int[] _list;

	/**
	 * format: dx[d]
	 */
	@Override
	protected void readImpl()
	{
		_count = readD();
		if(_count * 4 > _buf.remaining() || _count > Short.MAX_VALUE || _count < 1)
		{
			_count = 0;
			return;
		}
		_list = new int[_count]; // количество элементов для удаления
		for(int i = 0; i < _count; i++)
		{
			_list[i] = readD(); // уникальный номер письма
		}
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null || _count == 0)
		{
			return;
		}

		List<Mail> mails = MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId());
		if(!mails.isEmpty())
		{
			for(Mail mail : mails)
			{
				if(ArrayUtils.contains(_list, mail.getMessageId()))
				{
					if(mail.getAttachments().isEmpty())
					{
						MailDAO.getInstance().deleteReceivedMailByMailId(activeChar.getObjectId(), mail.getMessageId());
					}
				}
			}
		}

		activeChar.sendPacket(new ExShowReceivedPostList(activeChar));
	}
}