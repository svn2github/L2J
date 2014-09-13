package l2next.gameserver.skills.effects;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Player;
import l2next.gameserver.stats.Env;

public final class EffectCharge extends Effect
{
	// Максимальное количество зарядов находится в поле val="xx"

	public EffectCharge(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		if(getEffected().isPlayer())
		{
			final Player player = (Player) getEffected();

			if(player.getIncreasedForce() >= calc())
			{
				player.sendPacket(Msg.YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_);
			}
			else
			{
				player.setIncreasedForce(player.getIncreasedForce() + 1);
			}
		}
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
