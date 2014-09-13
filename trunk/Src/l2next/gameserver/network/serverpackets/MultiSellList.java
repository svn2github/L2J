package l2next.gameserver.network.serverpackets;

import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import l2next.gameserver.model.base.MultiSellEntry;
import l2next.gameserver.model.base.MultiSellIngredient;

import java.util.ArrayList;
import java.util.List;

public class MultiSellList extends L2GameServerPacket
{
	private final int _page;
	private final int _finished;
	private final int _listId;
	private boolean _isnew;
	private boolean _isNewProduction;
	private final List<MultiSellEntry> _list;

	public MultiSellList(MultiSellListContainer list, int page, int finished, boolean isNew, boolean isNewProduction)
	{
		_list = list.getEntries();
		_listId = list.getListId();
		_page = page;
		_finished = finished;
		_isnew = isNew;
		_isNewProduction = isNewProduction;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_listId); // list id
		writeD(_page); // page
		writeD(_finished); // finished
		writeD(Config.MULTISELL_SIZE); // size of pages
		writeD(_list.size()); // list length
		writeC(_isnew ? 0x01 : 0x00); // L2WT GOD при 1 открывается новый тип мультисела, с обменником
		List<MultiSellIngredient> ingredients;
		List<MultiSellIngredient> production;
		for(MultiSellEntry ent : _list)
		{
			ingredients = fixIngredients(ent.getIngredients());
			production = ent.getProduction();

			writeD(ent.getEntryId());

			final MultiSellIngredient firstEntry = ent.getProduction().get(0);
			if(firstEntry == null)
			{
				writeC(0x00);
				writeH(0x00);
				writeD(0x00);
				writeD(0x00);
				writeItemElements();
			}
			else
			{
				writeC(firstEntry.isStackable() ? 1 : 0);
				writeH(firstEntry.getItemEnchant());
				writeAugmentationInfo(firstEntry);
				writeItemElements(firstEntry);
			}

			writeH(_isnew ? production.size() + 1 : production.size());
			writeH(ingredients.size());

			if(_isnew && _isNewProduction)
			{
				writeInfo(firstEntry, true);
			}
			else if(_isnew && !_isNewProduction)
			{
				writeInfo(ingredients.get(0), true);
			}

			for(final MultiSellIngredient prod : ent.getProduction())
			{
				writeInfo(prod, true);
			}

			for(final MultiSellIngredient i : ingredients)
			{
				writeInfo(i, false);
			}
		}
	}

	// FIXME временная затычка, пока NCSoft не починят в клиенте отображение
	// мультиселов где кол-во больше Integer.MAX_VALUE
	private static List<MultiSellIngredient> fixIngredients(List<MultiSellIngredient> ingredients)
	{
		int needFix = 0;
		for(MultiSellIngredient ingredient : ingredients)
		{
			if(ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				needFix++;
			}
		}

		if(needFix == 0)
		{
			return ingredients;
		}

		MultiSellIngredient temp;
		List<MultiSellIngredient> result = new ArrayList<MultiSellIngredient>(ingredients.size() + needFix);
		for(MultiSellIngredient ingredient : ingredients)
		{
			ingredient = ingredient.clone();
			while(ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				temp = ingredient.clone();
				temp.setItemCount(2000000000);
				result.add(temp);
				ingredient.setItemCount(ingredient.getItemCount() - 2000000000);
			}
			if(ingredient.getItemCount() > 0)
			{
				result.add(ingredient);
			}
		}

		return result;
	}
}