package l2next.gameserver.skills.effects;

import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ChangeWaitType;
import l2next.gameserver.network.serverpackets.Revive;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.stats.Env;

public final class EffectFakeDeath extends Effect
{
	public EffectFakeDeath(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		Player player = (Player) getEffected();
		player.setFakeDeath(true);
		player.getAI().notifyEvent(CtrlEvent.EVT_FAKE_DEATH, null, null);
		player.broadcastPacket(new ChangeWaitType(player, ChangeWaitType.WT_START_FAKEDEATH));
		player.broadcastCharInfo();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		// 5 секунд после FakeDeath на персонажа не агрятся мобы
		Player player = (Player) getEffected();
		player.setNonAggroTime(System.currentTimeMillis() + 5000L);
		player.setFakeDeath(false);
		player.broadcastPacket(new ChangeWaitType(player, ChangeWaitType.WT_STOP_FAKEDEATH));
		player.broadcastPacket(new Revive(player));
		player.broadcastCharInfo();
	}

	@Override
	public boolean onActionTime()
	{
		if(getEffected().isDead())
		{
			return false;
		}

		double manaDam = calc();

		if(manaDam > getEffected().getCurrentMp())
		{
			if(getSkill().isToggle())
			{
				getEffected().sendPacket(Msg.NOT_ENOUGH_MP);
				getEffected().sendPacket(new SystemMessage(SystemMessage.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(getSkill().getId(), getSkill().getDisplayLevel()));
				return false;
			}
		}

		getEffected().reduceCurrentMp(manaDam, null);
		return true;
	}
}