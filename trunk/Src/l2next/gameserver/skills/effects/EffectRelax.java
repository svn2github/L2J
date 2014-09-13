package l2next.gameserver.skills.effects;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.stats.Env;

public class EffectRelax extends Effect
{
	private boolean _isWereSitting;

	public EffectRelax(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public boolean checkCondition()
	{
		Player player = _effected.getPlayer();
		if(player == null)
		{
			return false;
		}
		if(player.isMounted())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(_skill.getId(), _skill.getLevel()));
			return false;
		}
		return super.checkCondition();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		Player player = _effected.getPlayer();
		if(player.isMoving)
		{
			player.stopMove();
		}
		_isWereSitting = player.isSitting();
		player.sitDown(null);
	}

	@Override
	public void onExit()
	{
		super.onExit();
		if(!_isWereSitting)
		{
			_effected.getPlayer().standUp();
		}
	}

	@Override
	public boolean onActionTime()
	{
		Player player = _effected.getPlayer();
		if(player.isAlikeDead() || player == null)
		{
			return false;
		}

		if(!player.isSitting())
		{
			return false;
		}

		if(player.isCurrentHpFull() && getSkill().isToggle())
		{
			getEffected().sendPacket(Msg.HP_WAS_FULLY_RECOVERED_AND_SKILL_WAS_REMOVED);
			return false;
		}

		double manaDam = calc();
		if(manaDam > _effected.getCurrentMp())
		{
			if(getSkill().isToggle())
			{
				player.sendPacket(Msg.NOT_ENOUGH_MP, new SystemMessage(SystemMessage.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(getSkill().getId(), getSkill().getDisplayLevel()));
				return false;
			}
		}

		_effected.reduceCurrentMp(manaDam, null);

		return true;
	}
}