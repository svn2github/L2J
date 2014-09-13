package l2next.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2next.commons.data.xml.AbstractHolder;

import java.util.ArrayList;
import java.util.List;

public class StartItemHolder extends AbstractHolder
{
	public static class StartItem
	{
		public int id;
		public long count;
		public boolean equipped = false;
		public boolean warehouse = false;
	}

	private static StartItemHolder _instance = new StartItemHolder();
	private TIntObjectHashMap<ArrayList<StartItem>> _startItemTemplate;

	private StartItemHolder()
	{
		_startItemTemplate = new TIntObjectHashMap<ArrayList<StartItem>>();
	}

	public void clear()
	{
		_startItemTemplate.clear();
	}

	public int size()
	{
		return _startItemTemplate.size();
	}

	public static StartItemHolder getInstance()
	{
		return _instance;
	}

	public void addStartItem(StartItem item, int classId)
	{
		if(!_startItemTemplate.containsKey(classId))
		{
			_startItemTemplate.put(classId, new ArrayList<StartItem>());
		}
		_startItemTemplate.get(classId).add(item);
	}

	public List<StartItem> getStartItems(int classId)
	{
		return _startItemTemplate.get(classId) != null ? _startItemTemplate.get(classId) : new ArrayList<StartItem>(0);
	}
}