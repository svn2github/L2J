package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.templates.Henna;

import java.util.ArrayList;
import java.util.List;

public class HennaUnequipList extends L2GameServerPacket
{
	private int _emptySlots;
	private long _adena;
	private List<Henna> availHenna = new ArrayList<Henna>(3);

	public HennaUnequipList(Player player)
	{
		_adena = player.getAdena();
		_emptySlots = player.getHennaEmptySlots();
		for(int i = 1; i <= 3; i++)
		{
			if(player.getHenna(i) != null)
			{
				availHenna.add(player.getHenna(i));
			}
		}
	}

	@Override
	protected final void writeImpl()
	{

		writeQ(_adena);
		writeD(_emptySlots);
		writeD(availHenna.size());
		for(Henna henna : availHenna)
		{
			writeD(henna.getSymbolId()); // symbolid
			writeD(henna.getDyeId()); // itemid of dye
			writeQ(henna.getDrawCount());
			writeQ(henna.getPrice());
			writeD(1); // meet the requirement or not
		}
	}
}