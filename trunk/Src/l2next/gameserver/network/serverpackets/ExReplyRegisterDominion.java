package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.entity.events.impl.DominionSiegeEvent;
import l2next.gameserver.model.entity.events.impl.SiegeEvent;
import l2next.gameserver.model.entity.residence.Dominion;

/**
 * @author VISTALL
 */
public class ExReplyRegisterDominion extends L2GameServerPacket
{
	private int _dominionId, _clanCount, _playerCount;
	private boolean _success, _join, _asClan;

	public ExReplyRegisterDominion(Dominion dominion, boolean success, boolean join, boolean asClan)
	{
		_success = success;
		_join = join;
		_asClan = asClan;
		_dominionId = dominion.getId();

		DominionSiegeEvent siegeEvent = dominion.getSiegeEvent();

		_playerCount = siegeEvent.getObjects(DominionSiegeEvent.DEFENDER_PLAYERS).size();
		_clanCount = siegeEvent.getObjects(SiegeEvent.DEFENDERS).size() + 1;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_dominionId);
		writeD(_asClan);
		writeD(_join);
		writeD(_success);
		writeD(_clanCount);
		writeD(_playerCount);
	}
}