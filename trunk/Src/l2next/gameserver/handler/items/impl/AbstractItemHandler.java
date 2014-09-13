package l2next.gameserver.handler.items.impl;

import l2next.gameserver.handler.items.IItemHandler;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.Log;

/**
 * @user: Mifesto
 * @date: 11:32 / 03.05.13
 * @team: http://novell-project.ru/
 */
public abstract class AbstractItemHandler implements IItemHandler
{
	@Override
	public void dropItem(Player player, ItemInstance item, long count, Location loc)
	{
		if(item.isEquipped())
		{
			player.getInventory().unEquipItem(item);
			player.sendUserInfo(true);
		}

		item = player.getInventory().removeItemByObjectId(item.getObjectId(), count);
		if(item == null)
		{
			player.sendActionFailed();
			return;
		}

		Log.LogItem(player, Log.Drop, item);

		item.dropToTheGround(player, loc);
		player.disableDrop(1000);

		player.sendChanges();
	}

	@Override
	public boolean pickupItem(Playable playable, ItemInstance item)
	{
		return true;
	}
}