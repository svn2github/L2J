package l2next.gameserver.model.entity.olympiad;

import gnu.trove.map.hash.TIntObjectHashMap;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.party.Party;
import l2next.gameserver.network.serverpackets.ExOlympiadUserInfo;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.components.IStaticPacket;
import l2next.gameserver.templates.StatsSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OlympiadTeam
{
	private OlympiadGame _game;
	private TIntObjectHashMap<TeamMember> _members;
	private String _name = "";
	private int _side;
	private double _damage;

	public OlympiadTeam(OlympiadGame game, int side)
	{
		_game = game;
		_side = side;
		_members = new TIntObjectHashMap<TeamMember>();
	}

	public void addMember(int obj_id)
	{
		String player_name = "";
		Player player = GameObjectsStorage.getPlayer(obj_id);
		if(player != null)
		{
			player_name = player.getName();
		}
		else
		{
			StatsSet noble = Olympiad._nobles.get(obj_id);
			if(noble != null)
			{
				player_name = noble.getString(Olympiad.CHAR_NAME, "");
			}
		}

		_members.put(obj_id, new TeamMember(obj_id, player_name, player, _game, _side));

		_name = player_name;
	}

	public void addDamage(Player player, double damage)
	{
		_damage += damage;

		TeamMember member = _members.get(player.getObjectId());
		member.addDamage(damage);
	}

	public double getDamage()
	{
		return _damage;
	}

	public String getName()
	{
		return _name;
	}

	public void portPlayersToArena()
	{
		for(TeamMember member : _members.valueCollection())
		{
			member.portPlayerToArena();
		}
	}

	public void portPlayersBack()
	{
		for(TeamMember member : _members.valueCollection())
		{
			member.portPlayerBack();
		}
	}

	public void preparePlayers()
	{
		for(TeamMember member : _members.valueCollection())
		{
			member.preparePlayer();
		}

		if(_members.size() <= 1)
		{
			return;
		}

		List<Player> list = new ArrayList<Player>();
		for(TeamMember member : _members.valueCollection())
		{
			Player player = member.getPlayer();
			if(player != null)
			{
				list.add(player);
				player.leaveParty();
			}
		}

		if(list.size() <= 1)
		{
			return;
		}

		Player leader = list.get(0);
		if(leader == null)
		{
			return;
		}

		Party party = new Party(leader, 0);
		leader.setParty(party);

		for(Player player : list)
		{
			if(player != leader)
			{
				player.joinParty(party);
			}
		}
	}

	public void takePointsForCrash()
	{
		for(TeamMember member : _members.valueCollection())
		{
			member.takePointsForCrash();
		}
	}

	public boolean checkPlayers()
	{
		for(TeamMember member : _members.valueCollection())
		{
			if(member.checkPlayer())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isAllDead()
	{
		for(TeamMember member : _members.valueCollection())
		{
			if(!member.isDead() && member.checkPlayer())
			{
				return false;
			}
		}
		return true;
	}

	public boolean contains(int objId)
	{
		return _members.containsKey(objId);
	}

	public List<Player> getPlayers()
	{
		List<Player> players = new ArrayList<Player>(_members.size());
		for(TeamMember member : _members.valueCollection())
		{
			Player player = member.getPlayer();
			if(player != null)
			{
				players.add(player);
			}
		}
		return players;
	}

	public Collection<TeamMember> getMembers()
	{
		return _members.valueCollection();
	}

	public void broadcast(L2GameServerPacket p)
	{
		for(TeamMember member : _members.valueCollection())
		{
			Player player = member.getPlayer();
			if(player != null)
			{
				player.sendPacket(p);
			}
		}
	}

	public void broadcast(IStaticPacket p)
	{
		for(TeamMember member : _members.valueCollection())
		{
			Player player = member.getPlayer();
			if(player != null)
			{
				player.sendPacket(p);
			}
		}
	}

	public void broadcastInfo()
	{
		for(TeamMember member : _members.valueCollection())
		{
			Player player = member.getPlayer();
			if(player != null)
			{
				player.broadcastPacket(new ExOlympiadUserInfo(player, player.getOlympiadSide()));
			}
		}
	}

	public boolean logout(Player player)
	{
		if(player != null)
		{
			for(TeamMember member : _members.valueCollection())
			{
				Player pl = member.getPlayer();
				if(pl != null && pl == player)
				{
					member.logout();
				}
			}
		}
		return checkPlayers();
	}

	public boolean doDie(Player player)
	{
		if(player != null)
		{
			for(TeamMember member : _members.valueCollection())
			{
				Player pl = member.getPlayer();
				if(pl != null && pl == player)
				{
					member.doDie();
				}
			}
		}
		return isAllDead();
	}

	public void saveNobleData()
	{
		for(TeamMember member : _members.valueCollection())
		{
			member.saveNobleData();
		}
	}
}