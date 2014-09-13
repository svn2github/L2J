package l2next.gameserver.network.serverpackets;

import l2next.gameserver.data.xml.holder.ResidenceHolder;
import l2next.gameserver.model.entity.residence.Castle;
import l2next.gameserver.model.entity.residence.Residence;

import java.util.Collection;

public class ExSendManorList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		Collection<Castle> residences = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		writeD(residences.size());
		for(Residence castle : residences)
		{
			writeD(castle.getId());
			writeS(castle.getName().toLowerCase());
		}
	}
}