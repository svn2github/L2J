package npc.model.residences.clanhall;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.residences.clanhall.CTBBossInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

/**
 * @author VISTALL
 * @date 19:37/22.04.2011
 */
public class MatchBerserkerInstance extends CTBBossInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -918345394531733056L;

	public MatchBerserkerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if(attacker.isPlayer())
		{
			damage = damage / getMaxHp() / 0.05 * 100;
		}
		else
		{
			damage = damage / getMaxHp() / 0.05 * 10;
		}

		super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
}
