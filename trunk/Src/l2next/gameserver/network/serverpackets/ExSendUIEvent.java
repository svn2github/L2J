package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.components.NpcString;

public class ExSendUIEvent extends NpcStringContainer
{
	private int _objectId;
	private int _style;
	private int _isIncrease;
	private int _startTime;
	private int _endTime;
	private int _points;
	private boolean _UI;
	private int _isDuplicate;

	public ExSendUIEvent(Player player, int style, int isIncrease, int startTime, int endTime, String... params)
	{
		this(player, style, isIncrease, startTime, endTime, NpcString.NONE, params);
	}

	
	public ExSendUIEvent(Player player, int style, int isIncrease, int startTime, int endTime, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objectId = player.getObjectId();
		_style = style;
		_isIncrease = isIncrease;
		_startTime = startTime;
		_endTime = endTime;
	}
	
	public ExSendUIEvent(Player player, int style, int points, int isIncrease, int startTime, int endTime, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objectId = player.getObjectId();
		_style = style;
		_isIncrease = isIncrease;
		_startTime = startTime;
		_endTime = endTime;
		_points = points;
		_UI = true;
	}
	
	public ExSendUIEvent(Player player, int style, int points, int isIncrease, int startTime, int endTime, boolean UI, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objectId = player.getObjectId();
		_style = style;
		_isIncrease = isIncrease;
		_startTime = startTime;
		_endTime = endTime;
		_points = points;
		_UI = UI;
	}

	@Override
	protected void writeImpl()
	{
		if(!_UI)
		{
			writeD(_objectId);
			writeD(_style); //2 как у истхины на баллисте. 4й просто истекшее время. 3й для родильни, но не щелкает, 1 - вообще без таймера.
			writeD(_points); 
			writeD(_points);
			writeS(String.valueOf(_isIncrease));
			writeS(String.valueOf(_startTime / 60));
			writeS(String.valueOf(_startTime % 60));
			writeS(String.valueOf(_endTime / 60));
			writeS(String.valueOf(_endTime % 60));
			writeElements();
		}
		else
		{
			writeD(_objectId);
			writeD(_style);
			writeD(_points);
			writeD(_points);
			writeS(String.valueOf(_isIncrease));
			writeS(String.valueOf(_points));
			writeS(String.valueOf(0));
			writeS(String.valueOf(_endTime));
			writeS(String.valueOf(0));
			writeElements();
		}
		
	}
}