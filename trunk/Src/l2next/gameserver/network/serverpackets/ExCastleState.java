package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.entity.residence.Castle;
import l2next.gameserver.model.entity.residence.ResidenceSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bonux
 */
public class ExCastleState extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(ExCastleState.class);
	private final int _id;
	private final ResidenceSide _side;

	public ExCastleState(Castle castle)
	{
		_id = castle.getId();
		_side = castle.getResidenceSide();
	}

	@Override
	protected void writeImpl()
	{
		writeD(_id);
		writeD(_side.ordinal());

	}
}