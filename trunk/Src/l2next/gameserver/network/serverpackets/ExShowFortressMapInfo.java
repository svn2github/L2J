package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.entity.events.impl.FortressSiegeEvent;
import l2next.gameserver.model.entity.residence.Fortress;

/**
 * @author VISTALL
 */
public class ExShowFortressMapInfo extends L2GameServerPacket
{
	private int _fortressId;
	private boolean _fortressStatus;
	private boolean[] _commanders;

	public ExShowFortressMapInfo(Fortress fortress)
	{
		_fortressId = fortress.getId();
		_fortressStatus = fortress.getSiegeEvent().isInProgress();

		FortressSiegeEvent siegeEvent = fortress.getSiegeEvent();
		_commanders = siegeEvent.getBarrackStatus();
	}

	@Override
	protected final void writeImpl()
	{

		writeD(_fortressId);
		writeD(_fortressStatus);
		writeD(_commanders.length);
		for(boolean b : _commanders)
		{
			writeD(b);
		}
	}
}