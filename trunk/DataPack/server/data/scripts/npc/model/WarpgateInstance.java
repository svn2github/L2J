package npc.model;

import l2next.gameserver.instancemanager.HellboundManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author pchayka
 */
public class WarpgateInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1285593374919089575L;

	public WarpgateInstance(int objectId, NpcTemplate template)
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

		if(command.startsWith("enter_hellbound"))
		{
			if(HellboundManager.getHellboundLevel() != 0 && (player.isQuestCompleted("_130_PathToHellbound") || player.isQuestCompleted("_133_ThatsBloodyHot")))
			{
				player.teleToLocation(-11272, 236464, -3248);
				return;
			}
			else
			{
				showChatWindow(player, "default/32318-1.htm");
				return;
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}