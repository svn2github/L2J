package l2next.gameserver.exception;

/**
 * @author ALF
 * @date 21.08.2012
 */
public class SkillNotFoundException extends Exception
{
	private static final long serialVersionUID = -6058261899890636706L;

	private final int _skillId;
	private final int _skillLvl;

	public SkillNotFoundException(int skillId)
	{
		_skillId = skillId;
		_skillLvl = 1;
	}

	public SkillNotFoundException(int skillId, int skillLvl)
	{
		_skillId = skillId;
		_skillLvl = skillLvl;
	}

	@Override
	public String toString()
	{
		return "Skill not found! SkillId: " + _skillId + " level " + _skillLvl + " !";
	}

}
