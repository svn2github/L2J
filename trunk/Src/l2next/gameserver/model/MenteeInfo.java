package l2next.gameserver.model;

import l2next.commons.lang.reference.HardReference;
import l2next.commons.lang.reference.HardReferences;

/**
 * User: Andrey A. Date: 01.11.12 Time: 12:19
 */
public class MenteeInfo
{
	private final int _objectId;
	private String _name;
	private int _classId;
	private int _level;
	private boolean _isMentor;

	private HardReference<Player> _playerRef = HardReferences.emptyRef();

	public MenteeInfo(int objectId, String name, int classId, int level, boolean isMentor)
	{
		_objectId = objectId;
		_name = name;
		_classId = classId;
		_level = level;
		_isMentor = isMentor;
	}

	public MenteeInfo(Player player)
	{
		_objectId = player.getObjectId();
		update(player, true);
	}

	public MenteeInfo(Player player, boolean isMentor)
	{
		_objectId = player.getObjectId();
		_isMentor = isMentor;
		update(player, true);
	}

	public void update(Player player, boolean set)
	{
		_level = player.getLevel();
		_name = player.getName();
		_classId = player.getActiveClassId();
		_playerRef = set ? player.getRef() : HardReferences.<Player>emptyRef();
	}

	public String getName()
	{
		Player player = getPlayer();
		return player == null ? _name : player.getName();
	}

	public int getObjectId()
	{
		return _objectId;
	}

	public int getClassId()
	{
		Player player = getPlayer();
		return player == null ? _classId : player.getActiveClassId();
	}

	public int getLevel()
	{
		Player player = getPlayer();
		return player == null ? _level : player.getLevel();
	}

	public boolean isOnline()
	{
		Player player = _playerRef.get();
		return player != null && !player.isInOfflineMode();
	}

	public Player getPlayer()
	{
		Player player = _playerRef.get();
		return player != null && !player.isInOfflineMode() ? player : null;
	}

	public boolean isMentor()
	{
		return _isMentor;
	}

	@Override
	public String toString()
	{
		return "MenteeInfo{" +
			"_objectId=" + _objectId +
			", _name='" + _name + '\'' +
			", _classId=" + _classId +
			", _level=" + _level +
			", _isMentor=" + _isMentor +
			", _playerRef=" + _playerRef +
			'}';
	}
}
