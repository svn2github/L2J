package l2next.gameserver.network.serverpackets;

import l2next.gameserver.data.xml.holder.ProductHolder;
import l2next.gameserver.model.items.primeshop.PrimeItem;
import l2next.gameserver.model.items.primeshop.PrimeProduct;

import java.util.Collection;

public class ExBR_ProductList extends L2GameServerPacket
{
	private final Collection<PrimeProduct> products;

	public ExBR_ProductList()
	{
		products = ProductHolder.getInstance().getItemsOnSale();
	}

	@Override
	protected void writeImpl()
	{

		writeD(0x00);                                               // под вопросом
		writeD(products.size());
		for(PrimeProduct template : products)
		{
			writeD(template.getProductId());                        // Идентификатор продукта
			writeH(template.getCategory());                         // Категория - под вопросом
			writeD(template.getPoints());                           // Стоимость
			writeD(0x00);                                           // под вопросом
			writeD(template.getTabId());                            // под вопросом
			writeD((int) template.getStartTimeSale());              // Дата начала торговли
			writeD((int) template.getEndTimeSale());                // Дата окончания торговли
			writeC(127);                                            // day week (127 = not daily goods)
			writeC(template.getStartHour());                        // Часы начала торговли
			writeC(template.getStartMin());                         // Минуты начала торговли
			writeC(template.getEndHour());                          // Часы окончания торговли
			writeC(template.getEndMin());                           // Минуты окончания торговли
			writeD(0x00);                                           // Продано
			writeD(-1);                                             // Максимальное количество возможных продаж
			writeD(0x00);                                           // под вопросом
			writeD(0x00);                                           // Минимальный уровень для покупки
			writeD(template.getComponents().size());                // Количество предметов
			for(PrimeItem item : template.getComponents())
			{
				writeD(item.getItemId());                           // Идентификатор предмета
				writeD(item.getCount());                            // Количетво предмета
				writeD(item.getWeight());                           // Вес предмета
				writeD(item.dropable());                            // dropable - под вопросом
			}
		}
	}
}