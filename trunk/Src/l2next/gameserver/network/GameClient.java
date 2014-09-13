package l2next.gameserver.network;

import l2next.commons.dbutils.DbUtils;
import l2next.commons.net.nio.impl.MMOClient;
import l2next.commons.net.nio.impl.MMOConnection;
import l2next.gameserver.Config;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.dao.CharacterDAO;
import l2next.gameserver.database.DatabaseFactory;
import l2next.gameserver.loginservercon.LoginServerCommunication;
import l2next.gameserver.loginservercon.SessionKey;
import l2next.gameserver.loginservercon.gspackets.PlayerLogout;
import l2next.gameserver.loginservercon.gspackets.PointConnectionGS;
import l2next.gameserver.model.CharSelectionInfo;
import l2next.gameserver.model.CharSelectionInfo.CharSelectInfoPackage;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.security.SecondaryPasswordAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client connected on Game Server
 */
public final class GameClient extends MMOClient<MMOConnection<GameClient>>
{
	private static final Logger _log = LoggerFactory.getLogger(GameClient.class);
	private static final String NO_IP = "?.?.?.?";
	private static byte[] _keyClientEn = new byte[8];
	public GameCrypt _crypt = null;
	public GameClientState _state;
	/**
	 * Данные аккаунта
	 */
	private String _login;
	private double _bonus = 1.0;
	private int _bonusExpire;
	private Player _activeChar;
	private SessionKey _sessionKey;
	private String _ip = NO_IP;
	private int revision = 0;
	private boolean _gameGuardOk = false;
	private int _points;
	private List<Integer> _charSlotMapping = new ArrayList<Integer>();
	private SecondaryPasswordAuth _secondaryAuth;
	private int _failedPackets = 0;
	private int _unknownPackets = 0;
	private String _hwid;
	private boolean _isProtected;

	public GameClient(MMOConnection<GameClient> con)
	{
		super(con);

		_state = GameClientState.CONNECTED;
		_crypt = new GameCrypt();
		_ip = con.getSocket().getInetAddress().getHostAddress();
	}

	public static byte[] getKeyClientEn()
	{
		return _keyClientEn;
	}

	public static void setKeyClientEn(byte[] key)
	{
		_keyClientEn = key;
	}

	@Override
	protected void onDisconnection()
	{
		final Player player;

		World.removeIp(getIpAddr());

		setState(GameClientState.DISCONNECTED);
		player = getActiveChar();
		setActiveChar(null);

		if(player != null)
		{
			player.setNetConnection(null);
			player.scheduleDelete();
		}

		if(getSessionKey() != null)
		{
			if(isAuthed())
			{
				LoginServerCommunication.getInstance().removeAuthedClient(getLogin());
				LoginServerCommunication.getInstance().sendPacket(new PlayerLogout(getLogin()));
			}
			else
			{
				LoginServerCommunication.getInstance().removeWaitingClient(getLogin());
			}
		}
	}

	@Override
	protected void onForcedDisconnection()
	{
		// TODO Auto-generated method stub

	}

	public int getCharsInAccount()
	{
		return _charSlotMapping.size();
	}

