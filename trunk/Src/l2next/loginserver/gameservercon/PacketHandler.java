package l2next.loginserver.gameservercon;

import l2next.loginserver.gameservercon.gspackets.AuthRequest;
import l2next.loginserver.gameservercon.gspackets.BonusRequest;
import l2next.loginserver.gameservercon.gspackets.ChangeAccessLevel;
import l2next.loginserver.gameservercon.gspackets.OnlineStatus;
import l2next.loginserver.gameservercon.gspackets.PingResponse;
import l2next.loginserver.gameservercon.gspackets.PlayerAuthRequest;
import l2next.loginserver.gameservercon.gspackets.PlayerInGame;
import l2next.loginserver.gameservercon.gspackets.PlayerLogout;
import l2next.loginserver.gameservercon.gspackets.PointConnectionLS;
import l2next.loginserver.gameservercon.gspackets.RequestIsLocIp;
import l2next.loginserver.gameservercon.gspackets.RequestLocIp;
import l2next.loginserver.gameservercon.gspackets.SetAccountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class PacketHandler
{
	private static Logger _log = LoggerFactory.getLogger(PacketHandler.class);

	public static ReceivablePacket handlePacket(GameServer gs, ByteBuffer buf)
	{
		ReceivablePacket packet = null;

		int id = buf.get() & 0xff;

		if(!gs.isAuthed())
		{
			switch(id)
			{
				case 0x00:
					packet = new AuthRequest();
					break;
				default:
					_log.error("Received unknown packet: " + Integer.toHexString(id));
			}
		}
		else
		{
			switch(id)
			{
				case 0x01:
					packet = new OnlineStatus();
					break;
				case 0x02:
					packet = new PlayerAuthRequest();
					break;
				case 0x03:
					packet = new PlayerInGame();
					break;
				case 0x04:
					packet = new PlayerLogout();
					break;
				case 0x05:
					packet = new SetAccountInfo();
					break;
				case 0x10:
					packet = new BonusRequest();
					break;
				case 0x11:
					packet = new ChangeAccessLevel();
					break;
				case 0x12:
					packet = new RequestLocIp();
					break;
				case 0x13:
					packet = new RequestIsLocIp();
					break;
				case 0xff:
					packet = new PingResponse();
					break;
				case 0x1d:
					packet = new PointConnectionLS();
					break;
				default:
					_log.error("Received unknown packet: " + Integer.toHexString(id));
			}
		}

		return packet;
	}
}