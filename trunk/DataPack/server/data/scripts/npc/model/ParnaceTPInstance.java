package npc.model;

import instances.Baylor;
import instances.Vullock;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ReflectionUtils;

/**
 * @author Awakeninger
 *
 */

public final class ParnaceTPInstance extends NpcInstance
{

	private static final int VullockInstance = 167;
	private static final int BaylorInstance = 168;

	NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());

	public ParnaceTPInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.startsWith("request_vallock"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(VullockInstance))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(VullockInstance))
			{
				ReflectionUtils.enterReflection(player, new Vullock(), VullockInstance);
			}
		}
		else if(command.startsWith("request_Baylor"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(BaylorInstance))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(BaylorInstance))
			{
				ReflectionUtils.enterReflection(player, new Baylor(), BaylorInstance);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}