package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.dao.EffectsDAO;
import l2next.gameserver.data.xml.holder.NpcHolder;
import l2next.gameserver.idfactory.IdFactory;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObjectTasks;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.base.Experience;
import l2next.gameserver.model.entity.events.impl.SiegeEvent;
import l2next.gameserver.model.instances.CloneInstance;
import l2next.gameserver.model.instances.DeadsGateInstance;
import l2next.gameserver.model.instances.MerchantInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.instances.SummonInstance;
import l2next.gameserver.model.instances.SymbolInstance;
import l2next.gameserver.model.instances.TrapInstance;
import l2next.gameserver.model.instances.TreeInstance;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.stats.Stats;
import l2next.gameserver.stats.funcs.FuncAdd;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.Location;

public class Summon extends Skill
{
	private final SummonType _summonType;

	private final double _expPenalty;
	private final int _itemConsumeIdInTime;
	private final int _itemConsumeCountInTime;
	private final int _itemConsumeDelay;
	private final int _lifeTime;
	private final int _lifeTimeClone;

	private static enum SummonType
	{
		PET, SERVITOR, MULTI_SERVITOR, SIEGE_SUMMON, AGATHION, TRAP, TREE, CLONE, MERCHANT, DEADS_GATE, SYMBOL
	}

	public Summon(StatsSet set)
	{
		super(set);

		_summonType = Enum.valueOf(SummonType.class, set.getString("summonType", "PET").toUpperCase());
		_expPenalty = set.getDouble("expPenalty", 0.f);
		_itemConsumeIdInTime = set.getInteger("itemConsumeIdInTime", 0);
		_itemConsumeCountInTime = set.getInteger("itemConsumeCountInTime", 0);
		_itemConsumeDelay = set.getInteger("itemConsumeDelay", 240) * 1000;
		_lifeTime = set.getInteger("lifeTime", 1200) * 1000;
		_lifeTimeClone = set.getInteger("lifeTime", 120) * 1000;
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player player = activeChar.getPlayer();
		if(player == null)
		{
			return false;
		}
		if(player.isProcessingRequest())
		{
			player.sendPacket(Msg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}

		switch(_summonType)
		{
			case TRAP:
				if(player.isInZonePeace())
				{
					activeChar.sendPacket(Msg.A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE);
					return false;
				}
				break;
			case CLONE:
				// TODO: Спавн клона чара
				break;
			case TREE:
				if(player.getTree())
				{
					player.sendMessage("You already have a tree.");
					return false;
				}
				break;
			case SERVITOR:

			case MULTI_SERVITOR:
			case PET:
			{
				if(getNpcId() == 0)
				{
					System.out.println("[WARNING]: Error in skill id:" + getId());
					return false;
				}
				NpcTemplate summonTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				if(summonTemplate == null)
				{
					System.out.println("[WARNING]: Error in skill id:" + getId());
					return false;
				}
				if(!player.canSummon(getNpcId()) || player.isMounted())
				{
					player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
					return false;
				}
				break;
			}
			case SIEGE_SUMMON:
				if(player.getFirstPet() == null && !player.isMounted())
				{
					break;
				}
				player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
				return false;
			case AGATHION:
				if(player.getAgathionId() <= 0 || _npcId == 0)
				{
					break;
				}
				player.sendPacket(SystemMsg.AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED);
				return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature caster, GArray<Creature> targets)
	{
		Player activeChar = caster.getPlayer();

		switch(_summonType)
		{
			case AGATHION:
				activeChar.setAgathion(getNpcId());
				break;
			case TRAP:
				Skill trapSkill = getFirstAddedSkill();

				if(activeChar.getTrapsCount() >= 5)
				{
					activeChar.destroyFirstTrap();
				}
				TrapInstance trap = new TrapInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, trapSkill);
				activeChar.addTrap(trap);
				trap.spawnMe();
				break;
			case CLONE:
				if(activeChar.isMounted())
				{
					return;
				}
				Location cloneLoc = Location.findAroundPosition(activeChar, 50, 70);
				CloneInstance clone = new CloneInstance(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar, _lifeTimeClone, cloneLoc);
				clone.setHeading(activeChar.getHeading());
				clone.setReflection(activeChar.getReflection());
				clone.setCurrentCp(activeChar.getCurrentCp());
				clone.setCurrentHpMp(activeChar.getCurrentHp(), activeChar.getCurrentMp());
				clone.spawnMe(cloneLoc);
				clone.setRunning();
				clone.setFollowMode(true);

				cloneLoc = Location.findAroundPosition(activeChar, 50, 70);
				clone = new CloneInstance(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar, _lifeTimeClone, cloneLoc);
				clone.setHeading(activeChar.getHeading());
				clone.setReflection(activeChar.getReflection());
				clone.setCurrentCp(activeChar.getCurrentCp());
				clone.setCurrentHpMp(activeChar.getCurrentHp(), activeChar.getCurrentMp());
				clone.spawnMe(cloneLoc);
				clone.setRunning();
				clone.setFollowMode(true);

				cloneLoc = Location.findAroundPosition(activeChar, 50, 70);
				clone = new CloneInstance(IdFactory.getInstance().getNextId(), activeChar.getTemplate(), activeChar, _lifeTimeClone, cloneLoc);
				clone.setHeading(activeChar.getHeading());
				clone.setReflection(activeChar.getReflection());
				clone.setCurrentCp(activeChar.getCurrentCp());
				clone.setCurrentHpMp(activeChar.getCurrentHp(), activeChar.getCurrentMp());
				clone.spawnMe(cloneLoc);
				clone.setRunning();
				clone.setFollowMode(true);
				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(clone), _lifeTime);
				break;
			case TREE:
				if(activeChar.getTree())
				{
					activeChar.sendMessage("You already have a tree.");
					return;
				}
				Skill treeSkill = getFirstAddedSkill();
				TreeInstance tree = new TreeInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, _lifeTime, treeSkill);
				activeChar.setTree(true);
				tree.spawnMe(Location.findAroundPosition(activeChar, 50, 70));
				break;
			case PET:
			case SIEGE_SUMMON:
				// Удаление трупа, если идет суммон из трупа.
				Location loc = null;
				if(_targetType == Skill.SkillTargetType.TARGET_CORPSE)
				{
					for(Creature target : targets)
					{
						if(target != null && target.isDead())
						{
							activeChar.getAI().setAttackTarget(null);
							loc = target.getLoc();
							if(target.isNpc())
							{
								((NpcInstance) target).endDecayTask();
							}
							else if(target.isSummon())
							{
								((SummonInstance) target).endDecayTask();
							}
							else
							{
								return;
							}
						}
					}
				}
				NpcTemplate summonTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				if(!activeChar.canSummon(getNpcId()) || activeChar.isMounted())
				{
					activeChar.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
					return;
				}

				SummonInstance summon = new SummonInstance(IdFactory.getInstance().getNextId(), summonTemplate, activeChar, _lifeTime, _itemConsumeIdInTime, _itemConsumeCountInTime, _itemConsumeDelay, this);
				activeChar.addPet(summon);

				summon.setTitle(activeChar.getName());
				summon.setExpPenalty(_expPenalty);
				summon.setExp(Experience.LEVEL[java.lang.Math.min(summon.getLevel(), Experience.LEVEL.length - 1)]);
				summon.setHeading(activeChar.getHeading());
				summon.setReflection(activeChar.getReflection());
				summon.spawnMe(loc == null ? Location.findAroundPosition(activeChar, 50, 70) : loc);
				summon.setRunning();
				summon.setFollowMode(true);

				if(summon.getSkillLevel(Integer.valueOf(4140)) > 0)
				{
					summon.altUseSkill(SkillTable.getInstance().getInfo(4140, summon.getSkillLevel(Integer.valueOf(4140))), activeChar);
				}
				if(summon.getName().equalsIgnoreCase("Shadow"))
				{
					summon.addStatFunc(new FuncAdd(Stats.ABSORB_DAMAGE_PERCENT, 64, this, 15.0D));
				}
				EffectsDAO.getInstance().restoreEffects(summon);
				if(activeChar.isInOlympiadMode())
				{
					summon.getEffectList().stopAllEffects();
				}
				summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp(), false);

