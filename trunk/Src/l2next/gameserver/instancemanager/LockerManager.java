package l2next.gameserver.instancemanager;

import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Author: Awakeninger
public class LockerManager
{
	private static final Logger _log = LoggerFactory.getLogger(LockerManager.class);
	private static LockerManager _instance;

	public static LockerManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new LockerManager();
		}
		return _instance;
	}

	public LockerManager()
	{
		for(Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if(!player.isAval)
			{
				_log.error("Invalid Licence Key");
				return;
			}
		}
	}

}