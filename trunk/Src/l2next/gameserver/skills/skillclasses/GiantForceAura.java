package l2next.gameserver.skills.skillclasses;

import l2next.gameserver.model.Creature;
import l2next.gameserver.templates.StatsSet;

import java.util.List;

/**
 * @author : Ragnarok
 * @date : 24.05.12 13:12
 */
public class GiantForceAura extends Toggle
{
	private int forceSkillId;
	private int auraSkillId;

	public GiantForceAura(StatsSet set)
	{
		super(set);

		auraSkillId = set.getInteger("auraSkillId", -1);
		forceSkillId = set.getInteger("forceSkillId", -1);
	}

	// @Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		for(Creature target : targets)
		{
			if(getForceSkillId() != -1)
			{    // Контроллирующий скилл
				if(target != activeChar)
				{
					continue;
				}

				if(target.getEffectList().getEffectsBySkillId(_id) == null)
				{    // Включение
					getEffects(target, target, false, false);
				}
				else
				{    // Выключение
					target.getEffectList().stopEffect(_id);
					target.sendActionFailed();
				}
			}
		}
	}

	public int getAuraSkillId()
	{
		return auraSkillId;
	}

	public int getForceSkillId()
	{
		return forceSkillId;
	}
}
