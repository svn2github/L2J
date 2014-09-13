package l2next.gameserver.dao;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.PremiumItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Дмитрий
 * @date 06.11.12  22:20
 */
public class CharactersPremiumItemsDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CharactersPremiumItemsDAO.class);
	private static final String INSERT_SQL_QUERY = "INSERT INTO character_premium_items (charId,itemNum,itemId,itemCount,itemSender) VALUES(?,?,?,?,?)";
	private static final String SELECT_QUERY = "SELECT itemNum, itemId, itemCount, itemSender FROM character_premium_items WHERE charId=?";
	private static final String UPDATE_QUERY = "UPDATE character_premium_items SET itemCount=? WHERE charId=? AND itemNum=?";
	private static final String DELETE_QUERY = "DELETE FROM character_premium_items WHERE charId=? AND itemNum=?";
	private static final String DELETE_ALL_QUERY = "DELETE FROM character_premium_items WHERE charId=?";
	private static CharactersPremiumItemsDAO ourInstance = new CharactersPremiumItemsDAO();

	private Map<Integer, PremiumItem> _list;

	private CharactersPremiumItemsDAO()
	{
	}

	public static CharactersPremiumItemsDAO getInstance()
	{
		return ourInstance;
	}

	public Map<Integer, PremiumItem> loadPremiumItemList(int playerObjId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		//Map<Integer, PremiumItem> premiumItemMap = new HashMap<>();
		Map<Integer, PremiumItem> premiumItemMap = new HashMap<Integer, PremiumItem>();
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_QUERY);
			statement.setInt(1, playerObjId);
			rs = statement.executeQuery();
			while(rs.next())
			{
				int itemNum = rs.getInt("itemNum");
				int itemId = rs.getInt("itemId");
				long itemCount = rs.getLong("itemCount");
				String itemSender = rs.getString("itemSender");
				PremiumItem item = new PremiumItem(itemId, itemCount, itemSender);
				premiumItemMap.put(itemNum, item);
			}
		}
		catch(Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
		return premiumItemMap;
	}

	public void updatePremiumItem(int playerObjId, int itemNum, long newcount)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_QUERY);
			statement.setLong(1, newcount);
			statement.setInt(2, playerObjId);
			statement.setInt(3, itemNum);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void deletePremiumItem(int playerObjId, int itemNum)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_QUERY);
			statement.setInt(1, playerObjId);
			statement.setInt(2, itemNum);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void insert(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_ALL_QUERY);
			statement.setInt(1, player.getObjectId());
			statement.execute();

			_list = player.getPremiumItemList();
			if(_list.isEmpty())
			{
				return;
			}
			PreparedStatement stmt = null;
			synchronized(_list)
			{
				for(Map.Entry<Integer, PremiumItem> entry : _list.entrySet())
				{
					try
					{
						con = DatabaseFactory.getInstance().getConnection();
						stmt = con.prepareStatement(INSERT_SQL_QUERY);
						stmt.setInt(1, player.getObjectId());
						stmt.setInt(2, entry.getKey());
						stmt.setInt(3, entry.getValue().getItemId());
						stmt.setLong(4, entry.getValue().getCount());
						stmt.setString(5, entry.getValue().getSender());
						stmt.execute();
					}
					catch(Exception e)
					{
						_log.error("", e);
					}
					finally
					{
						DbUtils.closeQuietly(stmt);
					}
				}
			}
		}
		catch(final Exception e)
		{
			_log.error("CharactersPremiumItemsDAO.insert(L2Player):", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
