package l2next.gameserver.listener.actor;

import l2next.gameserver.listener.CharListener;
import l2next.gameserver.model.Creature;

public interface OnAttackListener extends CharListener
{
	public void onAttack(Creature actor, Creature target);
}
