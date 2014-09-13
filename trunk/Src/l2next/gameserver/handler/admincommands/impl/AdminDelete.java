package l2next.gameserver.handler.admincommands.impl;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.handler.admincommands.IAdminCommandHandler;
import l2next.gameserver.model.GameObject;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Spawner;
import l2next.gameserver.model.instances.NpcInstance;
import org.apache.commons.lang3.math.NumberUtils;

public class AdminDelete implements IAdminCommandHandler
{
	private static enum Commands
	{
		admin_delete
	}

	@Override
	public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;

		if(!activeChar.getPlayerAccess().CanEditNPC)
		{
			return false;
		}

		switch(command)
		{
			case admin_delete:
				GameObject obj = wordList.length == 1 ? activeChar.getTarget() : GameObjectsStorage.getNpc(NumberUtils.toInt(wordList[1]));
				if(obj != null && obj.isNpc())
				{
					NpcInstance target = (NpcInstance) obj;
					target.deleteMe();

					Spawner spawn = target.getSpawn();
					if(spawn != null)
					{
						spawn.stopRespawn();
					}

					// TODO SimpleSpawner
					// SpawnTable.getInstance().deleteSpawn(spawn);
				}
				else
				{
					activeChar.sendPacket(Msg.INVALID_TARGET);
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