package l2next.gameserver.instancemanager.commission;

import l2next.commons.math.SafeMath;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.dao.CommissionShopDAO;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.model.items.TradeItem;
import l2next.gameserver.model.mail.Mail;
import l2next.gameserver.network.serverpackets.ExNoticePostArrived;
import l2next.gameserver.network.serverpackets.ExResponseCommissionBuyInfo;
import l2next.gameserver.network.serverpackets.ExResponseCommissionBuyItem;
import l2next.gameserver.network.serverpackets.ExResponseCommissionDelete;
import l2next.gameserver.network.serverpackets.ExResponseCommissionInfo;
import l2next.gameserver.network.serverpackets.ExResponseCommissionItemList;
import l2next.gameserver.network.serverpackets.ExResponseCommissionList;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.item.ExItemType;
import l2next.gameserver.templates.item.ItemTemplate;
import l2next.gameserver.utils.ItemFunctions;
import l2next.gameserver.utils.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Camelion
 * @modified KilRoy, Avadon
 */
public class CommissionShopManager
{
	private static final Logger _log = LoggerFactory.getLogger(CommissionShopManager.class);

	private static final long MIN_FEE = 1000;
	private static final double REGISTRATION_FEE = 0.0001;
	private static final double SALE_FEE = 0.005;
	private static final CommissionItemContainer container = new CommissionItemContainer();

	private static CommissionShopManager ourInstance = new CommissionShopManager();

	public static CommissionShopManager getInstance()
	{
		return ourInstance;
	}

	private CommissionShopManager()
	{
		restore();
	}

	public CommissionItemContainer getContainer()
	{
		return container;
	}

	private void restore()
	{
		container.restore();
	}

	/**
	 * Показывает предметы, которые игрок может продать в комиссионном магазине
	 *
	 * @param player - игрок
	 */
	public void showRegistrableItems(Player player)
	{
		ItemInstance[] items = player.getInventory().getItems();
		List<TradeItem> registrableItems = new ArrayList<TradeItem>(items.length);
		for(ItemInstance item : items)
		{
			if(item.canBeSold(player))
			{
				registrableItems.add(new TradeItem(item));
			}
		}
		player.sendPacket(new ExResponseCommissionItemList(registrableItems));
	}

	/**
	 * Добавляет предмет в регистрацию на продажу.
	 *
	 * @param player - игрок, для которого открываем окно
	 * @param itemObjId - objectId продаваемого предмета
	 */
	public void showCommissionInfo(Player player, int itemObjId)
	{
		ItemInstance item = player.getInventory().getItemByObjectId(itemObjId);
		if(item != null && item.canBeSold(player))
		{
			player.sendPacket(new ExResponseCommissionInfo(item));
		}
		else
		{
			player.sendPacket(new ExResponseCommissionInfo(item));
		}
	}

