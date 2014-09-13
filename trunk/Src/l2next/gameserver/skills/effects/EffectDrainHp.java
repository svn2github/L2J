package l2next.gameserver.skills.effects;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Effect;
import l2next.gameserver.stats.Env;
import l2next.gameserver.stats.Formulas;

public class EffectDrainHp extends Effect
{
	public EffectDrainHp(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	public boolean onActionTime()
	{
		if(_effected.isDead())
		{
			if(getSkill().isUsingWhileCasting())
			{
				_effector.abortCast(true, true);
			}
			return false;
		}
		int sps = (getSkill().isSSPossible()) ? 0 : (_effector.getChargedSoulShot()) ? 2 : (getSkill().isMagic()) ? _effector.getChargedSpiritShot() : 0;
		Formulas.AttackInfo info = Formulas.calcMagicDam(_effector, _effected, getSkill(), sps);

		if((info.damage > _effected.getCurrentHp() - 1.0D) && (!_effected.isNpc()))
		{
			if(!getSkill().isOffensive())
			{
				_effected.sendPacket(Msg.NOT_ENOUGH_HP);
			}
			if(getSkill().isUsingWhileCasting())
			{
				_effector.abortCast(true, true);
			}
			return false;
		}
		if(getSkill().getAbsorbPart() > 0.0D)
		{
			_effector.setCurrentHp(getSkill().getAbsorbPart() * Math.min(_effected.getCurrentHp(), info.damage) + _effector.getCurrentHp(), false);
		}
		_effected.reduceCurrentHp(info.damage, 0, _effector, getSkill(), !_effected.isNpc() && (_effected != _effector), _effected != _effector, _effector.isNpc() || (_effected == _effector), false, false, true, false);
		return true;
	}
}