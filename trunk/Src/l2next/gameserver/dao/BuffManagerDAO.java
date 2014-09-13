package l2next.gameserver.dao;

import gnu.trove.list.array.TIntArrayList;
import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.database.DatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author CSharp
 * @modified KilRoy
 */
public class BuffManagerDAO
{
	private static final Logger _log = LoggerFactory.getLogger(BuffManagerDAO.class);
	private static BuffManagerDAO _instance;

	public static final String SELECT_SQL_QUERY = "SELECT skillID FROM communitybuff_grp_allowed_buffs";

	private TIntArrayList allowedBuffs;

	public static BuffManagerDAO getInstance()
	{
		if(_instance == null)
		{
			_instance = new BuffManagerDAO();
		}

		return _instance;
	}

	public BuffManagerDAO()
	{
		if(allowedBuffs == null)
		{
			allowedBuffs = new TIntArrayList();
		}

		if(!loadAvailableBuffs())
		{
			_log.error("Can't load available buffs from database!");
		}
	}

	private boolean loadAvailableBuffs()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;

		try
		{
			con = new DatabaseFactory().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);

			rset = statement.executeQuery();

			while(rset.next())
			{
				allowedBuffs.add(rset.getInt("skillID"));
			}
			;

			return true;
		}
		catch(SQLException e)
		{
			_log.info("BuffManagerDAO.select(String): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public TIntArrayList getAllowedBuffs()
	{
		return allowedBuffs;
	}

}
