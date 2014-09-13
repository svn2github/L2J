package l2next.gameserver.handler.admincommands.impl;

import l2next.gameserver.handler.admincommands.IAdminCommandHandler;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;

/**
 * @author KilRoy AddPoints Manipulation //addpoints count <target> DelPoints Manipulation //delpoints count <target>
 */
public class AdminPSPoints implements IAdminCommandHandler
{
	private static enum Commands
	{
		admin_addpoints, admin_delpoints
	}

	@Override
	public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;

		if(!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}

		switch(command)
		{
			case admin_addpoints:
				try
				{
					String targetName = wordList[1];
					Player obj = World.getPlayer(targetName);
					if(obj != null && obj.isPlayer())
					{
						int add = (obj.getNetConnection().getPremiumPoint() + Integer.parseInt(wordList[2]));
						obj.getNetConnection().setPremiumPoint(add);
						activeChar.sendMessage("Player points account set to:" + add);
					}
					else
					{
						activeChar.sendMessage("Player " + targetName + " not found");
					}
				}
				catch(IndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Please specify correct name.");
				}
				break;
			case admin_delpoints:
				try
				{
					String targetName = wordList[1];
					Player obj = World.getPlayer(targetName);
					if(obj != null && obj.isPlayer())
					{
						int reduce = (obj.getNetConnection().getPremiumPoint() - Integer.parseInt(wordList[2]));
						obj.getNetConnection().setPremiumPoint(reduce);
						activeChar.sendMessage("Player points account set to:" + reduce);
					}
					else
					{
						activeChar.sendMessage("Player " + targetName + " not found");
					}
				}
				catch(IndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Please specify correct name.");
				}
				break;
		}

		return true;
	}

	@Override
	public Enum[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}