package l2next.gameserver.network.serverpackets;

public class StopAllianceWar extends L2GameServerPacket
{
	private String _allianceName;
	private String _char;

	public StopAllianceWar(String alliance, String charName)
	{
		_allianceName = alliance;
		_char = charName;
	}

	@Override
	protected final void writeImpl()
	{
		writeS(_allianceName);
		writeS(_char);
	}
}