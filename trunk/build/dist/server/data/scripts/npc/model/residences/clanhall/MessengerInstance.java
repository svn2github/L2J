package npc.model.residences.clanhall;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import l2next.gameserver.model.entity.residence.ClanHall;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.CastleSiegeInfo;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 15:54/07.05.2011
 * 35420
 */
public class MessengerInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 4717425322873960906L;
	private String _siegeDialog;
	private String _ownerDialog;

	public MessengerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);

		_siegeDialog = template.getAIParams().getString("siege_dialog");
		_ownerDialog = template.getAIParams().getString("owner_dialog");
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		ClanHall clanHall = getClanHall();
		ClanHallSiegeEvent siegeEvent = clanHall.getSiegeEvent();
		if(clanHall.getOwner() != null && clanHall.getOwner() == player.getClan())
		{
			showChatWindow(player, _ownerDialog);
		}
		else if(siegeEvent.isInProgress())
		{
			showChatWindow(player, _siegeDialog);
		}
		else
		{
			player.sendPacket(new CastleSiegeInfo(clanHall, player));
		}
	}
}
