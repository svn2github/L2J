package l2next.gameserver.network.clientpackets;

import l2next.commons.net.Ip;
import l2next.gameserver.Config;
import l2next.gameserver.instancemanager.PremiumIpManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.GameClient.GameClientState;
import l2next.gameserver.network.serverpackets.ActionFail;
import l2next.gameserver.network.serverpackets.CharSelected;
import l2next.gameserver.utils.AutoBan;

public class CharacterSelected extends L2GameClientPacket
{
	private int _charSlot;

	/**
	 * Format: cdhddd
	 */
	@Override
	protected void readImpl()
	{
		_charSlot = readD();
	}

	@Override
	protected void runImpl()
	{
		GameClient client = getClient();

		if(client.getActiveChar() != null)
		{
			return;
		}

		if(Config.SECOND_AUTH_ENABLED && !client.getSecondaryAuth().isAuthed())
		{
			client.getSecondaryAuth().openDialog();
			return;
		}

		int objId = client.getObjectIdForSlot(_charSlot);
		if(AutoBan.isBanned(objId))
		{
			sendPacket(ActionFail.STATIC);
			return;
		}

		Player activeChar = client.loadCharFromDisk(_charSlot);
		if(activeChar == null)
		{
			sendPacket(ActionFail.STATIC);
			return;
		}

		if(activeChar.getAccessLevel() < 0)
		{
			activeChar.setAccessLevel(0);
		}

		if(Config.WindowsCountEnable)
		{
			int i = 0;
			for(Ip ip : World.getIps())
			{
				if(ip.getIp().equalsIgnoreCase(getClient().getIpAddr()))
				{
					i++;
					if(i >= Config.WindowsCount + PremiumIpManager.getInstance().getCountPremiumIps(getClient().getIpAddr()))
					{
						sendPacket(ActionFail.STATIC);
						return;
					}
				}
			}
			World.addIp(getClient().getIpAddr());
		}

		client.setState(GameClientState.IN_GAME);

		sendPacket(new CharSelected(activeChar, client.getSessionKey().playOkID1));
	}
}