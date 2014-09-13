package l2next.gameserver.network.serverpackets.components;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author VISTALL
 * @date 13:28/01.12.2010
 */
public interface IStaticPacket
{
	L2GameServerPacket packet(Player player);
}
