package l2next.gameserver.listener.actor;

import l2next.gameserver.listener.CharListener;
import l2next.gameserver.model.Creature;

public interface OnAttackHitListener extends CharListener
{
	public void onAttackHit(Creature actor, Creature attacker);
}
