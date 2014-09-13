package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.pledge.Clan;

public class PledgeInfo extends L2GameServerPacket
{
	private int type;
	private int clan_id;
	private String clan_name, ally_name;

	public PledgeInfo(Clan clan)
	{
		type = clan.getUnionType();
		clan_id = clan.getClanId();
		clan_name = clan.getName();
		ally_name = clan.getAlliance() == null ? "" : clan.getAlliance().getAllyName();
	}

	@Override
	protected final void writeImpl()
	{
		writeD(type);
		writeD(clan_id);
		writeS(clan_name);
		writeS(ally_name);
	}
}