package l2next.gameserver.network.serverpackets;

import l2next.commons.collections.GArray;
import l2next.commons.util.Rnd;
import l2next.gameserver.Config;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Summon;
import l2next.gameserver.model.base.TeamType;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.pledge.Alliance;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.skills.AbnormalEffect;
import l2next.gameserver.utils.Location;
import org.apache.commons.lang3.StringUtils;

/**
 * ddddddddddddddddddffffdddcccccdSdSddd dddddccffddddccdddddddcddd
 */
public class NpcInfo extends L2GameServerPacket
{
	private boolean can_writeImpl = false;
	private int _npcObjId, _npcId, running, incombat, dead, _showSpawnAnimation;
	private int _runSpd, _walkSpd, _mAtkSpd, _pAtkSpd, _rhand, _lhand, _enchantEffect = 0;
	private int karma, pvp_flag, clan_id, clan_crest_id, ally_id, ally_crest_id, _formId, _titleColor;
	private int _HP, _maxHP, _MP, _maxMP, _CP, _maxCP;
	private double colHeight, colRadius, currentColHeight, currentColRadius;
	private boolean _isAttackable, _isNameAbove, isFlying;
	private Location _loc;
	private String _name = StringUtils.EMPTY;
	private String _title = StringUtils.EMPTY;
	private boolean _showName;
	private boolean _canTarget;
	private int _state;
	private NpcString _nameNpcString = NpcString.NONE;
	private NpcString _titleNpcString = NpcString.NONE;
	private TeamType _team;
	private int _transformId;
	private GArray<AbnormalEffect> _abnormalEffect;

	public NpcInfo(NpcInstance cha, Creature attacker)
	{
		_npcId = cha.getDisplayId() != 0 ? cha.getDisplayId() : cha.getTemplate().npcId;
		_isAttackable = attacker != null && cha.isAutoAttackable(attacker);
		_rhand = cha.getRightHandItem();
		_lhand = cha.getLeftHandItem();
		if(Config.RANDOM_NPC_ENCHANT_EFFECT)
		{
			_enchantEffect = Rnd.get(20);
		}
		if(Config.SERVER_SIDE_NPC_NAME || cha.getTemplate().displayId != 0 || cha.getName() != cha.getTemplate().name)
		{
			_name = cha.getName();
		}
		if(Config.SERVER_SIDE_NPC_TITLE || cha.getTemplate().displayId != 0 || cha.getTitle() != cha.getTemplate().title)
		{
			_title = cha.getTitle();
			if(Config.SERVER_SIDE_NPC_TITLE_ETC)
			{
				if(cha.isMonster())
				{
					if(_title.isEmpty())
					{
						_title = "Lv " + cha.getLevel();
					}
					else
					{
						_title = "Lv " + cha.getLevel() + "|" + _title;
					}
				}
			}
		}

		_HP = (int) cha.getCurrentHp();
		_MP = (int) cha.getCurrentMp();
		_CP = (int) cha.getCurrentCp();
		_maxHP = cha.getMaxHp();
		_maxMP = cha.getMaxMp();
		_maxCP = cha.getMaxCp();

		_showSpawnAnimation = cha.getSpawnAnimation();
		_showName = cha.isShowName();
		_canTarget = cha.isTargetable();
		_state = cha.getNpcState();
		_nameNpcString = cha.getNameNpcString();
		_titleNpcString = cha.getTitleNpcString();
		_transformId = cha.getTransformation();
		common(cha);
	}

	public NpcInfo(Summon cha, Creature attacker)
	{
		if(cha.getPlayer() != null && cha.getPlayer().isInvisible())
		{
			return;
		}

		_npcId = cha.getTemplate().npcId;
		_isAttackable = cha.isAutoAttackable(attacker);
		_rhand = 0;
		_lhand = 0;
		if(Config.RANDOM_NPC_ENCHANT_EFFECT)
		{
			_enchantEffect = Rnd.get(20);
		}
		_showName = true;
		_name = cha.getName();
		_title = cha.getTitle();
		_showSpawnAnimation = cha.getSpawnAnimation();

		common(cha);
	}

