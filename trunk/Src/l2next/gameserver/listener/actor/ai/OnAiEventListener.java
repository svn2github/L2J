package l2next.gameserver.listener.actor.ai;

import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.listener.AiListener;
import l2next.gameserver.model.Creature;

public interface OnAiEventListener extends AiListener
{
	public void onAiEvent(Creature actor, CtrlEvent evt, Object[] args);
}
