package l2next.gameserver.handler.voicecommands.impl;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.handler.voicecommands.IVoicedCommandHandler;
import l2next.gameserver.loginservercon.LoginServerCommunication;
import l2next.gameserver.loginservercon.SendablePacket;
import l2next.gameserver.loginservercon.gspackets.RequestIsLocIp;
import l2next.gameserver.loginservercon.gspackets.RequestLocIp;
import l2next.gameserver.model.Player;

public class Loc implements IVoicedCommandHandler
{
	private static final String[] commandList = {"loc"};

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		int _objId = activeChar.getObjectId();
		String _login = activeChar.getAccountName();
		String _ip = activeChar.getIP();
		boolean _loc = false;

		if(command.equals("loc"))
		{
			if(target != null)
			{
				if(target.equalsIgnoreCase("on"))
				{
					_loc = true;
				}

				SendablePacket sp = new RequestLocIp(_login, _ip, _loc);
				LoginServerCommunication.getInstance().sendPacket(sp);
				ThreadPoolManager.getInstance().schedule(new CheckLocStatuc(_objId, _login), 1000);
			}
		}
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return commandList;
	}

	class CheckLocStatuc extends RunnableImpl
	{
		private int _objId;
		private String _login;

		public CheckLocStatuc(int objId, String login)
		{
			_objId = objId;
			_login = login;
		}

		@Override
		public void runImpl() throws Exception
		{
			SendablePacket sp = new RequestIsLocIp(_objId, _login);
			LoginServerCommunication.getInstance().sendPacket(sp);
		}

	}
}
