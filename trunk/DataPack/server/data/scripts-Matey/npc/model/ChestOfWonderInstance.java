package npc.model;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author ALF
 * @date 03.09.2012
 */
public class ChestOfWonderInstance extends NpcInstance
{
	private static final long serialVersionUID = 1871608359166390528L;
	private static final int gate = 33691;

	public ChestOfWonderInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public boolean isMovementDisabled()
	{
		return true;
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.equalsIgnoreCase("start"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.DIMENSIONAL_IS_OPEN_NEAR_S1, 6000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, player.getName()));
			//player.sendPacket(new SystemMessage2(NpcString.DIMENSIONAL_IS_OPEN_NEAR_S1).addString(player.getName()));
			NpcInstance ni = getReflection().addSpawnWithoutRespawn(gate, getLoc(), 0);
			this.decayMe();
			this.doDie(null);
		}
	}
}