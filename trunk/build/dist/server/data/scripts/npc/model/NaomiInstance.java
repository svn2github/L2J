package npc.model;

import instances.KimerianNormal;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ReflectionUtils;
import l2next.gameserver.utils.ItemFunctions;

/**
 * @author Awakeninger
 */

public final class NaomiInstance extends NpcInstance
{
	public NaomiInstance(int objectId, NpcTemplate template)
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

		if(command.equalsIgnoreCase("start_normal"))
		{
			ItemFunctions.removeItem(player, 17375, 1, true);
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(161))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(161))
			{
				ReflectionUtils.enterReflection(player, new KimerianNormal(), 161);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}