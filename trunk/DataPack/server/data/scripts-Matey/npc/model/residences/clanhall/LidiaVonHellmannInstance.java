package npc.model.residences.clanhall;

import l2next.gameserver.model.AggroList;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import l2next.gameserver.model.entity.events.impl.SiegeEvent;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.templates.npc.NpcTemplate;
import npc.model.residences.SiegeGuardInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @date 12:25/08.05.2011
 */
public class LidiaVonHellmannInstance extends SiegeGuardInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 880184515847917714L;

	public LidiaVonHellmannInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onDeath(Creature killer)
	{
		SiegeEvent siegeEvent = getEvent(SiegeEvent.class);
		if(siegeEvent == null)
		{
			return;
		}

		siegeEvent.processStep(getMostDamagedClan());

		super.onDeath(killer);
	}

	public Clan getMostDamagedClan()
	{
		ClanHallSiegeEvent siegeEvent = getEvent(ClanHallSiegeEvent.class);

		Player temp = null;

		Map<Player, Integer> damageMap = new HashMap<Player, Integer>();

		for(AggroList.HateInfo info : getAggroList().getPlayableMap().values())
		{
			Playable killer = (Playable) info.attacker;
			int damage = info.damage;
			if(killer.isPet() || killer.isSummon())
			{
				temp = killer.getPlayer();
			}
			else if(killer.isPlayer())
			{
				temp = (Player) killer;
			}

			if(temp == null || siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, temp.getClan()) == null)
			{
				continue;
			}

			if(!damageMap.containsKey(temp))
			{
				damageMap.put(temp, damage);
			}
			else
			{
				int dmg = damageMap.get(temp) + damage;
				damageMap.put(temp, dmg);
			}
		}

		int mostDamage = 0;
		Player player = null;

		for(Map.Entry<Player, Integer> entry : damageMap.entrySet())
		{
			int damage = entry.getValue();
			Player t = entry.getKey();
			if(damage > mostDamage)
			{
				mostDamage = damage;
				player = t;
			}
		}

		return player == null ? null : player.getClan();
	}

	@Override
	public boolean isEffectImmune()
	{
		return true;
	}
}
