package l2next.gameserver.model.entity.olympiad;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.Announcements;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.cache.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CompStartTask extends RunnableImpl
{
	private static final Logger _log = LoggerFactory.getLogger(CompStartTask.class);

	@Override
	public void runImpl() throws Exception
	{
		if(Olympiad.isOlympiadEnd())
		{
			return;
		}

		Olympiad._manager = new OlympiadManager();
		Olympiad._inCompPeriod = true;

		new Thread(Olympiad._manager).start();

		ThreadPoolManager.getInstance().schedule(new CompEndTask(), Olympiad.getMillisToCompEnd());

		Announcements.getInstance().announceToAll(Msg.THE_OLYMPIAD_GAME_HAS_STARTED);
		_log.info("Olympiad System: Olympiad Game Started");
	}
}