package l2next.gameserver.stats.conditions;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.Race;
import l2next.gameserver.stats.Env;

public class ConditionPlayerRace extends Condition
{
	private final Race _race;

	public ConditionPlayerRace(String race)
	{
		_race = Race.valueOf(race.toLowerCase());
	}

	@Override
	protected boolean testImpl(Env env)
	{
		if(!env.character.isPlayer())
		{
			return false;
		}
		return ((Player) env.character).getRace() == _race;
	}
}