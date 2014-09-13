package l2next.gameserver.instancemanager;

import l2next.commons.dbutils.DbUtils;
import l2next.commons.net.PremiumIp;
import l2next.gameserver.database.DatabaseFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Samurai
 */
public class PremiumIpManager
{
	private static PremiumIpManager _instance;

	public static PremiumIpManager getInstance()
	{
		if(_instance == null)
		{
			_instance = new PremiumIpManager();
		}
		return _instance;
	}

	private ArrayList<PremiumIp> premiumIps = new ArrayList<>();

	private PremiumIpManager()
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
			statement = con.prepareStatement("SELECT ip, time FROM premium_ips");
			rset = statement.executeQuery();
			while(rset.next())
			{
				PremiumIp premiumIp = new PremiumIp();
				premiumIp.setIp(rset.getString("ip"));
				premiumIp.setTime(rset.getLong("time"));
				premiumIps.add(premiumIp);
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
	}

	public void addIp(String ip, long time)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO premium_ips(ip, time) VALUES (?, ?)");
			statement.setString(1, ip);
			statement.setLong(2, time);
			statement.execute();

			PremiumIp premiumIp = new PremiumIp();
			premiumIp.setIp(ip);
			premiumIp.setTime(time);
			premiumIps.add(premiumIp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public int getCountPremiumIps(String ip)
	{
		int count = 0;
		for(PremiumIp premiumIp : premiumIps)
		{
			if(premiumIp.getIp().equalsIgnoreCase(ip) && premiumIp.getTime() >= System.currentTimeMillis() / 1000)
			{
				count++;
			}
			else
			{
				//TODO delete row db
			}
		}
		return count;
	}

	public ArrayList<PremiumIp> getPremiumIpsFromIp(String ip)
	{
		ArrayList<PremiumIp> ips = new ArrayList<>();
		for(PremiumIp premiumIp : premiumIps)
		{
			if(premiumIp.getIp().equalsIgnoreCase(ip))
			{
				ips.add(premiumIp);
			}
		}
		Arrays.sort(ips.toArray());
		return ips;
	}
}

