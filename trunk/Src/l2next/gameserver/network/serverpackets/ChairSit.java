package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.StaticObjectInstance;

/**
 * format: d
 */
public class ChairSit extends L2GameServerPacket
{
	private int _objectId;
	private int _staticObjectId;

	public ChairSit(Player player, StaticObjectInstance throne)
	{
		_objectId = player.getObjectId();
		_staticObjectId = throne.getUId();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_objectId);
		writeD(_staticObjectId);
	}
}