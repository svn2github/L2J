package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.items.TradeItem;

import java.util.List;

/**
 * @author : Ragnarok
 * @date : 09.02.12  12:36
 *
 * Уходит в ответ на {@link l2next.gameserver.network.clientpackets.RequestCommissionRegistrableItemList}
 * Отсылает список вещей, которые можно продать в коммисионном магазине.
 */
public class ExResponseCommissionItemList extends L2GameServerPacket
{
	private List<TradeItem> items;

	public ExResponseCommissionItemList(List<TradeItem> items)
	{
		this.items = items;
	}

	@Override
	protected void writeImpl()
	{
		writeD(items.size());// itemsSize
		for(TradeItem item : items)
		{
			writeItemInfo(item);
		}
	}
}
