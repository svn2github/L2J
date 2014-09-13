package l2next.loginserver;

import l2next.commons.net.nio.impl.IPacketHandler;
import l2next.commons.net.nio.impl.ReceivablePacket;
import l2next.loginserver.L2LoginClient.LoginClientState;
import l2next.loginserver.clientpackets.AuthGameGuard;
import l2next.loginserver.clientpackets.RequestAuthLogin;
import l2next.loginserver.clientpackets.RequestServerList;
import l2next.loginserver.clientpackets.RequestServerLogin;

import java.nio.ByteBuffer;

public final class L2LoginPacketHandler implements IPacketHandler<L2LoginClient>
{
	@Override
	public ReceivablePacket<L2LoginClient> handlePacket(ByteBuffer buf, L2LoginClient client)
	{
		int opcode = buf.get() & 0xFF;

		ReceivablePacket<L2LoginClient> packet = null;
		LoginClientState state = client.getState();

		switch(state)
		{
			case CONNECTED:
				if(opcode == 0x07)
				{
					packet = new AuthGameGuard();
				}
				break;
			case AUTHED_GG:
				if(opcode == 0x00)
				{
					packet = new RequestAuthLogin();
				}
				break;
			case AUTHED:
				if(opcode == 0x05)
				{
					packet = new RequestServerList();
				}
				else if(opcode == 0x02)
				{
					packet = new RequestServerLogin();
				}
				break;
			default:
				break;
		}
		return packet;
	}
}