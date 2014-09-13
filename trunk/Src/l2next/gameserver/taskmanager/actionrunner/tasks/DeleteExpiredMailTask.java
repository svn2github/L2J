package l2next.gameserver.taskmanager.actionrunner.tasks;

import l2next.commons.dao.JdbcEntityState;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.dao.MailDAO;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.model.mail.Mail;
import l2next.gameserver.network.serverpackets.ExNoticePostArrived;

import java.util.List;

/**
 * @author G1ta0
 */
public class DeleteExpiredMailTask extends AutomaticTask
{
	public DeleteExpiredMailTask()
	{
		super();
	}

	@Override
	public void doTask() throws Exception
	{
		int expireTime = (int) (System.currentTimeMillis() / 1000L);

		List<Mail> mails = MailDAO.getInstance().getExpiredMail(expireTime);

		for(Mail mail : mails)
		{
			if(!mail.getAttachments().isEmpty())
			{
				if(mail.getType() == Mail.SenderType.NORMAL)
				{
					Player player = World.getPlayer(mail.getSenderId());

					Mail reject = mail.reject();
					mail.delete();
					reject.setExpireTime(expireTime + 360 * 3600);
					reject.save();

					if(player != null)
					{
						player.sendPacket(ExNoticePostArrived.STATIC_TRUE);
						player.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
					}
				}
				else
				{
					// TODO [G1ta0] возврат вещей в инвентарь игрока
					mail.setExpireTime(expireTime + 86400);
					mail.setJdbcState(JdbcEntityState.UPDATED);
					mail.update();
				}
			}
			else
			{
				mail.delete();
			}
		}
	}

	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + 600000L;
	}
}
