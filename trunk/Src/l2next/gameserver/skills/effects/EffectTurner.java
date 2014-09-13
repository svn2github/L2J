package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Effect;
import l2next.gameserver.network.serverpackets.FinishRotating;
import l2next.gameserver.network.serverpackets.StartRotating;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.stats.Env;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 30.06.12 Time: 9:39
 */
public class EffectTurner extends Effect
{
	public EffectTurner(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		getEffected().broadcastPacket(new StartRotating(getEffected(), getEffected().getHeading(), 1, 65535));
		getEffected().broadcastPacket(new FinishRotating(getEffected(), getEffector().getHeading(), 65535));
		getEffected().setHeading(getEffector().getHeading());
		getEffected().sendPacket(new SystemMessage(SystemMessage.S1_S2S_EFFECT_CAN_BE_FELT).addSkillName(_displayId, _displayLevel));
		getEffected().startStunning();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		getEffected().stopStunning();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
