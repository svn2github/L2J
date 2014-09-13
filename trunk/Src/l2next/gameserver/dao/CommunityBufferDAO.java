package l2next.gameserver.dao;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.model.ManageBbsBuffer;
import l2next.gameserver.model.ManageBbsBuffer.SBufferScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CommunityBufferDAO
{
	private static final Logger _log = LoggerFactory.getLogger(CommunityBufferDAO.class);

	private static final CommunityBufferDAO _instance = new CommunityBufferDAO();
	public static final String SELECT_SQL_QUERY = "SELECT * FROM bbs_skillsave";
	public static final String DELETE_SQL_QUERY = "DELETE FROM bbs_skillsave WHERE charId=? AND schameid=?";
	public static final String INSERT_SQL_QUERY = "INSERT INTO bbs_skillsave (charId,schameid,name,skills) VALUES(?,?,?,?)";

	public static CommunityBufferDAO getInstance()
	{
		return _instance;
	}

	public void select()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			rset = statement.executeQuery();
			while(rset.next())
			{
				SBufferScheme scheme = new SBufferScheme();

				scheme.id = rset.getInt("schameid");
				scheme.obj_id = rset.getInt("charId");
				scheme.name = rset.getString("name");
				scheme.skills_id = ManageBbsBuffer.StringToInt(rset.getString("skills"));

				ManageBbsBuffer.getInstance();
				ManageBbsBuffer.getSchemeList().add(scheme);
			}
		}
		catch(Exception e)
		{
			_log.error("CommunityBufferDAO.select():" + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void delete(SBufferScheme scheme)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, scheme.obj_id);
			statement.setInt(2, scheme.id);
			statement.execute();

			ManageBbsBuffer.getInstance();
			ManageBbsBuffer.getSchemeList().remove(scheme);
		}
		catch(Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void insert(SBufferScheme scheme)
	{
		Connection con = null;
		PreparedStatement stmt = null;

		scheme.id = ManageBbsBuffer.getAutoIncrement(1);
		String buff_list = ManageBbsBuffer.IntToString(scheme.skills_id);
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			stmt = con.prepareStatement(INSERT_SQL_QUERY);
			stmt.setInt(1, scheme.obj_id);
			stmt.setInt(2, scheme.id);
			stmt.setString(3, scheme.name);
			stmt.setString(4, buff_list);
			stmt.execute();

			ManageBbsBuffer.getInstance();
			ManageBbsBuffer.getSchemeList().add(scheme);
		}
		catch(Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, stmt);
		}
	}
}