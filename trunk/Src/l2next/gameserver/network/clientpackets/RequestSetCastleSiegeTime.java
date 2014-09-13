package l2next.gameserver.network.clientpackets;

import l2next.gameserver.data.xml.holder.ResidenceHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2next.gameserver.model.entity.residence.Castle;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.network.serverpackets.CastleSiegeInfo;
import l2next.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author VISTALL
 */
public class RequestSetCastleSiegeTime extends L2GameClientPacket
{
	private int _id, _time;

	@Override
	protected void readImpl()
	{
		_id = readD();
		_time = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _id);
		if(castle == null)
		{
			return;
		}

		if(player.getClan().getCastle() != castle.getId())
		{
			return;
		}

		if((player.getClanPrivileges() & Clan.CP_CS_MANAGE_SIEGE) != Clan.CP_CS_MANAGE_SIEGE)
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_SIEGE_TIME);
			return;
		}

		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();

		siegeEvent.setNextSiegeTime(_time);

		player.sendPacket(new CastleSiegeInfo(castle, player));
	}
}