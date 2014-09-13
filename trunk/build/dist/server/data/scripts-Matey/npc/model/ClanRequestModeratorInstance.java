package npc.model;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.network.serverpackets.UnionPledge.ExPledgeUnionState;

public class ClanRequestModeratorInstance extends NpcInstance
{

	public ClanRequestModeratorInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.equalsIgnoreCase("guild_1"))
		{
			player.sendPacket(new ExPledgeUnionState(0, 1));
		}
		else if(command.equalsIgnoreCase("guild_2"))
		{
			player.sendPacket(new ExPledgeUnionState(0, 2));
		}
		else if(command.equalsIgnoreCase("guild_3"))
		{
			player.sendPacket(new ExPledgeUnionState(0, 3));
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}