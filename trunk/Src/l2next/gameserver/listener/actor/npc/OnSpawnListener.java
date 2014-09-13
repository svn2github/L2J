package l2next.gameserver.listener.actor.npc;

import l2next.gameserver.listener.NpcListener;
import l2next.gameserver.model.instances.NpcInstance;

public interface OnSpawnListener extends NpcListener
{
	public void onSpawn(NpcInstance actor);
}
