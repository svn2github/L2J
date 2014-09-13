package l2next.gameserver.network.clientpackets;

import l2next.commons.dao.JdbcEntityState;
import l2next.gameserver.dao.CharacterDAO;
import l2next.gameserver.data.xml.holder.ProductHolder;
import l2next.gameserver.database.mysql;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.primeshop.PrimeItem;
import l2next.gameserver.model.items.primeshop.PrimeProduct;
import l2next.gameserver.model.mail.Mail;
import l2next.gameserver.network.serverpackets.ExBR_GamePoint;
import l2next.gameserver.network.serverpackets.ExBR_PresentBuyProductPacket;
import l2next.gameserver.network.serverpackets.ExNoticePostArrived;
import l2next.gameserver.network.serverpackets.ExReplyWritePost;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.utils.ItemFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Mifesto
 * Date: 12:39/18.01.13
 * Team: http://team.ankou.ru
 */
public class RequestBR_PresentBuyProduct extends L2GameClientPacket
{
	private int _productId;
	private int _count;
	private String _receiverName;
	private String _topic;
	private String _message;

	protected void readImpl() throws Exception
	{
		_productId = readD();
		_count = readD();
		_receiverName = readS();
		_topic = readS();
		_message = readS();
	}

	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		if(_count > 99 || _count < 0)
		{
			return;
		}

		PrimeProduct product = ProductHolder.getInstance().getProduct(_productId);
		if(product == null)
		{
			activeChar.sendPacket(ExBR_PresentBuyProductPacket.RESULT_WRONG_PRODUCT);
			return;
		}
		long currentType = System.currentTimeMillis() / 1000L;
		if(currentType < product.getStartTimeSale() || currentType > product.getEndTimeSale())
		{
			activeChar.sendPacket(ExBR_PresentBuyProductPacket.RESULT_SALE_PERIOD_ENDED);
			return;
		}

		final int pointsRequired = product.getPoints() * _count;
		if(pointsRequired < 0)
		{
			activeChar.sendPacket(ExBR_PresentBuyProductPacket.RESULT_WRONG_PRODUCT);
			return;
		}

		final long pointsCount = activeChar.getPremiumPoints();
		if(pointsRequired > pointsCount)
		{
			activeChar.sendPacket(ExBR_PresentBuyProductPacket.RESULT_NOT_ENOUGH_POINTS);
			return;
		}

		Player receiver = World.getPlayer(_receiverName);
		int recieverId;
		if(receiver != null)
		{
			recieverId = receiver.getObjectId();
			_receiverName = receiver.getName();
			if(receiver.getBlockList().contains(activeChar.getName()))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(_receiverName));
				return;
			}
		}
		else
		{
			recieverId = CharacterDAO.getInstance().getObjectIdByName(_receiverName);
			if(recieverId > 0)
			{
				if(mysql.simple_get_int("target_Id", "character_blocklist", "obj_Id=" + recieverId + " AND target_Id=" + activeChar.getObjectId()) > 0)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(_receiverName));
					return;
				}
			}
		}

		if(recieverId == 0)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.WHEN_THE_RECIPIENT_DOESN_T_EXIST_OR_THE_CHARACTER_HAS_BEEN_DELETED_SENDING_MAIL_IS_NOT_POSSIBLE));
			return;
		}

		activeChar.reducePremiumPoints(pointsRequired);

		List<ItemInstance> attachments = new ArrayList<ItemInstance>();
		for(PrimeItem comp : product.getComponents())
		{
			ItemInstance item = ItemFunctions.createItem(comp.getItemId());
			item.setCount(comp.getCount() * _count);
			item.setOwnerId(activeChar.getObjectId());
			item.setLocation(ItemInstance.ItemLocation.MAIL);
			if(item.getJdbcState().isSavable())
			{
				item.save();
			}
			else
			{
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();
			}
			attachments.add(item);
		}

		Mail mail = new Mail();
		mail.setSenderId(activeChar.getObjectId());
		mail.setSenderName(activeChar.getName());
		mail.setReceiverId(recieverId);
		mail.setReceiverName(_receiverName);
		mail.setTopic(_topic);
		mail.setBody(_message);
		mail.setPrice(0L);
		mail.setUnread(true);
		mail.setType(Mail.SenderType.PRESENT);
		mail.setExpireTime(1296000 + (int) (System.currentTimeMillis() / 1000L)); //15 суток дается.
		for(ItemInstance item : attachments)
		{
			mail.addAttachment(item);
		}
		mail.save();

		activeChar.sendPacket(ExReplyWritePost.STATIC_TRUE);
		activeChar.sendPacket(new ExBR_GamePoint(activeChar));
		activeChar.sendPacket(ExBR_PresentBuyProductPacket.RESULT_OK);
		activeChar.sendChanges();

		if(receiver != null)
		{
			receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
			receiver.sendPacket(new SystemMessage(SystemMessage.THE_MAIL_HAS_ARRIVED));
		}
	}
}
