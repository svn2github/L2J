package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.network.serverpackets.FlyToLocation;
import l2next.gameserver.stats.Env;
import l2next.gameserver.utils.Location;

/**
 * @author JustForFun
 */

public class EffectThrowHorizontal extends Effect
{
	public EffectThrowHorizontal(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		Location loc = _effected.getFlyLocation(_effected, getSkill());
		_effected.setLoc(loc);
		_effected.broadcastPacket(new FlyToLocation(_effected, loc, FlyToLocation.FlyType.THROW_HORIZONTAL, 100));
		_effected.stopMove();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopMove();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}