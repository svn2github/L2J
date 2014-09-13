package l2next.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2next.commons.data.xml.AbstractHolder;

import java.util.ArrayList;
import java.util.List;

public class ItemLevelHolder extends AbstractHolder
{
	public static class ItemLevel
	{
		public int id;
		public long count;
	}

	private static ItemLevelHolder _instance = new ItemLevelHolder();
	private TIntObjectHashMap<ArrayList<ItemLevel>> _itemLevelTemplate;

	private ItemLevelHolder()
	{
		_itemLevelTemplate = new TIntObjectHashMap<ArrayList<ItemLevel>>();
	}

	public void clear()
	{
		_itemLevelTemplate.clear();
	}

	public int size()
	{
		return _itemLevelTemplate.size();
	}

	public static ItemLevelHolder getInstance()
	{
		return _instance;
	}

	public void addItemLevel(ItemLevel item, int levelPlayer)
	{
		if(!_itemLevelTemplate.containsKey(levelPlayer))
		{
			_itemLevelTemplate.put(levelPlayer, new ArrayList<ItemLevel>());
		}
		_itemLevelTemplate.get(levelPlayer).add(item);
	}

	public List<ItemLevel> getItemLevel(int levelPlayer)
	{
		return _itemLevelTemplate.get(levelPlayer) != null ? _itemLevelTemplate.get(levelPlayer) : new ArrayList<ItemLevel>(0);
	}
}