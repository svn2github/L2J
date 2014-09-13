package l2next.gameserver.skills.effects;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;

/**
 * @author ALF
 * @date 11.07.2012 Увеличивает заряды как аура
 */
public class EffectChargesTen extends Effect
{
	private int _maxCharges;

	public EffectChargesTen(Env env, EffectTemplate template)
	{
		super(env, template);
		_maxCharges = getTemplate().getParam().getInteger("maxCharges", 10);
	}

	@Override
	public boolean onActionTime()
	{
		if(_effected.isDead())
		{
			return false;
		}

		double damage = calc();

		if(damage > _effected.getCurrentHp() - 1)
		{
			if(!getSkill().isOffensive())
			{
				_effected.sendPacket(Msg.NOT_ENOUGH_HP);
			}
			return false;
		}

		if(_effected.getIncreasedForce() >= _maxCharges)
		{
			_effected.sendPacket(Msg.YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_);
			return false;
		}

		_effected.setIncreasedForce(_effected.getIncreasedForce() + 10);
		_effected.reduceCurrentHp(damage, 0, _effector, getSkill(), false, false, true, false, false, true, false);

		return true;
	}

}
