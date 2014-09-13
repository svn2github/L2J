package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.xml.holder.ResidenceHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.residence.Residence;
import l2next.gameserver.network.serverpackets.CastleSiegeAttackerList;

public class RequestCastleSiegeAttackerList extends L2GameClientPacket
{
	private int _unitId;

	@Override
	protected void readImpl()
	{
		_unitId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		Residence residence = ResidenceHolder.getInstance().getResidence(_unitId);
		if(residence != null)
		{
			sendPacket(new CastleSiegeAttackerList(residence));
		}
	}
}