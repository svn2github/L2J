package l2next.gameserver.taskmanager.tasks;

import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.taskmanager.Task;
import l2next.gameserver.taskmanager.TaskManager;
import l2next.gameserver.taskmanager.TaskManager.ExecutedTask;
import l2next.gameserver.taskmanager.TaskTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskRecom extends Task
{
	private static final Logger _log = LoggerFactory.getLogger(TaskRecom.class);
	private static final String NAME = "sp_recommendations";

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		_log.info("Recommendation Global Task: launched.");
		for(Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.restartRecom();
		}
		_log.info("Recommendation Global Task: completed.");
	}

	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
}