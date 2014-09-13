package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.xml.holder.ItemHolder;
import l2next.gameserver.data.xml.holder.ProductHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.primeshop.PrimeItem;
import l2next.gameserver.model.items.primeshop.PrimeProduct;
import l2next.gameserver.network.serverpackets.ExBR_BuyProduct;
import l2next.gameserver.network.serverpackets.ExBR_GamePoint;
import l2next.gameserver.templates.item.ItemTemplate;

public class RequestExBR_BuyProduct extends L2GameClientPacket
{
	private int _productId;
	private int _count;

	@Override
	protected void readImpl()
	{
		_productId = readD();
		_count = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();

		if(activeChar == null)
		{
			return;
		}

		if(_count > 99 || _count < 0)
		{
			return;
		}

		PrimeProduct product = ProductHolder.getInstance().getProduct(_productId);
		if(product == null)
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_WRONG_PRODUCT));
			return;
		}

		if(System.currentTimeMillis() / 1000 < product.getStartTimeSale() || System.currentTimeMillis() / 1000 > product.getEndTimeSale())
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_SALE_PERIOD_ENDED));
			return;
		}

		int totalPoints = product.getPoints() * _count;

		if(totalPoints < 0)
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_WRONG_PRODUCT));
			return;
		}

		final long gamePointSize = activeChar.getPremiumPoints();

		if(totalPoints > gamePointSize)
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_NOT_ENOUGH_POINTS));
			return;
		}

		int totalWeight = 0;
		for(PrimeItem com : product.getComponents())
		{
			totalWeight += com.getWeight();
		}

		totalWeight *= _count; // увеличиваем вес согласно количеству

		int totalCount = 0;

		for(PrimeItem com : product.getComponents())
		{
			ItemTemplate item = ItemHolder.getInstance().getTemplate(com.getItemId());
			if(item == null)
			{
				activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_WRONG_PRODUCT));
				return; // what
			}
			totalCount += item.isStackable() ? 1 : com.getCount() * _count;
		}

		if(!activeChar.getInventory().validateCapacity(totalCount) || !activeChar.getInventory().validateWeight(totalWeight))
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_INVENTORY_FULL));
			return;
		}

		activeChar.reducePremiumPoints(totalPoints);

		for(PrimeItem $comp : product.getComponents())
		{
			activeChar.getInventory().addItem($comp.getItemId(), $comp.getCount() * _count);
		}

		activeChar.sendPacket(new ExBR_GamePoint(activeChar));
		activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_OK));
		activeChar.sendChanges();
	}
}