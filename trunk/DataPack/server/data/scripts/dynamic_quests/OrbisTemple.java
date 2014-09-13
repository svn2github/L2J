package dynamic_quests;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.instancemanager.SpawnManager;
import l2next.gameserver.listener.actor.OnKillListener;
import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.quest.dynamic.*;
import l2next.gameserver.network.serverpackets.ExDynamicQuestPacket;
import l2next.gameserver.network.serverpackets.ExDynamicQuestPacket.DynamicQuestInfo;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.utils.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Awakeninger
 * @Date: 21.05.2013
 * @Reworked 12.08.2013
 * TODO[A]: Найти корректные идентификаторы квестов. (201-206)?
 * TODO[A]: Спаун рб/открытие дверей в комнатах 3его этажа
 * TODO[A]: Когда закончу основную механику, сделать разделение очков мобов по стадиям.
 **/
public class OrbisTemple extends DynamicQuest implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(OrbisTemple.class);

	private static final int QUEST_ID = 2;
	private static final int MIN_LEVEL = 95;
	private static final int MAX_LEVEL = 99;
	private static final String SPAWN_GROUP = "Orbis";
	private static final int DURATION = 30*60; //WTF?!
	private static final String START_TIME = "* * * * *";
	private static final int REWARD = 32604;
	private static final int ELITE_REWARD = 32605;
	private static final int KILL_OTOH_MOB = 201;
	private static final int MAX_TASK_POINT = 42000;
	private static final String QUEST_ZONE_FIRST_SECOND = "[orbis_temple_1_2]";
	private static final String QUEST_ZONE_THIRD = "[orbis_temple_3]";
	private ZoneListener _zoneListener;
	private Zone zoneFirstSecond;
	private Zone zoneThird;
	private static final long delayS = 1 * 60 * 1000L;
	private static final long delayB = 5 * 60 * 1000L;
	private final KillListenerImpl _killListener = new KillListenerImpl();
	private static final int ORBIS1 = 22911; // 
	private static final int ORBIS2 = 22912; // 
	private static final int ORBIS3 = 22913; // 
	private static final int ORBIS4 = 22914; // 
	private static final int ORBIS5 = 22915; // 
	private static final int ORBIS6 = 22916; // 
	private static final int ORBIS7 = 22917; // 
	private static final int ORBIS8 = 22918; // 
	private static final int ORBIS9 = 22919; // 
	private static final int ORBIS10 = 22920; // 
	private static final int ORBIS11 = 25833; // 
	private static final int ORBIS12 = 18979; // 
	private static final int ORBIS13 = 25834; // 
	private static final int ORBIS14 = 18980; // 
	private static final int ORBIS15 = 25835; // 
	private static final int ORBIS16 = 22921; // 
	private static final int ORBIS17 = 22922; // 
	private static final int ORBIS18 = 22923; // 
	private static final int ORBIS19 = 22924; // 
	private static final int ORBIS20 = 22925; // 
	private static final int ORBIS21 = 22926; // 
	private static final int ORBIS22 = 22927; //
	private int _stage;  //
	private int _taskIdent;  //
	private static final int[] LOH_MOBS = {
		22911,
		22912,
		22913,
		22914,
		22915,
		22916,
		22917,
		22918,
		22919,
		22920,
		25833,
		18979,
		25834,
		18980,
		25835
	};

	/**
	 * Конструктор класса DynamicQuest являющийся суперклассом
	 *
	 * @param questId  - идентификатор квеста
	 * @param duration - продолжительность квеста (в секундах)
	 */
	public OrbisTemple()
	{
		super(QUEST_ID, DURATION);
		if(getStage() != 1 && getStage() != 2 && getStage() != 3 && getStage() != 4 && getStage() != 5 && getStage() != 6)
		{
			setStage(1);
		}
		else
		{
			setStage(getStage());
		}
		checkTaskIdent();
		switch(getStage())
		{
			case 1:
				addTask(getTaskIdent(), 42000, TASK_INCREASE_MODE_NO_LIMIT);
				break;
			case 2:
				addTask(getTaskIdent(), 60000, TASK_INCREASE_MODE_NO_LIMIT);
				break;
			case 3:
				addTask(getTaskIdent(), 90000, TASK_INCREASE_MODE_NO_LIMIT);
				break;
			case 4:
				addTask(getTaskIdent(), 42000, TASK_INCREASE_MODE_NO_LIMIT);
				break;
			case 5:
				addTask(getTaskIdent(), 60000, TASK_INCREASE_MODE_NO_LIMIT);
				break;
			case 6:
				addTask(getTaskIdent(), 90000, TASK_INCREASE_MODE_NO_LIMIT);
				break;
		}
		addReward(REWARD, 1);
		addEliteReward(ELITE_REWARD, 1, 3);
		addLevelCheck(MIN_LEVEL, MAX_LEVEL);
		addZoneCheck(QUEST_ZONE_FIRST_SECOND);
		addZoneCheck(QUEST_ZONE_THIRD);
		initSchedulingPattern(START_TIME);
	}

	@Override
	protected boolean isZoneQuest()
	{
		return true;
	}

	@Override
	public void onLoad()
	{
		_zoneListener = new ZoneListener();
		zoneFirstSecond = ReflectionUtils.getZone(QUEST_ZONE_FIRST_SECOND);
		zoneFirstSecond.addListener(_zoneListener);
		zoneThird = ReflectionUtils.getZone(QUEST_ZONE_THIRD);
		zoneThird.addListener(_zoneListener);
		_log.info("Dynamic Quest: Loaded quest ID " + QUEST_ID + ". Name: OrbisTemple - Zone Quest. Stage : " + getStage() + ". With ident : " + getTaskIdent());
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	/**
	 * Вызывается после инициализации квеста (спауны, задачи, отсчет)
	 */
	@Override
	protected void onStart()
	{
		SpawnManager.getInstance().spawn(SPAWN_GROUP);
		_log.info("Dynamic Quest: Loaded quest ID " + QUEST_ID + ". Name: OrbisTemple - Zone Quest. Stage : " + getStage() + ". With ident : " + getTaskIdent());
		for(Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if(player.isInZone(QUEST_ZONE_FIRST_SECOND) || player.isInZone(QUEST_ZONE_THIRD))
			{
				if(!getParticipants().contains(player.getObjectId()))
				{
					DynamicQuestInfo questInfo = new DynamicQuestInfo(1);
					questInfo.questType = isZoneQuest() ? 1 : 0;
					questInfo.questId = getQuestId();
					questInfo.step = getCurrentStep();
					player.sendPacket(new ExDynamicQuestPacket(questInfo));
					addParticipant(player);
				}
			}
		}
	}

	/**
	 * Вызывается после завершения квеста (деспаун, статистика),
	 * но перед 5 минутным отсчетом до полного завершения квеста
	 * success - флаг завершения квеста.
	 * Если квест завершился по истечению времени, и задачи не выполнены - success = false
	 * Если квеста завершился по выполнению всех задач, success = true
	 *
	 * @param success - флаг с которым завершился квест
	 */
	@Override
	protected void onStop(boolean success)
	{
		//SpawnManager.getInstance().despawn(SPAWN_GROUP);
		for(int objectId : getParticipants())
		{
			Player player = GameObjectsStorage.getPlayer(objectId);
			if(player != null)
			{
				removeParticipant(player);
			}
		}
	}

	/**
	 * Вызывается при полной остановке квеста, (после 5 минутного отсчета)
	 * <p/>
	 * Здесь удобно очищать переменные, устанавливать параметрам значения по умолчанию,
	 * производить подготовку к новому запуску квеста
	 */
	@Override
	protected void onFinish()
	{
		SpawnManager.getInstance().despawn(SPAWN_GROUP);
		switch(getStage())
		{
			case 1:
				setStage(2);
				break;
			case 2:
				setStage(3);
				break;
			case 3:
				setStage(4);
				break;
			case 4:
				setStage(5);
				break;
			case 5:
				setStage(6);
				break;
			case 6:
				setStage(1);
				break;
		}
		checkTaskIdent();
	}
		
		
	private class ChangeStage extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			switch(getStage())
			{
				case 1:
					DynamicQuestController.getInstance().startQuest(QUEST_ID,getTaskIdent());
					break;
				case 2:
					DynamicQuestController.getInstance().startQuest(QUEST_ID,getTaskIdent());
					addTask(getTaskIdent(), 60000, TASK_INCREASE_MODE_NO_LIMIT);
					break;
				case 3:
					DynamicQuestController.getInstance().startQuest(QUEST_ID,getTaskIdent());
					break;
				case 4:
					DynamicQuestController.getInstance().startQuest(QUEST_ID,getTaskIdent());
					break;
				case 5:
					DynamicQuestController.getInstance().startQuest(QUEST_ID,getTaskIdent());
					break;
				case 6:
					DynamicQuestController.getInstance().startQuest(QUEST_ID,getTaskIdent());
					break;
			}
		}
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
					return "dc0002_01_start001.htm";
				}
				else
				{
					return "dc0002_01_context001.htm";
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
					return "dc0002_01_reward001.htm";
				}
			}
			else
			{
				return "dc0002_01_failed001.htm";
			}
		}
		return null;
	}

	/**
	 * Вызывается при входе игрока в мир (только если квест не завершен окончательно)
	 *
	 * @param player - вошедший игрок
	 */
	@Override
	protected boolean onPlayerEnter(Player player)
	{
		if(player.isInZone(zoneFirstSecond) || player.isInZone(zoneThird))
		{
			return true;
		}
		return false;
	}

	/**
	 * Вызывается при завершении одной из задач
	 *
	 * @param taskId - завершенная задача
	 */
	@Override
	protected void onTaskCompleted(int taskId)
	{
		onStop(true);
		ThreadPoolManager.getInstance().schedule(new ChangeStage(), 300 * 1000);
	}

	@Override
	protected String onDialogEvent(String event, Player player)
	{
		String response = null;
		if(event.equals("Reward"))
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

	private final class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature character)
		{
			if(zone == null)
			{
				return;
			}

			if(!character.isPlayer())
			{
				return;
			}

			Player player = character.getPlayer();
			if(isStarted() && !isSuccessed())
			{
				if(!getParticipants().contains(player.getObjectId()))
				{
					addParticipant(player);
				}
				else
				{
					sendQuestInfoParticipant(player);
				}
			}
		}

		@Override
		public void onZoneLeave(Zone zone, Creature character)
		{
			if(!character.isPlayer())
			{
				return;
			}

			Player player = character.getPlayer();
			if(isStarted() && !isSuccessed())
			{
				if(getParticipants().contains(player.getObjectId()))
				{
					DynamicQuestInfo questInfo = new DynamicQuestInfo(1);
					questInfo.questType = isZoneQuest() ? 1 : 0;
					questInfo.questId = getQuestId();
					questInfo.step = getCurrentStep();
					player.sendPacket(new ExDynamicQuestPacket(questInfo));
				}
			}
		}
	}

	/**
	 * Увеличивает очки выполнения задачи taskId на значение points
	 *
	 * @param taskId - Идентификатор задачи
	 * @param player - Игрок, в чей зачет пойдут очки выполнения
	 * @param points - Кол-во очков
	 */
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
					case ORBIS1:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS2:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS3:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS4:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS5:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS6:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS7:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS8:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
						break;
					case ORBIS9:
						increaseTaskPoint(getTaskIdent(), actor.getPlayer(), 3000);
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
	
	private void setStage(int stage)
	{
		_stage = stage;
	}
	
	private int getStage()
	{
		return _stage;
	}
	
	private void setTaskIdent(int taskIdent)
	{
		_taskIdent = taskIdent;
	}
	
	private int getTaskIdent()
	{
		return _taskIdent;
	}
	
	private void checkTaskIdent()
	{
		switch(getStage())
		{
			case 1:
				setTaskIdent(201);
				break;
			case 2:
				setTaskIdent(202);
				break;
			case 3:
				setTaskIdent(203);
				break;
			case 4:
				setTaskIdent(204);
				break;
			case 5:
				setTaskIdent(205);
				break;
			case 6:
				setTaskIdent(206);
				break;
		}
	}
}