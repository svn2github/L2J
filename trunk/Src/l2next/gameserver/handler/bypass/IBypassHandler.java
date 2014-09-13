package l2next.gameserver.handler.bypass;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.NpcInstance;

/**
 * @author VISTALL
 * @date 15:54/12.07.2011
 */
public interface IBypassHandler
{
	String[] getBypasses();

	void onBypassFeedback(NpcInstance npc, Player player, String command);
}
