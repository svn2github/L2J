package l2next.gameserver.network.serverpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import java.util.List;
import java.util.Map;

//ExCuriousHouseResult dh(Sddd)
public class ExCuriousHouseResult extends L2GameServerPacket
{
	private Player _activeChar;
	private List<Player> _members;
	private PlayerState _state;
	private Map<Integer, Integer> _killCounters;
	private Map<Integer, Integer> _survivalTimeCounters;
	
	public ExCuriousHouseResult(Player activeChar, List<Player> members, PlayerState state, Map<Integer, Integer> killCounters, Map<Integer, Integer> survivalTimeCounters)
	{
		_activeChar = activeChar;
		_members = members;
		_state = state;
		_killCounters = killCounters;
		_survivalTimeCounters = survivalTimeCounters;
	}
	
	public static enum PlayerState
	{
		TIE, 
		WIN, 
		LOSE;
	}

	@Override
	protected void writeImpl()
	{
		writeD(0);
		writeH(_state.ordinal());
		writeD(_members.size());
		int number = 0;
		for (Player member : _members)
		{
			writeD(member.getObjectId());
			writeD(++number);
			writeD(member.getActiveClassId());
			//writeD(146);
			//writeD(2);
			writeD(_survivalTimeCounters.containsKey(Integer.valueOf(member.getObjectId())) ? ((Integer)_survivalTimeCounters.get(Integer.valueOf(member.getObjectId()))).intValue() : 0);
			writeD(_killCounters.containsKey(Integer.valueOf(member.getObjectId())) ? ((Integer)_killCounters.get(Integer.valueOf(member.getObjectId()))).intValue() : 0);
		}
	}

	@Override
	public String getType()
	{
		return "[S] FE:12A ExCuriousHouseResult";
	}
}


