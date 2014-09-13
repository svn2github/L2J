package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;
import l2next.gameserver.model.Player;

/**
 * @author : Ragnarok
 * @date : 28.03.12 16:54
 */
public interface OnLevelChangeListener extends PlayerListener
{
	public void onLevelChange(Player player, int oldLvl, int newLvl);
}
