package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.Player;

public interface OnPlayerExitListener extends PlayerListener
{
	public void onPlayerExit(Player player);
}
