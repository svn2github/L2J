package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.tables.SkillTable;

import java.util.Collection;

public class GMViewSkillInfo extends L2GameServerPacket
{
	private String _charName;
	private Collection<Skill> _skills;
	private Player _targetChar;

	public GMViewSkillInfo(Player cha)
	{
		_charName = cha.getName();
		_skills = cha.getAllSkills();
		_targetChar = cha;
	}

	@Override
	protected final void writeImpl()
	{
		writeS(_charName);
		writeD(_skills.size());
		for(Skill skill : _skills)
		{
			writeD(skill.isPassive() ? 1 : 0);
			writeD(skill.getDisplayLevel());
			writeD(skill.getId());
			writeC(_targetChar.isUnActiveSkill(skill.getId()) ? 0x01 : 0x00);
			writeC(SkillTable.getInstance().getMaxLevel(skill.getId()) > 100 ? 1 : 0);
		}
	}
}