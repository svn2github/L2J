package l2next.gameserver.listener.actor;

import l2next.gameserver.listener.CharListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;

public interface OnCurrentHpDamageListener extends CharListener
{
	public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill);
}