	public void markRestoredChar(int charslot) throws Exception
	{
		int objid = getObjectIdForSlot(charslot);
		if(objid < 0)
		{
			return;
		}

		if(_activeChar != null && _activeChar.getObjectId() == objid)
		{
			_activeChar.setDeleteTimer(0);
		}

		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET deletetime=0 WHERE obj_id=?");
			statement.setInt(1, objid);
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

	public void markToDeleteChar(int charslot) throws Exception
	{
		int objid = getObjectIdForSlot(charslot);
		if(objid < 0)
		{
			return;
		}

		if(_activeChar != null && _activeChar.getObjectId() == objid)
		{
			_activeChar.setDeleteTimer((int) (System.currentTimeMillis() / 1000));
		}

		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE characters SET deletetime=? WHERE obj_id=?");
			statement.setLong(1, (int) (System.currentTimeMillis() / 1000L));
			statement.setInt(2, objid);
			statement.execute();
		}
		catch(Exception e)
		{
			_log.error("data error on update deletime char:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void deleteChar(int charslot) throws Exception
	{
		// have to make sure active character must be nulled
		if(_activeChar != null)
		{
			return;
		}

		int objid = getObjectIdForSlot(charslot);
		if(objid == -1)
		{
			return;
		}

		CharacterDAO.getInstance().deleteCharByObjId(objid);
	}

	public Player loadCharFromDisk(int charslot)
	{
		int objectId = getObjectIdForSlot(charslot);
		if(objectId == -1)
		{
			return null;
		}

		Player character = null;
		Player oldPlayer = GameObjectsStorage.getPlayer(objectId);

		if(oldPlayer != null)
		{
			if(oldPlayer.isInOfflineMode() || oldPlayer.isLogoutStarted())
			{
				// оффтрейдового чара проще выбить чем восстанавливать
				oldPlayer.kick();
				return null;
			}
			else
			{
				oldPlayer.sendPacket(Msg.ANOTHER_PERSON_HAS_LOGGED_IN_WITH_THE_SAME_ACCOUNT);

				GameClient oldClient = oldPlayer.getNetConnection();
				if(oldClient != null)
				{
					oldClient.setActiveChar(null);
					oldClient.closeNow(false);
				}
				oldPlayer.setNetConnection(this);
				character = oldPlayer;
			}
		}

		if(character == null)
		{
			character = Player.restore(objectId);
		}

		if(character != null)
		{
			setActiveChar(character);
		}
		else
		{
			_log.warn("could not restore obj_id: " + objectId + " in slot:" + charslot);
		}

		return character;
	}

	public int getObjectIdForSlot(int charslot)
	{
		if(charslot < 0 || charslot >= _charSlotMapping.size())
		{
			_log.warn(getLogin() + " tried to modify Character in slot " + charslot + " but no characters exits at that slot.");
			return -1;
		}
		return _charSlotMapping.get(charslot);
	}

	public Player getActiveChar()
	{
		return _activeChar;
	}

	public void setActiveChar(Player player)
	{
		_activeChar = player;
		if(player != null)
		{
			player.setNetConnection(this);
		}
	}

	/**
	 * @return Returns the sessionId.
	 */
	public SessionKey getSessionKey()
	{
		return _sessionKey;
	}

    /*
      * @Override public boolean encrypt(final ByteBuffer buf, final int size) { _crypt.encrypt(buf.array(), buf.position(), size);
      * buf.position(buf.position() + size); return true; }
      *
      * @Override public boolean decrypt(ByteBuffer buf, int size) { boolean ret = _crypt.decrypt(buf.array(), buf.position(), size);
      *
      * return ret; }
      */

	public String getLogin()
	{
		return _login;
	}

	public void setLoginName(String loginName)
	{
		_login = loginName;

		if(Config.SECOND_AUTH_ENABLED)
		{
			_secondaryAuth = new SecondaryPasswordAuth(this);
		}
	}

	public void setSessionId(SessionKey sessionKey)
	{
		_sessionKey = sessionKey;
	}

	public void setCharSelection(CharSelectionInfo selectionInfo)
	{
		_charSlotMapping.clear();

		for(CharSelectInfoPackage element : selectionInfo)
		{
			int objectId = element.getObjectId();
			_charSlotMapping.add(objectId);
		}
	}

	public int getRevision()
	{
		return revision;
	}

	public void setRevision(int revision)
	{
		this.revision = revision;
	}

	@Override
	public boolean encrypt(final ByteBuffer buf, final int size)
	{
		_crypt.encrypt(buf.array(), buf.position(), size);
		buf.position(buf.position() + size);
		return true;
	}

	@Override
	public boolean decrypt(ByteBuffer buf, int size)
	{
		boolean ret = true;
		_crypt.decrypt(buf.array(), buf.position(), size);
		return ret;
	}

	public void sendPacket(L2GameServerPacket gsp)
	{
		if(isConnected())
		{
			getConnection().sendPacket(gsp);
		}
	}

	public void sendPacket(L2GameServerPacket... gsp)
	{
		if(isConnected())
		{
			getConnection().sendPacket(gsp);
		}
	}

	public void sendPackets(List<L2GameServerPacket> gsp)
	{
		if(isConnected())
		{
			getConnection().sendPackets(gsp);
		}
	}

	public void close(L2GameServerPacket gsp)
	{
		if(isConnected())
		{
			getConnection().close(gsp);
		}
	}

	public String getIpAddr()
	{
		return _ip;
	}

	public byte[] enableCrypt()
	{
		byte[] key = BlowFishKeygen.getRandomKey();
		_crypt.setKey(key);

		return key;
	}

	public double getBonus()
	{
		return _bonus;
	}

	public void setBonus(double bonus)
	{
		_bonus = bonus;
	}

	public int getBonusExpire()
	{
		return _bonusExpire;
	}

	public void setBonusExpire(int bonusExpire)
	{
		_bonusExpire = bonusExpire;
	}

	public GameClientState getState()
	{
		return _state;
	}

	public void setState(GameClientState state)
	{
		_state = state;
	}

	public void onPacketReadFail()
	{
		if(_failedPackets++ >= 10)
		{
			_log.warn("Too many client packet fails, connection closed : " + this);
			closeNow(true);
		}
	}

	public void onUnknownPacket()
	{
		if(_unknownPackets++ >= 10)
		{
			_log.warn("Too many client unknown packets, connection closed : " + this);
			closeNow(true);
		}
	}

	@SuppressWarnings("deprecation")
	public void checkHwid(String allowedHwid)
	{
		if(!allowedHwid.equalsIgnoreCase("") && !getHWID().equalsIgnoreCase(allowedHwid))
		{
			closeNow(false);
		}
	}

	public String getHWID()
	{
		return _hwid;
	}

	public void setHWID(String hwid)
	{
		_hwid = hwid;
	}

	public boolean isProtected()
	{
		return _isProtected;
	}

	public void setProtected(boolean isProtected)
	{
		_isProtected = isProtected;
	}

	public SecondaryPasswordAuth getSecondaryAuth()
	{
		return _secondaryAuth;
	}

	public boolean isGameGuardOk()
	{
		return _gameGuardOk;
	}

	public void setGameGuardOk(boolean gameGuardOk)
	{
		_gameGuardOk = gameGuardOk;
	}

	public int getPremiumPoint()
	{
		return _points;
	}

	public void setPremiumPoint(int points)
	{
		_points = points;
		LoginServerCommunication.getInstance().sendPacket(new PointConnectionGS(getLogin(), getPremiumPoint()));
	}

	@Override
	public String toString()
	{
		return _state + " IP: " + getIpAddr() + (_login == null ? "" : " Account: " + _login) + (_activeChar == null ? "" : " Player : " + _activeChar);
	}

	public static enum GameClientState
	{
		CONNECTED, AUTHED, IN_GAME, DISCONNECTED
	}
}