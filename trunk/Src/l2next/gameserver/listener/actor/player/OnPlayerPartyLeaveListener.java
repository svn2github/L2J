package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.Player;

public interface OnPlayerPartyLeaveListener extends PlayerListener
{
	public void onPartyLeave(Player player);
}
