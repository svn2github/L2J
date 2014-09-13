package l2next.gameserver.network.clientpackets.Test_packet;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.ExBR_NewIConCashBtnWnd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class d0de extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(d0de.class);
	private int read;
	@Override
	protected void readImpl() throws Exception
	{
		read = readD();
	}

	@Override
	protected void runImpl() throws Exception
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		
		activeChar.sendPacket(new ExBR_NewIConCashBtnWnd(read));
		_log.info("Packet 0xDE (chd) : d = " + read);
	}
}
