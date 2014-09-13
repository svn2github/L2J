package l2next.gameserver.network.clientpackets;

import l2next.gameserver.cache.CrestCache;
import l2next.gameserver.model.Player;
import l2next.gameserver.network.serverpackets.PledgeCrest;

public class RequestPledgeCrest extends L2GameClientPacket
{
	private int serverId;
	private int _crestId;

	@Override
	protected void readImpl()
	{
		serverId = readD();
		_crestId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		if(_crestId == 0)
		{
			return;
		}
		byte[] data = CrestCache.getInstance().getPledgeCrest(_crestId);
		if(data != null)
		{
			PledgeCrest pc = new PledgeCrest(_crestId, data);
			sendPacket(pc);
		}
	}
}