package l2next.gameserver.stats.triggers;

import l2next.commons.lang.ArrayUtils;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.stats.Env;
import l2next.gameserver.stats.conditions.Condition;

/**
 * @author VISTALL
 * @date 15:03/22.01.2011
 */
public class TriggerInfo extends Skill.AddedSkill
{
	private final TriggerType _type;
	private final double _chance;
	private Condition[] _conditions = Condition.EMPTY_ARRAY;
	private final boolean _cancelEffectsOnRemove;

	public TriggerInfo(int id, int level, TriggerType type, double chance, boolean cancel)
	{
		super(id, level);
		_type = type;
		_chance = chance;
		_cancelEffectsOnRemove = cancel;
	}

	public final void addCondition(Condition c)
	{
		_conditions = ArrayUtils.add(_conditions, c);
	}

	public boolean checkCondition(Creature actor, Creature target, Creature aimTarget, Skill owner, double damage)
	{
		// Скилл проверяется и кастуется на aimTarget
		if(getSkill().checkTarget(actor, aimTarget, aimTarget, false, false) != null)
		{
			return false;
		}

		Env env = new Env();
		env.character = actor;
		env.skill = owner;
		env.target = target; // В условии проверяется реальная цель.
		env.value = damage;

		for(Condition c : _conditions)
		{
			if(!c.test(env))
			{
				return false;
			}
		}
		return true;
	}

	public TriggerType getType()
	{
		return _type;
	}

	public double getChance()
	{
		return _chance;
	}
	
	public boolean cancelEffectsOnRemove()
	{
		return _cancelEffectsOnRemove;
	}
}
