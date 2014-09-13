package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.items.CrystallizationItem;

import java.util.List;

/**
 * @author ALF
 * @modified KilRoy
 */
public class ExGetCrystalizingEstimation extends L2GameServerPacket
{
	final int _crystalId;
	final long _countCrystals;
	private List<CrystallizationItem> _items;

	public ExGetCrystalizingEstimation(final int crystalId, final long crystalCount, List<CrystallizationItem> itemList)
	{
		_crystalId = crystalId;
		_countCrystals = crystalCount;
		_items = itemList;
	}

	@Override
	protected void writeImpl()
	{
		writeD(1 + _items.size());
		writeD(_crystalId);
		writeQ(_countCrystals);
		writeF(100);

		if(!_items.isEmpty())
		{
			for(CrystallizationItem i : _items)
			{
				writeD(i.getItemId());
				writeQ(i.getCount());
				writeF(i.getChance());
			}
		}
	}
}