	private void common(Creature cha)
	{
		colHeight = cha.getTemplate().getCollisionHeight();
		colRadius = cha.getTemplate().getCollisionRadius();
		currentColHeight = cha.getColHeight();
		currentColRadius = cha.getColRadius();
		_npcObjId = cha.getObjectId();
		_loc = cha.getLoc();
		_mAtkSpd = cha.getMAtkSpd();
		//
		Clan clan = cha.getClan();
		Alliance alliance = clan == null ? null : clan.getAlliance();
		//
		clan_id = clan == null ? 0 : clan.getClanId();
		clan_crest_id = clan == null ? 0 : clan.getCrestId();
		//
		ally_id = alliance == null ? 0 : alliance.getAllyId();
		ally_crest_id = alliance == null ? 0 : alliance.getAllyCrestId();

		_runSpd = cha.getRunSpeed();
		_walkSpd = cha.getWalkSpeed();
		karma = cha.getKarma();
		pvp_flag = cha.getPvpFlag();
		_pAtkSpd = cha.getPAtkSpd();
		running = cha.isRunning() ? 1 : 0;
		incombat = cha.isInCombat() ? 1 : 0;
		dead = cha.isAlikeDead() ? 1 : 0;
		_abnormalEffect = cha.getAbnormalEffects();
		isFlying = cha.isFlying();
		_team = cha.getTeam();
		_formId = cha.getFormId();
		_isNameAbove = cha.isNameAbove();
		_titleColor = cha.isSummon() || cha.isPet() ? 1 : 0;
		can_writeImpl = true;
	}

	public NpcInfo update()
	{
		_showSpawnAnimation = 1;
		return this;
	}

	@Override
	protected final void writeImpl()
	{
		if(!can_writeImpl)
		{
			return;
		}

		writeD(_npcObjId);
		writeD(_npcId + 1000000); // npctype id c4
		writeD(_isAttackable ? 1 : 0);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z + Config.CLIENT_Z_SHIFT);
		writeD(_loc.h);
		writeD(0x00);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd /* _swimRunSpd *//* 0x32 */); // swimspeed
		writeD(_walkSpd/* _swimWalkSpd *//* 0x32 */); // swimspeed
		writeD(_runSpd/* _flRunSpd */);
		writeD(_walkSpd/* _flWalkSpd */);
		writeD(_runSpd/* _flyRunSpd */);
		writeD(_walkSpd/* _flyWalkSpd */);
		writeF(1.100000023841858); // взято из клиента
		writeF(_pAtkSpd / 277.478340719);
		writeF(colRadius);
		writeF(colHeight);
		writeD(_rhand); // right hand weapon
		writeD(0); // TODO chest
		writeD(_lhand); // left hand weapon
		writeC(_isNameAbove ? 1 : 0); // 2.2: name above char 1=true ... ??;
		// 2.3: 1 - normal, 2 - dead
		writeC(running);
		writeC(incombat);
		writeC(dead);
		writeC(_showSpawnAnimation); // invisible ?? 0=false 1=true 2=summoned
		// (only works if model has a summon
		// animation)
		writeD(_nameNpcString.getId());
		writeS(_name);
		writeD(_titleNpcString.getId());
		writeS(_title);

		writeD(_titleColor); // 0- светло зеленый титул(моб), 1 - светло
		// синий(пет)/отображение текущего МП
		writeD(pvp_flag);
		writeD(karma); // hmm karma ??

		writeD(clan_id);
		writeD(clan_crest_id);
		writeD(ally_id);
		writeD(ally_crest_id);
		writeD(0x00);

		writeC(isFlying ? 2 : 0); // C2
		writeC(_team.ordinal()); // team aura 1-blue, 2-red

		writeF(currentColRadius); // тут что-то связанное с colRadius
		writeF(currentColHeight); // тут что-то связанное с colHeight

		writeD(_enchantEffect); // C4
		writeD(isFlying ? 1 : 0);
		writeD(0x00);
		writeD(_formId);

		writeC(_canTarget ? 0x01 : 0x00); // show name
		writeC(_showName ? 0x01 : 0x00); // show title
		writeD(_state);
		writeD(_transformId);

		writeD(_HP);
		writeD(_maxHP);
		writeD(_MP);
		writeD(_maxMP);
		writeD(_CP);
		writeD(_maxCP);
		writeD(0);
		writeC(0);
		writeF(0);

		writeD(_abnormalEffect.size());
		for(AbnormalEffect ae : _abnormalEffect)
		{
			writeD(ae.getMask());
		}
	}
}