package l2next.gameserver.listener.actor;

import l2next.gameserver.listener.CharListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;

public interface OnMagicUseListener extends CharListener
{
	public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt);
}
