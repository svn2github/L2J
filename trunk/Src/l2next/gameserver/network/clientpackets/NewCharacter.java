package l2next.gameserver.network.clientpackets;

import l2next.gameserver.network.serverpackets.NewCharacterSuccess;

public class NewCharacter extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		sendPacket(new NewCharacterSuccess());
	}
}