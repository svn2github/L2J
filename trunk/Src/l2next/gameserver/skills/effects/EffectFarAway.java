package l2next.gameserver.skills.effects;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Skill.SkillType;
import l2next.gameserver.stats.Env;
import l2next.gameserver.stats.Stats;

public final class EffectFarAway extends Effect
{
	// TODO уточнить уровни 1, 2, 9, 10, 11, 12
	private static int[] bleed = new int[]{
		12,
		17,
		25,
		34,
		44,
		54,
		62,
		67,
		72,
		77,
		82,
		87
	};
	private static int[] poison = new int[]{
		11,
		16,
		24,
		32,
		41,
		50,
		58,
		63,
		68,
		72,
		77,
		82
	};
	private boolean _percent;

	public EffectFarAway(Env env, EffectTemplate template)
	{
		super(env, template);
		_percent = getTemplate().getParam().getBool("percent", false);
	}

	@Override
	public boolean checkCondition()
	{
		if(_effected.isInvul())
		{
			return false;
		}
		Skill skill = _effected.getCastingSkill();
		if(skill != null && (skill.getSkillType() == SkillType.TAKECASTLE || skill.getSkillType() == SkillType.TAKEFORTRESS || skill.getSkillType() == SkillType.TAKEFLAG))
		{
			return false;
		}
		return super.checkCondition();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_effected.startHealBlocked();
		_effected.setIsInvul(true);
	}

	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopHealBlocked();
		_effected.setIsInvul(false);
	}

	@Override
	public boolean onActionTime()
	{
		if(_effected.isDead())
		{
			return false;
		}

		double damage = calc();
		if(_percent)
		{
			damage = _effected.getMaxHp() * _template._value * 0.01;
		}
		if(damage < 2 && getStackOrder() != -1)
		{
			switch(getEffectType())
			{
				case Poison:
					damage = poison[getStackOrder() - 1] * getPeriod() / 1000;
					break;
				case Bleed:
					damage = bleed[getStackOrder() - 1] * getPeriod() / 1000;
					break;
			}
		}

		damage = _effector.calcStat(getSkill().isMagic() ? Stats.MAGIC_DAMAGE : Stats.PHYSICAL_DAMAGE, damage, _effected, getSkill());

		if(damage > _effected.getCurrentHp() - 1 && !_effected.isNpc())
		{
			if(!getSkill().isOffensive())
			{
				_effected.sendPacket(Msg.NOT_ENOUGH_HP);
			}
			return false;
		}

		if(getSkill().getAbsorbPart() > 0)
		{
			_effector.setCurrentHp(getSkill().getAbsorbPart() * Math.min(_effected.getCurrentHp(), damage) + _effector.getCurrentHp(), false);
		}

		_effected.reduceCurrentHp(damage, 0, _effector, getSkill(), !_effected.isNpc() && _effected != _effector, _effected != _effector, _effector.isNpc() || _effected == _effector, false, false, true, false);

		return true;
	}
}