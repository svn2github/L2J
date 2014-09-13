package npc.model.residences.castle;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.residence.Castle;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.CastleSiegeInfo;
import l2next.gameserver.templates.npc.NpcTemplate;

public class CastleMessengerInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -6467755146111198375L;

	public CastleMessengerInstance(int objectID, NpcTemplate template)
	{
		super(objectID, template);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		Castle castle = getCastle();

		if(player.isCastleLord(castle.getId()))
		{
			if(castle.getSiegeEvent().isInProgress())
			{
				showChatWindow(player, "residence2/castle/sir_tyron021.htm");
			}
			else
			{
				showChatWindow(player, "residence2/castle/sir_tyron007.htm");
			}
		}
		else if(castle.getSiegeEvent().isInProgress())
		{
			showChatWindow(player, "residence2/castle/sir_tyron021.htm");
		}
		else
		{
			player.sendPacket(new CastleSiegeInfo(castle, player));
		}
	}
}