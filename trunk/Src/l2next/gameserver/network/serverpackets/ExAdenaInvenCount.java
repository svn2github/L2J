package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

public class ExAdenaInvenCount extends L2GameServerPacket
{
	public int invsize;
	public long count;

	public ExAdenaInvenCount(Player pl)
	{
		count = pl.getInventory().getAdena();
		invsize = pl.getInventory().getSize();
	}

	@Override
	protected void writeImpl()
	{
		writeQ(count);
		writeD(invsize);
	}
}
