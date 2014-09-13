package npc.model;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author pchayka
 */

public final class MaguenInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 5116559538506417574L;

	public MaguenInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		return;
	}
}