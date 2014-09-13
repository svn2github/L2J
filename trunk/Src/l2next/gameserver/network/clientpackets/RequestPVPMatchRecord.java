package l2next.gameserver.network.clientpackets;

public class RequestPVPMatchRecord extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		System.out.println("Unimplemented packet: " + getType() + " | size: " + _buf.remaining());
	}

	@Override
	protected void runImpl()
	{
	}
}