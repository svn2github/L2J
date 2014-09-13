package l2next.gameserver.listener.actor;

import l2next.gameserver.listener.CharListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;

public interface OnMagicHitListener extends CharListener
{
	public void onMagicHit(Creature actor, Skill skill, Creature caster);
}
