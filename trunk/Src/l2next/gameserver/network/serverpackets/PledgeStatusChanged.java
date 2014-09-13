package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.pledge.Clan;

/**
 * sample 0000: cd b0 98 a0 48 1e 01 00 00 00 00 00 00 00 00 00 ....H........... 0010: 00 00 00 00 00 .....
 * <p/>
 * format ddddd
 */
public class PledgeStatusChanged extends L2GameServerPacket
{
	private int leader_id, clan_id, level, type, crestId, allyId;

	public PledgeStatusChanged(Clan clan)
	{
		leader_id = clan.getLeaderId();
		clan_id = clan.getClanId();
		level = clan.getLevel();
		type = clan.getUnionType();
		crestId = clan.getCrestId();
		allyId = clan.getAllyId();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(type);
		writeD(leader_id);
		writeD(clan_id);
		writeD(crestId);
		writeD(allyId);
		writeD(0);
		writeD(0);
		writeD(0);
	}
}