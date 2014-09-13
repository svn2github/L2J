package l2next.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2next.commons.data.xml.AbstractHolder;
import l2next.gameserver.model.ArmorSet;

public final class ArmorSetsHolder extends AbstractHolder
{
	private static final ArmorSetsHolder _instance = new ArmorSetsHolder();
	private final TIntObjectHashMap<ArmorSet> _armorSets = new TIntObjectHashMap<ArmorSet>();

	public static ArmorSetsHolder getInstance()
	{
		return _instance;
	}

	public void addArmorSet(ArmorSet armorset)
	{
		for(int id : armorset.getChestIds())
		{
			_armorSets.put(id, armorset);
		}

		for(int id : armorset.getLegIds())
		{
			_armorSets.put(id, armorset);
		}

		for(int id : armorset.getHeadIds())
		{
			_armorSets.put(id, armorset);
		}

		for(int id : armorset.getGlovesIds())
		{
			_armorSets.put(id, armorset);
		}

		for(int id : armorset.getFeetIds())
		{
			_armorSets.put(id, armorset);
		}

		for(int id : armorset.getShieldIds())
		{
			_armorSets.put(id, armorset);
		}
	}

	public ArmorSet getArmorSet(int id)
	{
		return _armorSets.get(id);
	}

	@Override
	public int size()
	{
		return _armorSets.size();
	}

	@Override
	public void clear()
	{
		_armorSets.clear();
	}
}
