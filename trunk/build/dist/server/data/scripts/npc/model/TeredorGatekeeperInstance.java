package npc.model;

import instances.TeredorCavern;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ReflectionUtils;

public final class TeredorGatekeeperInstance extends NpcInstance
{

	private static final long serialVersionUID = 6518350180076969631L;
	private static final int teredorInstanceId = 160;

	public TeredorGatekeeperInstance(int objectId, NpcTemplate template)
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

		if(command.equalsIgnoreCase("teredorenterinst"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(teredorInstanceId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(teredorInstanceId))
			{
				ReflectionUtils.enterReflection(player, new TeredorCavern(), teredorInstanceId);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}