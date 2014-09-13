package npc.model;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

public final class OrbisInstance extends MonsterInstance
{
	private final int _weaponId;
	Creature attacker = getPlayer();

	public OrbisInstance(int ObjectID, NpcTemplate temp)
	{
		super(ObjectID, temp);
		//_weaponIdT = getParameter("weapon_id", 17372);
		_weaponId = getParameter("weapon_id", 15280);
	}

	public void onUseSkill()
	{
		if(attacker != null && attacker != this)
		{
			startAttackStanceTask();
		}
	}

	public void startAttackStanceTask()
	{
		equipWeapon();
		super.startAttackStanceTask();
	}

	public void stopAttackStanceTask()
	{
		unequipWeapon();
		super.stopAttackStanceTask();
	}

	private void unequipWeapon()
	{
		if(isAlikeDead())
		{
			return;
		}

		setRHandId(0);
		broadcastCharInfoImpl();
	}

	private void equipWeapon()
	{
		setRHandId(_weaponId);
		broadcastCharInfoImpl();
	}
}