package npc.model;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExStartScenePlayer;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author Awakeninger
 */

public final class BalrogStoryInstance extends NpcInstance
{

	public BalrogStoryInstance(int objectId, NpcTemplate template)
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

		if(command.startsWith("request_video"))
		{
			player.showQuestMovie(ExStartScenePlayer.SCENE_SI_BARLOG_STORY);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}