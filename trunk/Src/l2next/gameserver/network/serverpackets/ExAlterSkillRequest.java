package l2next.gameserver.network.serverpackets;

// Это кнопка связана с комбо ударами!или ударами в прыжке!судя по мувикам
public class ExAlterSkillRequest extends L2GameServerPacket
{

	int nextSkillId;
	int currentSkillId;
	int alterTime;

	public ExAlterSkillRequest(int nextSkillId, int currentSkillId, int alterTime)
	{
		this.nextSkillId = nextSkillId;
		this.currentSkillId = currentSkillId;
		this.alterTime = alterTime;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(nextSkillId);
		writeD(currentSkillId);
		writeD(alterTime);
	}
}