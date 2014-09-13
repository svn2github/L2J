package l2next.gameserver.instancemanager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javolution.util.FastMap;
import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.database.*;
import l2next.gameserver.idfactory.*;
import l2next.gameserver.model.*;
import l2next.gameserver.templates.InstancePartyHistory;
import l2next.gameserver.model.party.Party;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Arrays;

/**
* @Author Awakeninger
*/
public class InstanceHistoryManager
{
	private String select = "SELECT * FROM character_inzone_history ORDER BY character_id";
	private String select1 = "SELECT * FROM party_inzone_history  ORDER BY party_id";
	private static final Logger _log = LoggerFactory.getLogger(InstanceHistoryManager.class);

	private static final FastMap<Integer, InstancePartyHistory> _instancePartyHistoryData = new FastMap();
	private static final FastMap<Integer, List<Integer>> _instanceCharacterHistoryData = new FastMap();
	private static InstanceHistoryManager _instance;
	private int instanceId;
	private long instanceReuse;
  
	public static InstanceHistoryManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new InstanceHistoryManager();
		}
		return _instance;
	}
	
	public InstanceHistoryManager()
	{
		load();
	}

	private void load()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM character_inzone_history ORDER BY character_id");
			rset = statement.executeQuery(select);
			
			if(rset.next())
			{
				InstancePartyHistory data = new InstancePartyHistory();
				data.setInstanceId(rset.getInt("instance_id"));
				data.setInstanceUseTime(rset.getInt("instance_use_time"));
				data.setInstanceStatus(rset.getInt("instance_status"));
				_instancePartyHistoryData.put(Integer.valueOf(rset.getInt("party_id")), data);
			}
			_log.info(getClass().getSimpleName() + ": Loaded " + _instancePartyHistoryData.size() + " party histories.");
		}
		catch(Exception e)
		{
			_log.error("Could not load party instance history: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
			

		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM party_inzone_history ORDER BY party_id");
			rset = statement.executeQuery(select1);
			if(rset.next())
			{
				int charId = rset.getInt("character_id");
				int partyId = rset.getInt("party_id");
				List charHistory = new ArrayList();
				if (!_instanceCharacterHistoryData.containsKey(Integer.valueOf(charId)))
				{
					charHistory.add(Integer.valueOf(partyId));
				}
				else
				{
					charHistory = (List)_instanceCharacterHistoryData.get(Integer.valueOf(charId));
					charHistory.add(Integer.valueOf(partyId));
				}
				_instanceCharacterHistoryData.put(Integer.valueOf(charId), charHistory);
			}
			_log.info(getClass().getSimpleName() + ": Loaded " + _instancePartyHistoryData.size() + " characters in  histories.");
		}
		catch (Exception e)
		{
			_log.error("Could not load character party's instance's history: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void addHistoryForParty(Party party)
	{
		int partyId = IdFactory.getInstance().getNextId();
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO party_inzone_history (character_id, party_id) VALUES (?, ?)");
			for (Player player : party.getPartyMembers())
			{
				statement.setInt(1, player.getObjectId());
				statement.setInt(2, partyId);
				statement.execute();

				if (_instanceCharacterHistoryData.containsKey(Integer.valueOf(player.getObjectId())))
				{
					((List)_instanceCharacterHistoryData.get(Integer.valueOf(player.getObjectId()))).add(Integer.valueOf(partyId));
				}
				else
				{
					List temp = new ArrayList();
					temp.add(Integer.valueOf(partyId));
					_instanceCharacterHistoryData.put(Integer.valueOf(player.getObjectId()), temp);
				}

			}

		}
		catch (Exception e)
		{
			_log.error("Error while addHistoryForParty(): " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void addHistoryForCharacter(Player player)
	{
		int instance_id = player.getActiveReflection().getInstancedZoneId();
		long instance_use_time = player.getActiveReflection().getReuseTime();
		int instance_status = 0;
		int partyId = 0;
		if(player.getParty() != null)
		{
			partyId = IdFactory.getInstance().getNextId();
		}
		
		if(player.getActiveReflection() != null)
		{
			instance_status = 1;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO character_inzone_history (character_id, instance_id, instance_use_time, instance_status, party_id) VALUES (?, ?, ?, ?, ?)");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, instance_id);
			statement.setLong(3, instance_use_time);
			statement.setInt(4, instance_status);
			statement.setInt(5, partyId);
			statement.execute();	
			if (_instanceCharacterHistoryData.containsKey(Integer.valueOf(player.getObjectId())))
			{
				((List)_instanceCharacterHistoryData.get(Integer.valueOf(player.getObjectId()))).add(Integer.valueOf(partyId));
			}
			else
			{
				List temp = new ArrayList();
				temp.add(Integer.valueOf(partyId));
				_instanceCharacterHistoryData.put(Integer.valueOf(player.getObjectId()), temp);
			}
		}
		catch (Exception e)
		{
			_log.error("Error while addHistoryForParty(): " + e.getMessage(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		
		instanceId = instance_id;
		instanceReuse = instance_use_time;
	}
	
	public int instId()
	{
		return instanceId;
	}
	
	public long instReenter()
	{
		return instanceReuse;
	}
	
	public InstancePartyHistory getPartyInzoneHistory(int partyId)
	{
		return (InstancePartyHistory)_instancePartyHistoryData.get(Integer.valueOf(partyId));
	}

	public List<Integer> getCharacterInzoneHistory(int charId)
	{
		return (List)_instanceCharacterHistoryData.get(Integer.valueOf(charId));
	}
}