package npc.model.residences;

import l2next.gameserver.model.Player;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 17:49/13.07.2011
 */
public class TeleportSiegeGuardInstance extends SiegeGuardInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 3814356141829950745L;

	public TeleportSiegeGuardInstance(int objectId, NpcTemplate template)
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

	}
}
