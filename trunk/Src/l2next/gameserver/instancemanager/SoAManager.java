package l2next.gameserver.instancemanager;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.ReflectionUtils;
import l2next.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Calendar;

/**
 * @author Awakeninger]
 * @Date: 11.08.2013
 * Используется для регулировки стадий СоА и соответственной смены периодов доступа к Истхине/Б.Истхине
 */
public class SoAManager
{
	private static final Logger _log = LoggerFactory.getLogger(SoAManager.class);
	private static SoAManager _instance;
	private int _stage;
	private int _killedCount;
	private int _killedIsthina;
	private int _mobs;
	private static Reflection ref;

	public static SoAManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new SoAManager();
		}
		return _instance;
	}

	public SoAManager()
	{
		setCurrentStage(getCurrentStage());
		_log.info("SoA Manager: Loaded " + getCurrentStage() + " stage.");
		if(getCurrentStage() == 1)
		{
			_log.info("SoA Manager: Killed " + getKilledCount() + " monsters.");
		}
		else if(getCurrentStage() == 3)
		{
			_log.info("SoA Manager: Isthina was killed " + getKilledIsthina() + " time.");
		}
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new updateState(), 60 * 1000L, 60 * 1000L);
	}

	public void setCurrentStage(int stage)
	{
		_stage = stage;
		ServerVariables.set("SoA_stage", _stage);
		if(_stage == 1) //Убийство мобов в СоА
		switch(_stage)
		{
			case 1:		
			{
				if(getKilledCount() == 1000)
				{
					setCurrentStage(2);
				}
			}
			break;
			case 2://Вторая стадия 2 недели
			{
				ThreadPoolManager.getInstance().schedule(new SecondStateExtendThird(), 1000 * 3600 * 24 * 14);
			}
			break;
			case 3://3 стадия, если убили истхину 10+ раз то 4я, иначе 5я
			{
				if(getKilledIsthina() >= 10)
				{
					if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1)
					{
						setCurrentStage(4);
					}
				}
				else
				{
					if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1)
					{
						setCurrentStage(5);
					}
				}
			}
			break;
			case 4://Экстрим истхина 2 недели offlike
			{
				ThreadPoolManager.getInstance().schedule(new FourthStateExtendFifth(), 1000 * 3600 * 24 * 14);
			}
			break;
			case 5://3 недели, доступна обычная истхина.
			{
				ThreadPoolManager.getInstance().schedule(new FifthStateToFirst(), 1000 * 3600 * 24 * 21);
			}
			break;
		}
	}

	public int getCurrentStage()
	{
		return ServerVariables.getInt("SoA_stage");
	}
	
	public void plusKilledCount(int count)
	{
		_mobs = getKilledCount() + count;
		ServerVariables.set("KilledSOA", _mobs);	
	}
	
	public int getKilledCount()
	{
		return ServerVariables.getInt("KilledSOA");
	}
	
	public void IsthinaDeath(int time)
	{
		_killedIsthina = Math.round(getKilledIsthina() + time);
		ServerVariables.set("KilledIsthinaSOA", _killedIsthina);
	}
	
	public int getKilledIsthina()
	{
		return ServerVariables.getInt("KilledIsthinaSOA");
	}
	
	public class SecondStateExtendThird extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			setCurrentStage(3);
		}
	}
	
	public class FourthStateExtendFifth extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			setCurrentStage(5);
		}
	}
	
	public class FifthStateToFirst extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			setCurrentStage(1);
		}
	}
	
	public class updateState extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			setCurrentStage(getCurrentStage());
		}
	}
}