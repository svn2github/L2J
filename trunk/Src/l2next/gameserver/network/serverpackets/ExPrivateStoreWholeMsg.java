package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

public class ExPrivateStoreWholeMsg extends L2GameServerPacket
{
	private final int _objectId;
	private final String _msg;

	public ExPrivateStoreWholeMsg(Player player, String msg)
	{
		_objectId = player.getObjectId();
		_msg = msg;
	}

	public ExPrivateStoreWholeMsg(Player player)
	{
		this(player, player.getSellStoreName());
	}
	
	@Override
	protected void writeImpl()
	{
		writeD(_objectId);
		writeS(_msg);
	}
}