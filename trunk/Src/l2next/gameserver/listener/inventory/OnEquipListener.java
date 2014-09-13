package l2next.gameserver.listener.inventory;

import l2next.commons.listener.Listener;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.items.ItemInstance;

public interface OnEquipListener extends Listener<Playable>
{
	public void onEquip(int slot, ItemInstance item, Playable actor);

	public void onUnequip(int slot, ItemInstance item, Playable actor);
}
