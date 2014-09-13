package npc.model;

import instances.FortunaInstance;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ReflectionUtils;

/**
 * @author cruel
 */
public final class IzaelManagerInstance extends NpcInstance
{
	private static final int fortunaId = 179;

	public IzaelManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}
		if(command.equalsIgnoreCase("enter"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(179))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(179))
			{
				ReflectionUtils.enterReflection(player, new FortunaInstance(), 179);
			}
		}
		super.onBypassFeedback(player, command);
	}
}