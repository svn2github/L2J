package l2next.gameserver.listener.actor.npc;

import l2next.gameserver.listener.NpcListener;
import l2next.gameserver.model.instances.NpcInstance;

public interface OnDecayListener extends NpcListener
{
	public void onDecay(NpcInstance actor);
}
