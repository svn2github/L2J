package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.quest.dynamic.DynamicQuestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDynamicQuestHTML extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestDynamicQuestHTML.class);
	private int id;
	private int step;

	@Override
	protected void readImpl()
	{
		id = readD();
		step = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();

		if(player == null)
		{
			return;
		}

		DynamicQuestController.getInstance().requestDynamicQuestHtml(id, step, player);
	}
}