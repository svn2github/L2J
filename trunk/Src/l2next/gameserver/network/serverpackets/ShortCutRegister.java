package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.actor.instances.player.ShortCut;

public class ShortCutRegister extends ShortCutPacket
{
	private ShortcutInfo _shortcutInfo;

	public ShortCutRegister(Player player, ShortCut sc)
	{
		_shortcutInfo = convert(player, sc);
	}

	@Override
	protected final void writeImpl()
	{

		_shortcutInfo.write(this);
	}
}