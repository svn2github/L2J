package l2next.gameserver.instancemanager;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.EventTrigger;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.components.NpcString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArcanManager
{
	private static final Logger _log = LoggerFactory.getLogger(ArcanManager.class);
	private static ArcanManager _instance;
	private static final long _taskDelay = 30 * 60 * 1000L; // 30min
	private static int _Stage = 0;
	private static int _BLUE = 262001;
	private static int _RED = 262003;

	public static ArcanManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new ArcanManager();
		}
		return _instance;
	}

	public ArcanManager()
	{
		setStage(_BLUE);
		SpawnManager.getInstance().despawn("magmeld_ritual");
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new ChangeStage(), _taskDelay, _taskDelay);
	}

	private class ChangeStage extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			if(getStage() == _RED)
			{
				setStage(_BLUE);
				broadcastPacket(_BLUE, true, false);
				broadcastPacket(_RED, false, false);
				SpawnManager.getInstance().despawn("magmeld_ritual");
			}
			else
			{
				setStage(_RED);
				broadcastPacket(_RED, true, true);
				broadcastPacket(_BLUE, false, false);
				SpawnManager.getInstance().spawn("magmeld_ritual");
			}
		}
	}

	public void broadcastPacket(int value, boolean b, boolean message)
	{
		L2GameServerPacket trigger = new EventTrigger(value, b);
		for(Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(trigger);
		}

		if(message)
		{
			L2GameServerPacket sm = new ExShowScreenMessage(NpcString.DARK_POWER_SEEPS_OUT_FROM_THE_MIDDLE_OF_THE_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true);
			for(Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				player.sendPacket(sm);
			}
		}
	}

	public static int getStage()
	{
		return _Stage;
	}

	public void setStage(int s)
	{
		_Stage = s;
	}
}