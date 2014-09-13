package l2next.gameserver.dao;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.database.DatabaseFactoryLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: Samurai
 */
public class AccountDAO
{
	private static final Logger _log = LoggerFactory.getLogger(AccountDAO.class);
	private static final AccountDAO _instance = new AccountDAO();

	public static final String SELECT_SQL_QUERY = "SELECT login FROM accounts WHERE login=? LIMIT 1";

	public static AccountDAO getInstance()
	{
		return _instance;
	}

	public boolean check(String login)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactoryLogin.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setString(1, login);
			rset = statement.executeQuery();
			if(rset.next())
			{
				return true;
			}
		}
		catch(Exception e)
		{
			_log.info("AccountDAO.select(String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return false;
	}

	public void setAllowIp(String account, String ip)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactoryLogin.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE accounts SET allow_ip=? WHERE login=?");
			statement.setString(1, ip);
			statement.setString(2, account);
			statement.execute();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public String getAllowIp(String account)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactoryLogin.getInstance().getConnection();
			statement = con.prepareStatement("SELECT allow_ip FROM accounts WHERE login=? LIMIT 1");
			statement.setString(1, account);
			rset = statement.executeQuery();
			if(rset.next())
			{
				return rset.getString("allow_ip");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return "";
	}
}