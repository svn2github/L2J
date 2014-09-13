package l2next.gameserver.network.serverpackets;

public class ExVitalityPointInfo extends L2GameServerPacket
{
	private final int _vitality;

	public ExVitalityPointInfo(int vitality)
	{
		_vitality = vitality;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_vitality);
		writeD(0);
		writeD(0);
		writeD(0);
	}
}