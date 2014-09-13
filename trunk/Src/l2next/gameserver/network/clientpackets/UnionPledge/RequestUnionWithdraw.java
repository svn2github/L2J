package l2next.gameserver.network.clientpackets.UnionPledge;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestUnionWithdraw extends L2GameClientPacket
{
	private int unk1;
	@Override
	protected void readImpl()
	{
		unk1 = readD();
	}

	@Override
	protected void runImpl()
	{
		Player cha = getClient().getActiveChar();

		if(cha != null)
		{
			//TODO:
		}
		
		System.out.println("unk1 = " + unk1);
	}
}
