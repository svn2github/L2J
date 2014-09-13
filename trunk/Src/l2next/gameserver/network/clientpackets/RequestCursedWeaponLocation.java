package l2next.gameserver.network.clientpackets;

import l2next.gameserver.instancemanager.CursedWeaponsManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.CursedWeapon;
import l2next.gameserver.network.serverpackets.ExCursedWeaponLocation;
import l2next.gameserver.network.serverpackets.ExCursedWeaponLocation.CursedWeaponInfo;
import l2next.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;

public class RequestCursedWeaponLocation extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		Creature activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		List<CursedWeaponInfo> list = new ArrayList<CursedWeaponInfo>();
		for(CursedWeapon cw : CursedWeaponsManager.getInstance().getCursedWeapons())
		{
			Location pos = cw.getWorldPosition();
			if(pos != null)
			{
				list.add(new CursedWeaponInfo(pos, cw.getItemId(), cw.isActivated() ? 1 : 0));
			}
		}

		activeChar.sendPacket(new ExCursedWeaponLocation(list));
	}
}