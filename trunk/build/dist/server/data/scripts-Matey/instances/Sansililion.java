package instances;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExSendUIEvent;
import l2next.gameserver.network.serverpackets.components.NpcString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;

/**
 * Инстанс Родильня
 * @Author Awakeninger
 */
public class Sansililion extends Reflection
{
	private static final Logger _log = LoggerFactory.getLogger(Sansililion.class);
	public long _startedTime = 0;
	public long _endTime = 0;
	public int _points;
	public int _lastBuff = 0;
	private static int _status = 0;
	private ScheduledFuture<?> _updateUITask;
	private ScheduledFuture<?> _stopInstance;

	public void startWorld()
	{
		_status = 1;
		_startedTime = System.currentTimeMillis();
		_endTime = _startedTime + 1800000L;
		_stopInstance = ThreadPoolManager.getInstance().schedule(new StopInstance(), 1800000L);
		_updateUITask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new updateUITask(), 100L, 1000L);
	}
	
	public void setPoints(int p)
	{
		_points = p;
	}
	
	public int getPoints()
	{
		return _points;
	}

	public void updateTimer()
	{
		int timerStatus = 3;
		if(_status == 2)
			timerStatus = 1;

		int timeLeft = (int)((_endTime - System.currentTimeMillis()) / 1000L);
		timeLeft = Math.max(0, timeLeft);
		for(Player player : getPlayers())
		{
			//player.sendPacket(new ExSendUIEvent(player, timerStatus, timeLeft, _points, 60, NpcString.ELAPSED_TIME__));
			player.sendPacket(new ExSendUIEvent(player, 1, 0, timeLeft, 0, 60, NpcString.ELAPSED_TIME__));
			player.sendPacket(new ExSendUIEvent(player, timerStatus, getPoints(), timeLeft, _points/**очки почему то не динамичные*/, 60, NpcString.ELAPSED_TIME__));
		}
	}

	protected class updateUITask extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			if(this == null)
				return;
			updateTimer();
		}
	}

	protected class StopInstance extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			if(this == null)
				return;

			if(_status == 1)
			{
				_status = 2;
				for(NpcInstance npc : getNpcs())
				{
					if(npc.getNpcId() == 33152)
						continue;
					npc.deleteMe();
				}

				if(_updateUITask != null)
				{
					_updateUITask.cancel(false);
					_updateUITask = null;
				}
			}
		}
	}

	public int getStatus()
	{
		return _status;
	}
}