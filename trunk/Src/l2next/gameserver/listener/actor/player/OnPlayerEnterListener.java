package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.Player;

public interface OnPlayerEnterListener extends PlayerListener
{
	public void onPlayerEnter(Player player);
}
