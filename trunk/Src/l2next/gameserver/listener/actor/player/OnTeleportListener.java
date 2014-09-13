package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;

public interface OnTeleportListener extends PlayerListener
{
	public void onTeleport(Player player, int x, int y, int z, Reflection reflection);
}
