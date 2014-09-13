package l2next.gameserver.model.instances;

import l2next.gameserver.ai.CharacterAI;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Skill;
import l2next.gameserver.network.serverpackets.Die;
import l2next.gameserver.templates.npc.NpcTemplate;

public class DeadManInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 6018606494042957815L;

	public DeadManInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setAI(new CharacterAI(this));
	}

	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		setCurrentHp(0, false);
		broadcastPacket(new Die(this));
		setWalking();
	}

	@Override
	public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
	}

	@Override
	public boolean isInvul()
	{
		return true;
	}

	@Override
	public boolean isBlocked()
	{
		return true;
	}
}