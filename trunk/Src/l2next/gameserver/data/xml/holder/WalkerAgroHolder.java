package l2next.gameserver.data.xml.holder;

import l2next.commons.data.xml.AbstractHolder;
import l2next.gameserver.templates.spawn.WalkerAgroTemplate;

import java.util.ArrayList;
import java.util.List;

public final class WalkerAgroHolder extends AbstractHolder
{
	private static final WalkerAgroHolder _instance = new WalkerAgroHolder();

	private List<WalkerAgroTemplate> _spawns = new ArrayList<WalkerAgroTemplate>();

	public static WalkerAgroHolder getInstance()
	{
		return _instance;
	}

	protected WalkerAgroHolder()
	{
	}

	public void addSpawn(WalkerAgroTemplate spawn)
	{
		_spawns.add(spawn);
	}

	public List<WalkerAgroTemplate> getSpawns()
	{
		return _spawns;
	}

	@Override
	public int size()
	{
		return _spawns.size();
	}

	@Override
	public void clear()
	{
		_spawns.clear();
	}
}
