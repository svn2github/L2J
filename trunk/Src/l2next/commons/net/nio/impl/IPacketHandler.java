package l2next.commons.net.nio.impl;

import java.nio.ByteBuffer;

public interface IPacketHandler<T extends MMOClient>
{
	public ReceivablePacket<T> handlePacket(ByteBuffer buf, T client);
}