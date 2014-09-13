package l2next.gameserver.dao;

import l2next.commons.dbutils.DbUtils;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.model.MenteeInfo;
import l2next.gameserver.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MentoringDAO
{
	private static final Logger _log = LoggerFactory.getLogger(MentoringDAO.class);

	private static final MentoringDAO _instance = new MentoringDAO();

	// список для учеников
	private static final String menteeList = "SELECT m.mentor AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentor = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentor = s.char_obj_id AND s.active =1 ) WHERE m.mentee = ?";
	// список для наставника
	private static final String mentorList = "SELECT m.mentee AS charid, c.char_name, s.class_id, s.level FROM character_mentoring m LEFT JOIN characters c ON m.mentee = c.obj_Id LEFT JOIN character_subclasses s ON ( m.mentee = s.char_obj_id AND s.active =1 ) WHERE m.mentor = ?";
	// Выдан ли на аккаунт сертификат
	private static final String providedCertificate = "SELECT status FROM account_mentoring WHERE accountId= ?;";
	private static final String INSERT_CHAR_MENTORING = "INSERT INTO character_mentoring (mentor,mentee) VALUES(?,?)";
	private static final String DELETE_CHAR_MENTORING = "DELETE FROM character_mentoring WHERE mentor=? AND mentee=?";
	private static final String INSERT_CHAR_CERTIFICATE = "INSERT INTO account_mentoring (accountId,status) VALUES(?,?)";

	public static MentoringDAO getInstance()
	{
		return _instance;
	}

	public List<MenteeInfo> selectMenteeList(Player listOwner)
	{
		List<MenteeInfo> listMetees = new ArrayList<MenteeInfo>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			int clid = listOwner.getClassId().getId();
			statement = con.prepareStatement(clid > 138 ? mentorList : menteeList);
			statement.setInt(1, listOwner.getObjectId());
			rset = statement.executeQuery();
			while(rset.next())
			{
				int objectId = rset.getInt("charid");
				String name = rset.getString("c.char_name");
				int classId = rset.getInt("s.class_id");
				int level = rset.getInt("s.level");
				listMetees.add(new MenteeInfo(objectId, name, classId, level, classId > 138));
			}
		}
		catch(Exception e)
		{
			_log.error("MentoringDAO.load(L2Player): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return listMetees;
	}

	public void insert(Player mentor, Player mentee)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_CHAR_MENTORING);
			statement.setInt(1, mentor.getObjectId());
			statement.setInt(2, mentee.getObjectId());
			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn(mentor.getMentorSystem() + " could not add mentee objectid: " + mentee.getObjectId(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void delete(int mentor, int mentee)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_CHAR_MENTORING);
			statement.setInt(1, mentor);
			statement.setInt(2, mentee);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn("MenteeList: could not delete mentee objectId: " + mentee + " mentorId: " + mentor, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public boolean isCertificateProvide(String accountName)
	{
		boolean provided = false;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(providedCertificate);
			statement.setString(1, accountName);
			rset = statement.executeQuery();
			while(rset.next())
			{
				provided = rset.getBoolean("status");
			}
		}
		catch(Exception e)
		{
			_log.error("MentoringDAO.loadAccountData: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return provided;
	}

	public void addCertificateProvide(String accountName)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_CHAR_CERTIFICATE);
			statement.setString(1, accountName);
			statement.setBoolean(2, true);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.warn("Could not add certificate to account : " + accountName, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
