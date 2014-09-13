package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.GameObject;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;

public interface OnUseItemListener extends PlayerListener
{
	public void onUseItem(Player player, GameObject target, ItemInstance item);
}