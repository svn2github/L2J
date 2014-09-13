package l2next.gameserver.network.serverpackets;

public class PledgeSkillListAdd extends L2GameServerPacket
{
	private int _skillId;
	private int _skillLevel;

	public PledgeSkillListAdd(int skillId, int skillLevel)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_skillId);
		writeD(_skillLevel);
	}
}