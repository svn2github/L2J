package npc.model;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.entity.residence.Castle;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.network.serverpackets.NpcSay;
import l2next.gameserver.network.serverpackets.components.ChatType;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.tables.ClanTable;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.HtmlUtils;

public class GeroldInstance extends NpcInstance
{
	private static Castle _castle;
	public static final long serialVersionUID = 1L;
	private GArray<Creature> target = new GArray<Creature>();
	private Zone zone;
	//Castle _castle = ResidenceHolder.getInstance().getResidence(zone.getTemplate().getIndex());

	public GeroldInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}
		if(command.equalsIgnoreCase("gift"))
		{
			target.add(player);
			Skill skill = SkillTable.getInstance().getInfo(19036, 1);
			if(skill != null)
			{
				callSkill(skill, target, false);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}

	public void showChatWindow(Player player, int val, Object[] replace)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);

		Castle castle = getCastle(player);
		//if(castle != null && castle.getId() > 0)
		if(castle.getId() > 0)
		{
			Clan clan = ClanTable.getInstance().getClan(castle.getOwnerId());
			if(clan != null)
			{
				html.setFile("custom/gerold/index.htm");
				html.replace("%castle%", HtmlUtils.htmlResidenceName(castle.getId()));
				html.replace("%clanName%", clan.getName());
				html.replace("%leaderName%", clan.getLeaderName());
				//return;
			}
			else
			{
				html.setFile("custom/gerold/index_no_clan.htm");
			}
			player.sendPacket(html);
			player.sendPacket(new NpcSay(this, ChatType.TELL, NpcString.WHEN_THE_WORLD_PLUNGES_INTO_CHAOS_WE_WILL_NEED_YOUR_HELP_AT_THAT_TIME_PLEASE_JOIN_IN_WITH_US_I_HOPE_THAT_YOU_WILL_BECOME_STRONGER, new String[0]));
		}
	}
}
