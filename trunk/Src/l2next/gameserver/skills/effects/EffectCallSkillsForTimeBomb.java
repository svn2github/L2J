package l2next.gameserver.skills.effects;

import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Skill;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.stats.Env;
import l2next.gameserver.tables.SkillTable;

public class EffectCallSkillsForTimeBomb extends Effect
{
	private boolean check;
	public EffectCallSkillsForTimeBomb(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public void onExit()
	{
		super.onExit();
	}
	@Override
	public void onStart()
	{
		super.onStart();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				if(getEffected().getEffectList().getEffectsBySkillId(10788) != null)
				{
					check = true;
				}
			}
		}, 1000L, 1000L);
		if(check)
		{
			int[] skillIds = getTemplate().getParam().getIntegerArray("skillIds");
			int[] skillLevels = getTemplate().getParam().getIntegerArray("skillLevels");
			for(int i = 0; i < skillIds.length; i++)
			{
				Skill skill = SkillTable.getInstance().getInfo(skillIds[i], skillLevels[i]);
				for(Creature cha : skill.getTargets(getEffector(), getEffected(), false))
				{
					getEffector().broadcastPacket(new MagicSkillUse(getEffector(), cha, skillIds[i], skillLevels[i], 0, 0));
					getEffector().callSkill(skill, skill.getTargets(getEffector(), getEffected(), false), false);
				}
			}
			onExit();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				int[] skillIds = getTemplate().getParam().getIntegerArray("skillIds");
				int[] skillLevels = getTemplate().getParam().getIntegerArray("skillLevels");
				for(int i = 0; i < skillIds.length; i++)
				{
					Skill skill = SkillTable.getInstance().getInfo(skillIds[i], skillLevels[i]);
					for(Creature cha : skill.getTargets(getEffector(), getEffected(), false))
					{
						getEffector().broadcastPacket(new MagicSkillUse(getEffector(), cha, skillIds[i], skillLevels[i], 0, 0));
						getEffector().callSkill(skill, skill.getTargets(getEffector(), getEffected(), false), false);
					}
				}
				onExit();
			}
		},10 * 1000L);
		//getEffector().callSkill(skill, skill.getTargets(getEffector(), getEffected(), false), false);
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}