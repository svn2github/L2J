package l2next.gameserver.listener.actor.player;

import l2next.gameserver.listener.PlayerListener;

/**
 * @author VISTALL
 * @date 9:37/15.04.2011
 */
public interface OnAnswerListener extends PlayerListener
{
	void sayYes();

	void sayNo();
}
