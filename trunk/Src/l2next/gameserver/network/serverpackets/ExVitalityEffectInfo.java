package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;

public class ExVitalityEffectInfo extends L2GameServerPacket
{
	private int points;
	private int expBonus;

	public ExVitalityEffectInfo(Player player)
	{
		points = player.getVitality().getPoints();
		expBonus = (int) (player.getVitality().getExpMod() * 100);
	}

	@Override
	protected void writeImpl()
	{

		writeD(points);
		writeD(expBonus);
		// Один из следующих - максимальное, другой - текущее.
		writeD(5);// TODO: Remaining items count
		writeD(5);// TODO: Remaining items count
	}
}