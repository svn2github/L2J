package l2next.gameserver.skills.effects;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Skill;
import l2next.gameserver.stats.Env;

public final class EffectBuff extends Effect
{
	public EffectBuff(Env env, EffectTemplate template)
	{
		super(env, template);
	}

    @Override
    public void onStart() {
        super.onStart();
        
        if (getEffected().isPlayer()) {
            final Player player = (Player) getEffected();
            if (player.getEffectList().getEffectsBySkillId(Skill.SKILL_DUAL_CAST) != null)
            	player.setIsEnabledDoubleCast(true);
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        if (getEffected().isPlayer()) {
            final Player player = (Player) getEffected();
            if (player.getEffectList().getEffectsBySkillId(Skill.SKILL_DUAL_CAST) != null)
            	player.setIsEnabledDoubleCast(false);
        }
    }
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}