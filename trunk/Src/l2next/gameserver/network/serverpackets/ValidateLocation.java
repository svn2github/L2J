package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Creature;
import l2next.gameserver.utils.Location;

/**
 * format dddddd (player id, target id, distance, startx, starty, startz)
 * <p>
 */
public class ValidateLocation extends L2GameServerPacket
{
	private int _chaObjId;
	private Location _loc;

	public ValidateLocation(Creature cha)
	{
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
	}

	@Override
	protected final void writeImpl()
	{

		writeD(_chaObjId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
	}
}