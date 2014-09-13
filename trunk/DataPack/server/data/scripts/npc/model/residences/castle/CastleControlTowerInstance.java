package npc.model.residences.castle;

import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Spawner;
import l2next.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import l2next.gameserver.templates.npc.NpcTemplate;

import java.util.HashSet;
import java.util.Set;

public class CastleControlTowerInstance extends SiegeToggleNpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = -4324555600651530261L;
	private Set<Spawner> _spawnList = new HashSet<Spawner>();

	public CastleControlTowerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onDeathImpl(Creature killer)
	{
		for(Spawner spawn : _spawnList)
		{
			spawn.stopRespawn();
		}
		_spawnList.clear();
	}

	@Override
	public void register(Spawner spawn)
	{
		_spawnList.add(spawn);
	}
}