				if(_summonType != SummonType.SIEGE_SUMMON)
				{
					break;
				}
				SiegeEvent siegeEvent = activeChar.getEvent(SiegeEvent.class);

				siegeEvent.addSiegeSummon(summon);
				break;
			case MERCHANT:
				if(activeChar.getFirstPet() != null || activeChar.isMounted())
				{
					return;
				}

				NpcTemplate merchantTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				MerchantInstance merchant = new MerchantInstance(IdFactory.getInstance().getNextId(), merchantTemplate);

				merchant.setCurrentHp(merchant.getMaxHp(), false);
				merchant.setCurrentMp(merchant.getMaxMp());
				merchant.setHeading(activeChar.getHeading());
				merchant.setReflection(activeChar.getReflection());
				merchant.spawnMe(activeChar.getLoc());

				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(merchant), _lifeTime);
				break;
			case SYMBOL:
				if(activeChar.isMounted())
				{
					return;
				}
				NpcTemplate symbolTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				Location symbolLoc;
				if(activeChar.getGroundSkillLoc() != null)
				{
					symbolLoc = activeChar.getGroundSkillLoc();
				}
				else
				{
					symbolLoc = activeChar.getLoc();
				}
				Skill symbolSkill = getFirstAddedSkill();
				SymbolInstance symbol = new SymbolInstance(IdFactory.getInstance().getNextId(), symbolTemplate, activeChar, symbolSkill);
				symbol.setReflection(activeChar.getReflection());
				symbol.setShowName(false);
				symbol.spawnMe(symbolLoc);
				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(symbol), _lifeTime);
				break;
			case DEADS_GATE:
				if(activeChar.isMounted())
				{
					return;
				}
				NpcTemplate deadsGateTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				Location deadsGateLoc;
				if(activeChar.getGroundSkillLoc() != null)
				{
					deadsGateLoc = activeChar.getGroundSkillLoc();
				}
				else
				{
					deadsGateLoc = activeChar.getLoc();
				}
				Skill deadsGateSkill = getFirstAddedSkill();
				Skill deadsGateSkill2 = getSecondAddedSkill();
				DeadsGateInstance deadsGate = new DeadsGateInstance(IdFactory.getInstance().getNextId(), deadsGateTemplate, activeChar, deadsGateSkill, deadsGateSkill2);
				deadsGate.setReflection(activeChar.getReflection());
				deadsGate.setShowName(false);
				deadsGate.spawnMe(deadsGateLoc);
				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(deadsGate), _lifeTime);
				if(deadsGateTemplate == null)
				{
					activeChar.getEffectList().stopEffect(deadsGateSkill);
					activeChar.getEffectList().stopEffect(deadsGateSkill2);
				}
				break;
		}

		if(isSSPossible())
		{
			caster.unChargeShots(isMagic());
		}
	}

	@Override
	public boolean isOffensive()
	{
		return _targetType == SkillTargetType.TARGET_CORPSE;
	}
}