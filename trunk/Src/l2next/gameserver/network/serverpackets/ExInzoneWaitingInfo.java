package l2next.gameserver.network.serverpackets;

import javolution.util.FastMap;
import l2next.gameserver.model.Player;
import l2next.gameserver.instancemanager.InstanceHistoryManager;

import java.util.Map;

public class ExInzoneWaitingInfo extends L2GameServerPacket
{
	private int _currentInzoneID = -1;
	private long instReenter = 0;
	Map<Integer, Integer> _instanceTimes;
	private int instRe;
	

	public ExInzoneWaitingInfo(Player player)
	{
		_instanceTimes = new FastMap<Integer, Integer>();
		_currentInzoneID = InstanceHistoryManager.getInstance().instId();	
		instReenter = InstanceHistoryManager.getInstance().instReenter();	
	}

	@Override
	protected void writeImpl()
	{
		writeD(_currentInzoneID);
		writeD(_instanceTimes.size());
		for(int instanceId : _instanceTimes.keySet())
		{
			instReenter = (int) instRe;
			writeD(_currentInzoneID);
			writeD(instRe);
		}
	}
}
