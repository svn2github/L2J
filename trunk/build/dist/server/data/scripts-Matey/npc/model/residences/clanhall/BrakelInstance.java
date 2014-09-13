package npc.model.residences.clanhall;

import l2next.gameserver.data.xml.holder.ResidenceHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.residence.ClanHall;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.TimeUtils;

/**
 * @author VISTALL
 * @date 18:16/04.03.2011
 */
public class BrakelInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -1288221728377663435L;

	public BrakelInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		ClanHall clanhall = ResidenceHolder.getInstance().getResidence(ClanHall.class, 21);
		if(clanhall == null)
		{
			return;
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile("residence2/clanhall/partisan_ordery_brakel001.htm");
		html.replace("%next_siege%", TimeUtils.toSimpleFormat(clanhall.getSiegeDate().getTimeInMillis()));
		player.sendPacket(html);
	}
}
