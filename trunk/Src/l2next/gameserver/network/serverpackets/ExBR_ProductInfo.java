package l2next.gameserver.network.serverpackets;

import l2next.gameserver.data.xml.holder.ProductHolder;
import l2next.gameserver.model.items.primeshop.PrimeItem;
import l2next.gameserver.model.items.primeshop.PrimeProduct;

public class ExBR_ProductInfo extends L2GameServerPacket
{
	private PrimeProduct _productId;

	public ExBR_ProductInfo(int id)
	{
		_productId = ProductHolder.getInstance().getProduct(id);
	}

	@Override
	protected void writeImpl()
	{
		if(_productId == null)
		{
			return;
		}

		writeD(_productId.getProductId()); //product id
		writeD(_productId.getPoints()); // points
		writeD(_productId.getComponents().size()); //size

		for(PrimeItem com : _productId.getComponents())
		{
			writeD(com.getItemId()); //item id
			writeD(com.getCount()); //quality
			writeD(com.getWeight()); //weight
			writeD(com.dropable()); //0 - dont drop/trade
		}
	}
}