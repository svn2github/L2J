package l2next.gameserver.database;

import l2next.commons.dbcp.BasicDataSource;
import l2next.gameserver.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: Samurai
 */
public class DatabaseFactoryLogin extends BasicDataSource
{
	private static final DatabaseFactoryLogin _instance = new DatabaseFactoryLogin();

	public static final DatabaseFactoryLogin getInstance() throws SQLException
	{
		return _instance;
	}

	public DatabaseFactoryLogin()
	{
		super(Config.DATABASE_DRIVER, Config.DATABASE_URL_LOGIN, Config.DATABASE_LOGIN_LOGIN, Config.DATABASE_PASSWORD_LOGIN, Config.DATABASE_MAX_CONNECTIONS, Config.DATABASE_MAX_CONNECTIONS, Config.DATABASE_MAX_IDLE_TIMEOUT, Config.DATABASE_IDLE_TEST_PERIOD, false);
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		return getConnection(null);
	}
}
