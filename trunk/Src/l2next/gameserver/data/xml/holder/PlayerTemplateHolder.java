package l2next.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2next.commons.data.xml.AbstractHolder;
import l2next.gameserver.model.base.ClassId;
import l2next.gameserver.model.base.ClassType;
import l2next.gameserver.model.base.Race;
import l2next.gameserver.model.base.Sex;
import l2next.gameserver.templates.player.PlayerTemplate;

public final class PlayerTemplateHolder extends AbstractHolder
{
	private static final PlayerTemplateHolder _instance = new PlayerTemplateHolder();
	private static final TIntObjectHashMap<TIntObjectHashMap<TIntObjectHashMap<PlayerTemplate>>> _templates;

	static
	{
		_templates = new TIntObjectHashMap<TIntObjectHashMap<TIntObjectHashMap<PlayerTemplate>>>();
	}

	private static int _templates_count = 0;

	public static PlayerTemplateHolder getInstance()
	{
		return _instance;
	}

	public void addPlayerTemplate(Race race, ClassType type, Sex sex, PlayerTemplate template)
	{
		if(!_templates.containsKey(race.ordinal()))
		{
			_templates.put(race.ordinal(), new TIntObjectHashMap<TIntObjectHashMap<PlayerTemplate>>());
		}

		if(!_templates.get(race.ordinal()).containsKey(type.ordinal()))
		{
			_templates.get(race.ordinal()).put(type.ordinal(), new TIntObjectHashMap<PlayerTemplate>());
		}

		_templates.get(race.ordinal()).get(type.ordinal()).put(sex.ordinal(), template);
		_templates_count++;
	}

	public PlayerTemplate getPlayerTemplate(Race race, ClassId classId, Sex sex)
	{
		ClassType type = classId.getType();
		PlayerTemplate _template = null;
		if(_templates.containsKey(race.ordinal()))
		{
			if(_templates.get(race.ordinal()).containsKey(type.ordinal()))
			{
				_template = _templates.get(race.ordinal()).get(type.ordinal()).get(sex.ordinal());
				_template.setRace(race);
			}
		}

		return _template;
	}

	@Override
	public int size()
	{
		return _templates_count;
	}

	@Override
	public void clear()
	{
		_templates.clear();
	}
}
