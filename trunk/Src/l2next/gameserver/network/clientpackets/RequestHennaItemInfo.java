package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.xml.holder.HennaHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.HennaItemInfo;
import l2next.gameserver.templates.Henna;

public class RequestHennaItemInfo extends L2GameClientPacket
{
	// format cd
	private int _symbolId;

	@Override
	protected void readImpl()
	{
		_symbolId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		Henna henna = HennaHolder.getInstance().getHenna(_symbolId);
		if(henna != null)
		{
			player.sendPacket(new HennaItemInfo(henna, player));
		}
	}
}