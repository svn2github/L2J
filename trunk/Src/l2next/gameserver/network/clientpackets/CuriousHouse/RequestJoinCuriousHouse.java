package l2next.gameserver.network.clientpackets.CuriousHouse;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseEnter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestJoinCuriousHouse extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestJoinCuriousHouse.class);

	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		if (activeChar != null)// && (ChaosFestival.getInstance().canParticipate(activeChar)) && (ChaosFestival.getInstance().getStatus() == ChaosFestival.ChaosFestivalStatus.INVITING))
		{
			//ChaosFestival.getInstance().addMember(activeChar);
		}
		activeChar.sendPacket(new ExCuriousHouseEnter());
		_log.info("[IMPLEMENT ME!] RequestJoinCuriousHouse (maybe trigger)");
	}
}
/*
int __thiscall UNetworkHandler__RequestJoinCuriousHouse(int this)
{
  return (*(int (__cdecl **)(_DWORD, _DWORD, signed int, signed int))(**(_DWORD **)(this + 72) + 108))(
           *(_DWORD *)(this + 72),
           "ch",
           208,
           195);
}
 */
