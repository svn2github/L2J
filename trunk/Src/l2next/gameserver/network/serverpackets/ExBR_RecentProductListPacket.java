package l2next.gameserver.network.serverpackets;

import l2next.gameserver.data.xml.holder.ProductHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.primeshop.PrimeProduct;

import java.util.ArrayList;
import java.util.Collection;

public class ExBR_RecentProductListPacket extends L2GameServerPacket
{
	private Collection<PrimeProduct> _products;

	public ExBR_RecentProductListPacket(Player activeChar)
	{
		_products = new ArrayList<PrimeProduct>();

		int[] products = activeChar.getRecentProductList();
		if(products != null)
		{
			for(int productId : products)
			{
				PrimeProduct product = ProductHolder.getInstance().getProduct(productId);
				if(product == null)
				{
					continue;
				}

				_products.add(product);
			}
		}
	}

	@Override
	protected void writeImpl()
	{
		// TODO dx[dhddddcccccdd]
	}
}