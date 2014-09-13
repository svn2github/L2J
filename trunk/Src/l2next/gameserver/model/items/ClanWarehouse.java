package l2next.gameserver.model.items;

import l2next.gameserver.model.items.ItemInstance.ItemLocation;
import l2next.gameserver.model.pledge.Clan;

public final class ClanWarehouse extends Warehouse
{
	public ClanWarehouse(Clan clan)
	{
		super(clan.getClanId());
	}

	@Override
	public ItemLocation getItemLocation()
	{
		return ItemLocation.CLANWH;
	}
}