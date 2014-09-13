package dynamic_quests;

import l2next.gameserver.instancemanager.SpawnManager;
import l2next.gameserver.listener.actor.OnKillListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.quest.dynamic.DynamicQuest;
import l2next.gameserver.scripts.ScriptFile;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Awakeninger
 */
public class SealofShilen extends DynamicQuest implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(SealofShilen.class);

	private static final int QUEST_ID = 9;

	private static final int MIN_LEVEL = 95;
	private static final int MAX_LEVEL = 99;

	private static final int DURATION = 10;
	private static final String START_TIME1 = "00 2 * * Fri"; //Выставить несколько
	private static final String START_TIME2 = "00 2 * * Sat"; //Выставить несколько
	private static final String START_TIME3 = "00 2 * * Sun"; //Выставить несколько
	private static final String START_TIME11 = "00 14 * * Fri"; //Выставить несколько
	private static final String START_TIME21 = "00 14 * * Sat"; //Выставить несколько
	private static final String START_TIME31 = "00 14 * * Sun"; //Выставить несколько
	private static final String START_TIME12 = "00 23 * * Fri"; //Выставить несколько
	private static final String START_TIME22 = "00 23 * * Sat"; //Выставить несколько
	private static final String START_TIME32 = "00 23 * * Sun"; //Выставить несколько

	private static final int REWARD = 33501;
	private static final String SPAWN_GROUP = "SealOfShilen"; //Каменный Портал - Вход в Алтарь Шилен
	private static final int KILL_LOH_MOB = 901;//Клиентский параметр
	private static final int MAX_TASK_POINT = 45000; //уточнить

	private final KillListenerImpl _killListener = new KillListenerImpl();
	private static final int mob5 = 23147;
	private static final int mob4 = 23148;
	private static final int mob3 = 23149;
	private static final int mob2 = 23150;
	private static final int mob1 = 23151;
	private static final int[] LOH_MOBS = {
		23147,
		23148,
		23149,
		23150,
		23151
	};

	public SealofShilen()
	{
		super(QUEST_ID, DURATION);
		addTask(KILL_LOH_MOB, MAX_TASK_POINT, TASK_INCREASE_MODE_NO_LIMIT);
		addReward(REWARD, 1);
		addEliteReward(REWARD, 1, 3);
		addLevelCheck(MIN_LEVEL, MAX_LEVEL);
		initSchedulingPattern(START_TIME1);
		initSchedulingPattern(START_TIME2);
		initSchedulingPattern(START_TIME3);
		initSchedulingPattern(START_TIME11);
		initSchedulingPattern(START_TIME21);
		initSchedulingPattern(START_TIME31);
		initSchedulingPattern(START_TIME12);
		initSchedulingPattern(START_TIME22);
		initSchedulingPattern(START_TIME32);

	}

	@Override
	protected boolean isZoneQuest()
	{
		return false;
	}

	@Override
	public void onLoad()
	{
		_log.info("Dynamic Quest: Loaded quest ID " + QUEST_ID + ". Name: SealOfShilen - Campaign");
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	@Override
	protected void onStart()
	{
		SpawnManager.getInstance().despawn(SPAWN_GROUP);
	}

	@Override
	protected void onStop(boolean success)
	{
		for(int objectId : getParticipants())
		{
			Player player = GameObjectsStorage.getPlayer(objectId);
			if(player != null)
			{
				removeParticipant(player);
			}
		}
	}

	@Override
	protected void onFinish()
	{
	}

	@Override
	protected String onRequestHtml(Player player, boolean participant)
	{
		if(getCurrentStep() == 1)
		{
			if(isStarted())
			{
				if(!participant)
				{
					return "dc0009_01_start001.htm";
				}
				else
				{
					return "dc0009_01_context001.htm";
				}
			}
			else if(isSuccessed())
			{
				boolean rewardReceived = rewardReceived(player);
				if(rewardReceived)
				{
					return null;
				}
				else
				{
					return "dc0009_01_reward001.htm";
				}
			}
			else
			{
				return "dc0009_01_failed001.htm";
			}
		}
		return null;
	}

	@Override
	protected boolean onPlayerEnter(Player player)
	{
		if(getParticipants().contains(player.getObjectId()))
		{
			addParticipant(player);
			return true;
		}
		return false;
	}

	@Override
	protected void onTaskCompleted(int taskId)
	{
		SpawnManager.getInstance().spawn(SPAWN_GROUP);
	}

	@Override
	protected String onDialogEvent(String event, Player player)
	{
		String response = null;
		if(event.equals("Accept"))
		{
			addParticipant(player);
			response = "dc0009_context001.htm";
		}
		else if(event.equals("Reward"))
		{
			tryReward(player);
			response = null;
		}
		else if(event.endsWith(".htm"))
		{
			response = event;
		}
		return response;
	}

	@Override
	protected void onAddParticipant(Player player)
	{
		player.getListeners().add(_killListener);
	}

	@Override
	protected void onRemoveParticipant(Player player)
	{
		player.getListeners().remove(_killListener);
	}

	@Override
	protected boolean onStartCondition()
	{
		return true;
	}

	private final class KillListenerImpl implements OnKillListener
	{
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			if(victim.isPlayer())
			{
				return;
			}

			if(!actor.isPlayer())
			{
				return;
			}

			if(victim.isNpc() && isStarted() && ArrayUtils.contains(LOH_MOBS, victim.getNpcId()))
			{
				switch(victim.getNpcId())
				{
					case mob1:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					case mob2:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					case mob3:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					case mob4:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;
					case mob5:
						increaseTaskPoint(KILL_LOH_MOB, actor.getPlayer(), 1);
						break;

				}
			}
		}

		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}
}