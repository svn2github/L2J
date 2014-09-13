package l2next.gameserver.network.serverpackets;

import l2next.gameserver.instancemanager.CursedWeaponsManager;

public class ExCursedWeaponList extends L2GameServerPacket
{
	private int[] cursedWeapon_ids;

	public ExCursedWeaponList()
	{
		cursedWeapon_ids = CursedWeaponsManager.getInstance().getCursedWeaponsIds();
	}

	@Override
	protected final void writeImpl()
	{
		writeDD(cursedWeapon_ids, true);
	}
}