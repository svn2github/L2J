package l2next.gameserver.utils;

import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.model.Player;

/**
 * User: Samurai
 */
public class Merchant
{
	public static boolean pay(Player player, int itemId, long count)
	{
		if(player.getInventory().getItemByItemId(itemId) == null)
		{
			player.sendMessage("Цена: " + count + " " + getItemName(itemId));
			player.sendMessage("У вас вообще нету " + getItemName(itemId));
			return false;
		}
		else if(getItemCount(player, itemId) < count)
		{
			player.sendMessage("Цена: " + count + " " + getItemName(itemId));
			player.sendMessage("У вас нету " + count + " " + getItemName(itemId));
			return false;
		}
		else
		{
			player.getInventory().destroyItemByItemId(itemId, count);
			return true;
		}
	}

	public static void addItem(Player player, int itemId, long count)
	{
		player.getInventory().addItem(itemId, count);
		StringBuilder sb = new StringBuilder();
		sb.append("Вы получили ");
		sb.append(count).append(" ");
		sb.append(getItemName(itemId));
		player.sendMessage(sb.toString());
	}

	public static String getItemName(int itemId)
	{
		return ItemHolder.getInstance().getTemplate(itemId).getName();
	}

	public static long getItemCount(Player player, int itemId)
	{
		return player.getInventory().getItemByItemId(itemId).getCount();
	}
}