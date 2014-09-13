package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

/**
 * Format: (ch) dc d: character object id c: 1 if won 0 if failed
 */
public class ExFishingEnd extends L2GameServerPacket
{
	private int _charId;
	private boolean _win;

	public ExFishingEnd(Player character, boolean win)
	{
		_charId = character.getObjectId();
		_win = win;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_charId);
		writeC(_win ? 1 : 0);
	}
}