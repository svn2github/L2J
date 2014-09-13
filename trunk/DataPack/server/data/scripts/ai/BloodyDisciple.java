package ai;

import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.skills.effects.EffectTemplate;
import l2next.gameserver.stats.Env;
import l2next.gameserver.tables.SkillTable;

public class BloodyDisciple extends Fighter
{
	private static final int[] buffId = {
		14975,
		14976,
		14977
	};

	public BloodyDisciple(NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(Creature killer)
	{
		if(killer.isPlayable())
		{
			Player player = killer.getPlayer();
			for(int buff : buffId)
			{
				Skill skill = SkillTable.getInstance().getInfo(buff, 1);
				for(EffectTemplate et : skill.getEffectTemplates())
				{
					Env env = new Env(player, player, skill);
					Effect effect = et.getEffect(env);
					player.getEffectList().addEffect(effect);
				}
			}
		}
		super.onEvtDead(killer);
	}
}