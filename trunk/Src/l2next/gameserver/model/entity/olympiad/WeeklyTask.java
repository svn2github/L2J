package l2next.gameserver.model.entity.olympiad;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class WeeklyTask extends RunnableImpl
{
	private static final Logger _log = LoggerFactory.getLogger(WeeklyTask.class);

	@Override
	public void runImpl() throws Exception
	{
		Olympiad.doWeekTasks();
		_log.info("Olympiad System: Added weekly points to nobles");

		Calendar nextChange = Calendar.getInstance();
		Olympiad._nextWeeklyChange = nextChange.getTimeInMillis() + Config.ALT_OLY_WPERIOD;
	}
}