package l2next.gameserver.network.serverpackets;

import l2next.gameserver.utils.Location;

/**
 * format dddddd
 */
public class Earthquake extends L2GameServerPacket
{
	private Location _loc;
	private int _intensity;
	private int _duration;

	public Earthquake(Location loc, int intensity, int duration)
	{
		_loc = loc;
		_intensity = intensity;
		_duration = duration;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_intensity);
		writeD(_duration);
		writeD(0x00); // Unknown
	}
}