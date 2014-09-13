package l2next.gameserver.handler.items.impl;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.data.xml.holder.EnchantItemHolder;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.network.serverpackets.ExChooseInventoryAttributeItem;

/**
 * @author SYS
 * @rework ALF
 * @data 26.06.2012
 */
public class AttributeStones extends AbstractItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
		{
			return false;
		}
		Player player = (Player) playable;

		if(player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			player.sendPacket(Msg.YOU_CANNOT_ADD_ELEMENTAL_POWER_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return false;
		}

		if(player.getEnchantScroll() != null)
		{
			return false;
		}

		player.setEnchantScroll(item);
		player.sendPacket(Msg.PLEASE_SELECT_ITEM_TO_ADD_ELEMENTAL_POWER);
		player.sendPacket(new ExChooseInventoryAttributeItem(player, item));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return EnchantItemHolder.getInstance().getAttributeStones();
	}
}