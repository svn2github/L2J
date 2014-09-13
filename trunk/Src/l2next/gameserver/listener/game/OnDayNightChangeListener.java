package l2next.gameserver.listener.game;

import l2next.gameserver.listener.GameListener;

public interface OnDayNightChangeListener extends GameListener
{
	public void onDay();

	public void onNight();
}
