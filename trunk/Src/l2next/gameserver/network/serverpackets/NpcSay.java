package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.ChatType;
import l2next.gameserver.network.serverpackets.components.NpcString;

public class NpcSay extends NpcStringContainer
{
	private int _objId;
	private int _type;
	private int _id;

	public NpcSay(NpcInstance npc, ChatType chatType, String text)
	{
		this(npc, chatType, NpcString.NONE, text);
	}

	public NpcSay(NpcInstance npc, ChatType chatType, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objId = npc.getObjectId();
		_id = npc.getNpcId();
		_type = chatType.ordinal();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_objId);
		writeD(_type);
		writeD(1000000 + _id);
		writeElements();
	}
}