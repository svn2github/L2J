package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.GameObject;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.RequestActionUse.Action;

/**
 * Listener for social actions performed by player
 *
 * @author Yorie
 */
public interface OnSocialActionListener extends PlayerListener
{
	public void onSocialAction(Player player, GameObject target, Action action);
}