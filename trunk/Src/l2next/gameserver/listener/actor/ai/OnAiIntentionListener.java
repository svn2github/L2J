package l2next.gameserver.listener.actor.ai;

import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.listener.AiListener;
import l2next.gameserver.model.Creature;

public interface OnAiIntentionListener extends AiListener
{
	public void onAiIntention(Creature actor, CtrlIntention intention, Object arg0, Object arg1);
}
