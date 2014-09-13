package l2next.gameserver.skills.effects;

import l2next.commons.collections.GArray;
import l2next.commons.util.Rnd;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

/**
 * @author ALF
 * @date 06.11.2012
 */
public class EffectChangeTarget extends Effect
{
	private int radius;
	private int height;

	public EffectChangeTarget(Env env, EffectTemplate template)
	{
		super(env, template);
		radius = template.getParam().getInteger("radius");
		height = template.getParam().getInteger("height");
	}

	public boolean checkCondition()
	{
		return Rnd.chance(_template.chance(100));
	}

	@Override
	public void onStart()
	{
		GArray<Creature> _around = getEffected().getAroundCharacters(radius, height);

		if(_around.isEmpty())
		{
			return;
		}

		_around.remove(getEffector());

		if(_around.isEmpty())
		{
			return;
		}

		Creature target = _around.get(Rnd.get(_around.size()));

		getEffected().setTarget(target);

	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}

	@Override
	public boolean isHidden()
	{
		return true;
	}

}
