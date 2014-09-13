package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.EffectList;
import l2next.gameserver.skills.AbnormalEffect;
import l2next.gameserver.skills.EffectType;
import l2next.gameserver.stats.Env;
import l2next.gameserver.stats.StatTemplate;
import l2next.gameserver.stats.conditions.Condition;
import l2next.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class EffectTemplate extends StatTemplate
{
	private static final Logger _log = LoggerFactory.getLogger(EffectTemplate.class);

	public static final EffectTemplate[] EMPTY_ARRAY = new EffectTemplate[0];

	public static final String NO_STACK = "none".intern();
	public static final String HP_RECOVER_CAST = "HpRecoverCast".intern();

	public Condition _attachCond;
	public final double _value;
	public final int _count;
	public final long _period; // in milliseconds
	public AbnormalEffect _abnormalEffect;
	public AbnormalEffect _abnormalEffect2;
	public AbnormalEffect _abnormalEffect3;

	public final EffectType _effectType;

	public final String _stackTypes;
	public final String _stackTypes1;
	public final String _stackTypes2;
	public final String _stackTypes3;
	public final int _stackOrder;
	public final int _displayId;
	public final int _displayLevel;

	public final boolean _applyOnCaster;
	public final boolean _applyOnSummon;
	public final boolean _cancelOnAction;
	public final boolean _isReflectable;
	private final Boolean _isSaveable;
	private final Boolean _isCancelable;
	private final Boolean _isOffensive;

	private final StatsSet _paramSet;
	private final int _chance;

	public EffectTemplate(StatsSet set)
	{
		_value = set.getDouble("value");
		_count = set.getInteger("count", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("count", 1);
		_period = Math.min(Integer.MAX_VALUE, 1000 * (set.getInteger("time", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("time", 1)));
		_abnormalEffect = set.getEnum("abnormal", AbnormalEffect.class);
		_abnormalEffect2 = set.getEnum("abnormal2", AbnormalEffect.class);
		_abnormalEffect3 = set.getEnum("abnormal3", AbnormalEffect.class);

		_stackTypes = set.getString("stackType", NO_STACK);
		_stackTypes1 = set.getString("stackType1", NO_STACK);
		_stackTypes2 = set.getString("stackType2", NO_STACK);
		_stackTypes3 = set.getString("stackType3", NO_STACK);

		//_stackOrder = set.getInteger("stackOrder", _stackTypes.contains(NO_STACK) ? 1 : 0);
		_stackOrder = set.getInteger("stackOrder", (_stackTypes.contains(NO_STACK)) && (_stackTypes1.contains(NO_STACK)) && (_stackTypes2.contains(NO_STACK)) && (_stackTypes3.contains(NO_STACK))? 1 : 0);
		_applyOnCaster = set.getBool("applyOnCaster", false);
		_applyOnSummon = set.getBool("applyOnSummon", true);
		_cancelOnAction = set.getBool("cancelOnAction", false);
		_isReflectable = set.getBool("isReflectable", true);
		_isSaveable = set.isSet("isSaveable") ? set.getBool("isSaveable") : null;
		_isCancelable = set.isSet("isCancelable") ? set.getBool("isCancelable") : null;
		_isOffensive = set.isSet("isOffensive") ? set.getBool("isOffensive") : null;
		_displayId = set.getInteger("displayId", 0);
		_displayLevel = set.getInteger("displayLevel", 0);
		_effectType = set.getEnum("name", EffectType.class);
		_chance = set.getInteger("chance", Integer.MAX_VALUE);
		_paramSet = set;
	}

	public Effect getEffect(Env env)
	{
		if(_attachCond != null && !_attachCond.test(env))
		{
			return null;
		}
		try
		{
			return _effectType.makeEffect(env, this);
		}
		catch(Exception e)
		{
			_log.error("", e);
		}

		return null;
	}

	public void attachCond(Condition c)
	{
		_attachCond = c;
	}

	public int getCount()
	{
		return _count;
	}

	public long getPeriod()
	{
		return _period;
	}

	public EffectType getEffectType()
	{
		return _effectType;
	}

	public Effect getSameByStackType(List<Effect> list)
	{
		for(Effect ef : list)
		{
			if(ef != null && EffectList.checkStackType(ef.getTemplate(), this))
			{
				return ef;
			}
		}
		return null;
	}

	public Effect getSameByStackType(EffectList list)
	{
		return getSameByStackType(list.getAllEffects());
	}

	public Effect getSameByStackType(Creature actor)
	{
		return getSameByStackType(actor.getEffectList().getAllEffects());
	}

	public StatsSet getParam()
	{
		return _paramSet;
	}

	public int chance(int val)
	{
		return _chance == Integer.MAX_VALUE ? val : _chance;
	}

	public boolean isSaveable(boolean def)
	{
		return _isSaveable != null ? _isSaveable.booleanValue() : def;
	}

	public boolean isCancelable(boolean def)
	{
		return _isCancelable != null ? _isCancelable.booleanValue() : def;
	}

	public boolean isOffensive(boolean def)
	{
		return _isOffensive != null ? _isOffensive.booleanValue() : def;
	}
}