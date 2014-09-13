package l2next.gameserver.instancemanager;

import l2next.commons.threading.RunnableImpl;
import l2next.commons.util.Rnd;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.NpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToIManager
{
	private static final Logger _log = LoggerFactory.getLogger(ToIManager.class);
	private static ToIManager _instance;
	private static final long _taskDelay = 5 * 60 * 1000L; // 5min
	private static int VLADIMIR = 25809;
	private static NpcInstance Vladimir;
	private boolean isSpawnRb1;
	private boolean isSpawnRb2;
	private boolean isSpawnRb3;
	private Location[] LocVladimir = {
		new Location(113768, 16968, -5125, 57343),
		new Location(113784, 15176, -5125, 10747),
		new Location(115528, 15240, -5125, 25521),
		new Location(115464, 16952, -5125, 43018),
		new Location(114072, 16648, -3634, 56189),
		new Location(114040, 15496, -3634, 10250),
		new Location(115272, 15480, -3634, 27131),
		new Location(115240, 16664, -3634, 47093)
	};

	public static ToIManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new ToIManager();
		}
		return _instance;
	}

	public ToIManager()
	{
		Spawn1Rb();
		Spawn2Rb();
		Spawn3Rb();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new ChangeStage(), _taskDelay, _taskDelay);
		_log.info("ToIManager: Loaded " + getCount() + " RaidBoss(es) on spawn point");
	}
	

	private void Spawn1Rb()
	{
		Vladimir = NpcUtils.spawnSingle(VLADIMIR, LocVladimir[Rnd.get(LocVladimir.length)], 5 * 60 * 1000L);
		isSpawnRb1 = true;
	}
	
	private void Spawn2Rb()
	{
		//Vladimir = NpcUtils.spawnSingle(VLADIMIR, LocVladimir[Rnd.get(LocVladimir.length)], 5 * 60 * 1000L);
		//isSpawnRb2 = true;
		//TODO[A]:рб
	}
	
	private void Spawn3Rb()
	{
		//Vladimir = NpcUtils.spawnSingle(VLADIMIR, LocVladimir[Rnd.get(LocVladimir.length)], 5 * 60 * 1000L);
		//isSpawnRb3 = true;
		//TODO[A]:рб
	}
	
	private class ChangeStage extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			Vladimir = NpcUtils.spawnSingle(VLADIMIR, LocVladimir[Rnd.get(LocVladimir.length)], 5 * 60 * 1000L);
			//TODO[A]:Остальных рб
		}
	}
	
	public int getCount()
	{
		if(isSpawnRb1 && isSpawnRb2 && isSpawnRb3)
		{
			return 3;
		}
		else if((isSpawnRb1 && isSpawnRb2) || (isSpawnRb2 && isSpawnRb3))
		{
			return 2;
		}
		else if((isSpawnRb1) || (isSpawnRb2) || (isSpawnRb3))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

}