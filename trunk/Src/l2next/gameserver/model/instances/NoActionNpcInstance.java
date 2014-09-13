package l2next.gameserver.model.instances;

import l2next.gameserver.model.Player;
import l2next.gameserver.templates.npc.NpcTemplate;

@Deprecated
public class NoActionNpcInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -2398134370204139208L;

	public NoActionNpcInstance(final int objectID, final NpcTemplate template)
	{
		super(objectID, template);
	}

	@Override
	public void onAction(final Player player, final boolean dontMove)
	{
		player.sendActionFailed();
	}
}
