package l2next.gameserver.listener.game;

import l2next.gameserver.listener.GameListener;

public interface OnShutdownListener extends GameListener
{
	public void onShutdown();
}