	/**
	 * Отправляет список вещей, которые стоят на продаже у игрока
	 *
	 * @param player - игрок, для которого показываем веши.
	 */
	public void showPlayerRegisteredItems(Player player)
	{
		List<CommissionItemInfo> items = CommissionShopDAO.getInstance().getRegisteredItemsFor(player);

		if(items.size() == 0)
		{
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.EMPTY_LIST));
		}
		else
		{
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.PLAYER_REGISTERED_ITEMS, 0, items));
		}
	}

	/**
	 * Регистрирует вещь в списке продаж
	 *
	 * @param player - игрок, чья вещь регистрируется
	 * @param objectId - objectId вещи
	 * @param item_name - название вещи (передает клиент)
	 * @param price - регистрируемая цена
	 * @param count - количество
	 * @param sale_days - кол-во дней (0 - 1 день, 1 - 3 дня, 2 - 5 дней, 3 - 7 дней.)
	 */
	public void registerItem(Player player, int objectId, String item_name, long price, long count, int sale_days)
	{
		PcInventory inventory = player.getInventory();
		ItemInstance item = inventory.getItemByObjectId(objectId);
		if(item == null || item.getCount() < count || !item.canBeSold(player))
		{
			return;
		}

		int days = sale_days * 2 + 1;
		if(days <= 0 || days > 7)
		{
			return;
		}

		inventory.writeLock();
		container.writeLock();
		try
		{
			long total = SafeMath.mulAndCheck(price, count); // Цена на все предметы.
			long fees = Math.round(SafeMath.mulAndCheck(total, days) * REGISTRATION_FEE);
			long fee = Math.max(fees, MIN_FEE);
			if(fee > player.getAdena() || !player.reduceAdena(fee, false))
			{
				player.sendPacket(new SystemMessage2(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA_TO_REGISTER_THE_ITEM));
				return;
			}

			ItemInstance cItem = inventory.removeItemByObjectId(objectId, count);
			container.addItem(cItem);
			CommissionShopDAO.getInstance().saveNewItem(cItem.getObjectId(), player.getObjectId(), item_name, price, cItem.getTemplate().getExItemType(), sale_days, System.currentTimeMillis() + days * 86400000, player.getName());
			Log.LogItem(player, Log.CommissionItemRegister, cItem);
		}
		catch(ArithmeticException ae)
		{
			_log.warn("CommissionShopManager.registerItem: " + ae, ae);
		}
		finally
		{
			inventory.writeUnlock();
			container.writeUnlock();
		}

		showRegistrableItems(player);
		showPlayerRegisteredItems(player);
	}

	public void showItems(int listType, int category, int rareType, int grade, String searchName, Player player)
	{
		Queue<List<CommissionItemInfo>> list = new ArrayDeque<List<CommissionItemInfo>>();
		container.readLock();
		try
		{
			ExItemType[] types;
			if(listType == 1)
			{ // Категория, оружие, броня...
				types = ExItemType.getTypesForMask(category);
			}
			else if(listType == 2)
			{
				types = new ExItemType[]{ExItemType.valueOf(category)};
			}
			else
			{
				return;
			}
			list = CommissionShopDAO.getInstance().getRegisteredItems(types, rareType, grade, searchName);
		}
		catch(Exception e)
		{
			_log.warn("CommissionShopManager.showItems: " + e, e);
		}
		finally
		{
			container.readUnlock();
		}
		if(list.size() == 1 && list.peek().isEmpty())
		{
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.EMPTY_LIST));
			return;
		}
		while(list.size() > 0)
		{
			List<CommissionItemInfo> part = list.poll();
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.ALL_ITEMS, list.size(), part));
		}
	}

	public void showCommissionBuyInfo(Player player, long auctionId, int exItemType)
	{

		if(exItemType < 0 || exItemType > ExItemType.values().length)
		{
			return;
		}
		CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId, ExItemType.values()[exItemType]);
		if(itemInfo != null)
		{
			player.sendPacket(new ExResponseCommissionBuyInfo(itemInfo));
		}
	}

	public void requestBuyItem(Player player, long auctionId, int exItemType)
	{
		CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId, ExItemType.values()[exItemType]);
		if(itemInfo == null)
		{
			return;
		}
		PcInventory inventory = player.getInventory();
		container.writeLock();
		inventory.writeLock();
		try
		{
			if(itemInfo.getSellerId() == player.getObjectId())
			{
				player.sendPacket(new SystemMessage2(SystemMsg.ITEM_PURCHASE_HAS_FAILED));
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}

			if(!inventory.validateCapacity(itemInfo.getItem()) || !inventory.validateWeight(itemInfo.getItem()))
			{
				player.sendPacket(new SystemMessage2(SystemMsg.YOUR_INVENTORY_IS_FULL));
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}

			long price = itemInfo.getRegisteredPrice() * itemInfo.getItem().getCount();

			if(price > player.getAdena())
			{
				player.sendPacket(new SystemMessage2(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}

			if(!CommissionShopDAO.getInstance().removeItem(auctionId))
			{
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}

			inventory.reduceAdena(price);
			container.removeItem(itemInfo.getItem(), itemInfo.getItem().getCount());
			inventory.addItem(itemInfo.getItem().getItemId(), itemInfo.getItem().getCount());

			player.sendPacket(new ExResponseCommissionBuyItem(1, itemInfo.getItem().getItemId(), itemInfo.getItem().getCount()));

			long fee = (long) Math.max(1000, (price * (itemInfo.getSaleDays() * 2 + 1)) * SALE_FEE);

			Mail mail = new Mail();
			mail.setSenderId(itemInfo.getSellerId());
			mail.setSenderName(itemInfo.getSellerName());
			mail.setReceiverId(itemInfo.getSellerId());
			mail.setReceiverName(itemInfo.getSellerName());
			mail.setTopic("CommissionBuyTitle");
			mail.setBody(itemInfo.getItem().getName());
			mail.setPrice(0);
			mail.setSystemMsg1(3490);
			mail.setSystemMsg2(3491);
			ItemInstance item = ItemFunctions.createItem(ItemTemplate.ITEM_ID_ADENA);
			item.setLocation(ItemInstance.ItemLocation.MAIL);
			item.setCount(price - fee);
			if(item.getCount() > 0)
			{
				item.save();
				mail.addAttachment(item);
			}
			mail.setType(Mail.SenderType.SYSTEM);
			mail.setUnread(true);
			mail.setReturnable(false);
			mail.setExpireTime(360 * 3600 + (int) (System.currentTimeMillis() / 1000L));
			mail.save();

			Player receiver = World.getPlayer(itemInfo.getSellerId());
			if(receiver != null)
			{
				receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
				receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
			}
			Log.LogItem(player, Log.CommissionItemSold, itemInfo.getItem());
		}
		catch(Exception e)
		{
			_log.warn("CommissionShopManager.requestBuyItem: " + e, e);
		}
		finally
		{
			container.writeUnlock();
			inventory.writeUnlock();
		}
	}

	public void removeExpiredItemsBid(Player player, long auctionId)
	{
		container.writeLock();
		try
		{
			CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId);

			player.sendPacket(new ExResponseCommissionDelete(1, itemInfo.getItem().getItemId(), itemInfo.getItem().getCount()));

			Mail mail = new Mail();
			mail.setSenderId(player.getObjectId());
			mail.setSenderName("CommissionShop");
			mail.setReceiverId(player.getObjectId());
			mail.setReceiverName("CommissionBuyTitle");
			mail.setTopic("CommissionBuyTitle");
			mail.setBody(itemInfo.getItem().getName());
			mail.setSystemMsg1(3492);
			mail.setSystemMsg2(3493);
			container.removeItem(itemInfo.getItem());
			itemInfo.getItem().setLocation(ItemInstance.ItemLocation.MAIL);
			itemInfo.getItem().save();
			mail.addAttachment(itemInfo.getItem());
			mail.setType(Mail.SenderType.SYSTEM);
			mail.setUnread(true);
			mail.setReturnable(false);
			mail.setExpireTime(360 * 3600 + (int) (System.currentTimeMillis() / 1000L));
			mail.save();

			Player receiver = World.getPlayer(player.getObjectId());
			if(receiver != null)
			{
				receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
				receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
			}
		}
		catch(Exception e)
		{
			_log.warn("CommissionShopManager.removeExpiredItemsBid: " + e, e);
		}
		finally
		{
			container.writeUnlock();
			CommissionShopDAO.getInstance().removeItem(auctionId);
		}
	}

	public void returnExpiredItems()
	{
		container.writeLock();
		try
		{
			List<CommissionItemInfo> expiredItems = CommissionShopDAO.getInstance().removeExpiredItems(System.currentTimeMillis());
			for(CommissionItemInfo itemInfo : expiredItems)
			{
				Mail mail = new Mail();
				mail.setSenderId(itemInfo.getItem().getOwnerId());
				mail.setSenderName("CommissionBuyTitle");
				mail.setReceiverId(itemInfo.getItem().getOwnerId());
				mail.setReceiverName("CommissionBuyTitle");
				mail.setTopic("CommissionBuyTitle");
				mail.setBody(itemInfo.getItem().getName());
				mail.setSystemMsg1(3492);
				mail.setSystemMsg2(3493);
				container.removeItem(itemInfo.getItem());
				itemInfo.getItem().setLocation(ItemInstance.ItemLocation.MAIL);
				itemInfo.getItem().save();
				mail.addAttachment(itemInfo.getItem());
				mail.setType(Mail.SenderType.SYSTEM);
				mail.setUnread(true);
				mail.setReturnable(false);
				mail.setExpireTime(360 * 3600 + (int) (System.currentTimeMillis() / 1000L));
				mail.save();

				Player receiver = World.getPlayer(itemInfo.getItem().getOwnerId());
				if(receiver != null)
				{
					receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
					receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
				}
			}
		}
		catch(Exception e)
		{
			_log.warn("CommissionShopManager.removeExpiredItems: " + e, e);
		}
		finally
		{
			container.writeUnlock();
		}
	}
}
