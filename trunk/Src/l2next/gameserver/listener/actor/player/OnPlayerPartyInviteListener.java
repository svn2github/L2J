package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.Player;

public interface OnPlayerPartyInviteListener extends PlayerListener
{
	public void onPartyInvite(Player player);
}
