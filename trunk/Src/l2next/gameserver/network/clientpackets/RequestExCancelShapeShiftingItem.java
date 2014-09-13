/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.ExShapeShiftingResult;

public class RequestExCancelShapeShiftingItem extends L2GameClientPacket
{
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		player.setAppearanceStone(null);
		player.setAppearanceExtractItem(null);
		player.sendPacket(ExShapeShiftingResult.FAIL);
	}

	@Override
	protected void readImpl()
	{
	}
}
