package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;

public class GMViewQuestInfo extends L2GameServerPacket
{
	private final Player _cha;

	public GMViewQuestInfo(Player cha)
	{
		_cha = cha;
	}

	@Override
	protected final void writeImpl()
	{
		writeS(_cha.getName());

		Quest[] quests = _cha.getAllActiveQuests();

		if(quests.length == 0)
		{
			writeH(0);
			writeH(0);
			return;
		}

		writeH(quests.length);
		for(Quest q : quests)
		{
			writeD(q.getQuestIntId());
			QuestState qs = _cha.getQuestState(q.getName());
			writeD(qs == null ? 0 : qs.getInt("cond"));
		}

		writeH(0); // количество элементов типа: ddQd , как-то связано с
		// предметами
	}
}