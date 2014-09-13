package l2next.gameserver.network.serverpackets;

import l2next.gameserver.data.xml.holder.ResidenceHolder;
import l2next.gameserver.model.entity.residence.Castle;
import l2next.gameserver.tables.ClanTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExShowCastleInfo extends L2GameServerPacket
{
	private List<CastleInfo> _infos = Collections.emptyList();

	public ExShowCastleInfo()
	{
		String ownerName;
		int id, tax, nextSiege;

		List<Castle> castles = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		_infos = new ArrayList<CastleInfo>(castles.size());
		for(Castle castle : castles)
		{
			ownerName = ClanTable.getInstance().getClanName(castle.getOwnerId());
			id = castle.getId();
			tax = castle.getTaxPercent();
			nextSiege = (int) (castle.getSiegeDate().getTimeInMillis() / 1000);
			_infos.add(new CastleInfo(ownerName, id, tax, nextSiege));
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_infos.size());
		for(CastleInfo info : _infos)
		{
			writeD(info._id);
			writeS(info._ownerName);
			writeD(info._tax);
			writeD(info._nextSiege);
		}
		_infos.clear();
	}

	private static class CastleInfo
	{
		public String _ownerName;
		public int _id, _tax, _nextSiege;

		public CastleInfo(String ownerName, int id, int tax, int nextSiege)
		{
			_ownerName = ownerName;
			_id = id;
			_tax = tax;
			_nextSiege = nextSiege;
		}
	}
}