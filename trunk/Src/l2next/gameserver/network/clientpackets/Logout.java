package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;

public class Logout extends L2GameClientPacket
{
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

		// Dont allow leaving if player is fighting
		if(activeChar.isInCombat())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_EXIT_THE_GAME_WHILE_IN_COMBAT);
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isFishing())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING_2);
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isBlocked() && !activeChar.isFlying()) // Разрешаем
		// выходить из
		// игры если
		// используется
		// сервис
		// HireWyvern.
		// Вернет в
		// начальную
		// точку.
		{
			activeChar.sendMessage(new CustomMessage("l2next.gameserver.clientpackets.Logout.OutOfControl", activeChar));
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage(new CustomMessage("l2next.gameserver.clientpackets.Logout.Olympiad", activeChar));
			activeChar.sendActionFailed();
			return;
		}

		if(activeChar.isInObserverMode())
		{
			activeChar.sendMessage(new CustomMessage("l2next.gameserver.clientpackets.Logout.Observer", activeChar));
			activeChar.sendActionFailed();
			return;
		}

		World.removeIp(getClient().getIpAddr());

		activeChar.kick();
	}
}