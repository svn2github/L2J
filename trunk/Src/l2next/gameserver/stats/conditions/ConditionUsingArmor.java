package l2next.gameserver.stats.conditions;

import l2next.gameserver.model.Player;
import l2next.gameserver.stats.Env;
import l2next.gameserver.templates.item.ArmorTemplate.ArmorType;

public class ConditionUsingArmor extends Condition
{
	private final ArmorType _armor;

	public ConditionUsingArmor(ArmorType armor)
	{
		_armor = armor;
	}

	@Override
	protected boolean testImpl(Env env)
	{
		if(env.character.isPlayer() && ((Player) env.character).isWearingArmor(_armor))
		{
			return true;
		}

		return false;
	}
}
