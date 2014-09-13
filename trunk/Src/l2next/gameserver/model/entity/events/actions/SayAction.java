package l2next.gameserver.model.entity.events.actions;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.events.EventAction;
import l2next.gameserver.model.entity.events.GlobalEvent;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.Say2;
import l2next.gameserver.network.serverpackets.components.ChatType;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.network.serverpackets.components.SysString;
import l2next.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author VISTALL
 * @date 22:25/05.01.2011
 */
public class SayAction implements EventAction
{
	private int _range;
	private ChatType _chatType;

	private String _how;
	private NpcString _text;

	private SysString _sysString;
	private SystemMsg _systemMsg;

	protected SayAction(int range, ChatType type)
	{
		_range = range;
		_chatType = type;
	}

	public SayAction(int range, ChatType type, SysString sysString, SystemMsg systemMsg)
	{
		this(range, type);
		_sysString = sysString;
		_systemMsg = systemMsg;
	}

	public SayAction(int range, ChatType type, String how, NpcString string)
	{
		this(range, type);
		_text = string;
		_how = how;
	}

	@Override
	public void call(GlobalEvent event)
	{
		GArray<Player> players = event.broadcastPlayers(_range);
		for(Player player : players)
		{
			packet(player);
		}
	}

	private void packet(Player player)
	{
		if(player == null)
		{
			return;
		}

		L2GameServerPacket packet = null;
		if(_sysString != null)
		{
			packet = new Say2(0, _chatType, _sysString, _systemMsg);
		}
		else
		{
			packet = new Say2(0, _chatType, _how, _text);
		}

		player.sendPacket(packet);
	}
}
