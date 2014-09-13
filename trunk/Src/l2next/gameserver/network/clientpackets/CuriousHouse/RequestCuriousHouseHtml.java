package l2next.gameserver.network.clientpackets.CuriousHouse;

//import l2next.gameserver.cache.HtmCache;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.scripts.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCuriousHouseHtml extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestCuriousHouseHtml.class);

	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		if (activeChar != null)// && (ChaosFestival.getInstance().canParticipate(activeChar)) && (ChaosFestival.getInstance().getStatus() != ChaosFestival.ChaosFestivalStatus.SCHEDULED))
		{
			Functions.show("default/chaos_festival_invitation.htm", activeChar, null);
		}
		_log.info("[IMPLEMENT ME!] RequestCuriousHouseHtml (maybe trigger)");
	}
}

