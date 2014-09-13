package l2next.gameserver.handler.items.impl;

import l2next.gameserver.data.xml.holder.EnchantItemHolder;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.network.serverpackets.ChooseInventoryItem;

public class EnchantScrolls extends AbstractItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
		{
			return false;
		}
		Player player = (Player) playable;

		if(player.getEnchantScroll() != null)
		{
			return false;
		}

		player.setEnchantScroll(item);
		player.sendPacket(new ChooseInventoryItem(item.getItemId()));
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return EnchantItemHolder.getInstance().getEnchantScrolls();
	}
}