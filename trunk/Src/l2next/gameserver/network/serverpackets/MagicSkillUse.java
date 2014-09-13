package l2next.gameserver.network.serverpackets;

import l2next.gameserver.model.Creature;

/**
 * Format: dddddddddh [h] h [ddd] Пример пакета: 48 86 99 00 4F 86 99 00 4F EF 08 00 00 01 00 00 00 00 00 00 00 00 00 00 00 F9 B5 FF FF 7D E0 01 00 68
 * F3 FF FF 00 00 00 00
 */
public class MagicSkillUse extends L2GameServerPacket
{
	private int _targetId;
	private int _skillId;
	private int _skillLevel;
	private int _hitTime;
	private int _reuseDelay;
	private int _chaId, _x, _y, _z, _tx, _ty, _tz;
	private int _isDoubleCasting;

	public MagicSkillUse(Creature cha, Creature target, int skillId, int skillLevel, int hitTime, long reuseDelay, int isDoubleCastingNow)
	{
		_chaId = cha.getObjectId();
		_targetId = target.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = (int) reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		_isDoubleCasting = isDoubleCastingNow; //Упростил для инита в других классах, 1 - on, 0 - off
	}
	
	public MagicSkillUse(Creature cha, int skillId, int skillLevel, int hitTime, long reuseDelay) 
	{
        _chaId = cha.getObjectId();
        _targetId = cha.getTargetId();
        _skillId = skillId;
        _skillLevel = skillLevel;
        _hitTime = hitTime;
        _reuseDelay = (int) reuseDelay;
        _x = cha.getX();
        _y = cha.getY();
        _z = cha.getZ();
        _tx = cha.getX();
        _ty = cha.getY();
        _tz = cha.getZ();
        _isDoubleCasting = 0;
    }
	
	public MagicSkillUse(Creature cha, Creature target, int skillId, int skillLevel, int hitTime, long reuseDelay)
	{
		_chaId = cha.getObjectId();
		_targetId = target.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = (int) reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		_isDoubleCasting = 0;
	}

	@Override
	protected final void writeImpl()
	{
		writeD(_isDoubleCasting);
		writeD(_chaId);
		writeD(_targetId);
		writeC(0x00); // L2WT GOD
		writeD(_skillId);
		writeD(_skillLevel);
		writeD(_hitTime);
		writeD(getSkillReplace(_skillId));
		writeD(_reuseDelay);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(0x00); // L2WT GOD
		writeD(_tx);
		writeD(_ty);
		writeD(_tz);
	}

	public static int getSkillReplace(int _skillId)
	{
		switch(_skillId)
		{
			case 11012:
			case 11013:
			case 11014:
			case 11015:
			case 11016:
				return 11011;
			case 11018:
			case 11019:
			case 11020:
			case 11021:
			case 11022:
				return 11017;
			case 11024:
			case 11025:
			case 11026:
			case 11027:
			case 11028:
				return 11023;
			case 11035:
			case 11036:
			case 11037:
			case 11038:
			case 11039:
				return 11034;
			case 11041:
			case 11042:
			case 11043:
			case 11044:
			case 11045:
				return 11040;
			default:
				return -1;
		}
	}
}