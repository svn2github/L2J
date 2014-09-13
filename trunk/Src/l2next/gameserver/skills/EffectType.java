package l2next.gameserver.skills;

import l2next.gameserver.model.Effect;
import l2next.gameserver.skills.effects.EffectAddSkills;
import l2next.gameserver.skills.effects.EffectAgathionRes;
import l2next.gameserver.skills.effects.EffectAggression;
import l2next.gameserver.skills.effects.EffectBetray;
import l2next.gameserver.skills.effects.EffectBlessNoblesse;
import l2next.gameserver.skills.effects.EffectBlockStat;
import l2next.gameserver.skills.effects.EffectBluff;
import l2next.gameserver.skills.effects.EffectBuff;
import l2next.gameserver.skills.effects.EffectCPDamPercent;
import l2next.gameserver.skills.effects.EffectCallSkills;
import l2next.gameserver.skills.effects.EffectCallSkillsForTimeBomb;
import l2next.gameserver.skills.effects.EffectChange;
import l2next.gameserver.skills.effects.EffectChangeTarget;
import l2next.gameserver.skills.effects.EffectCharge;
import l2next.gameserver.skills.effects.EffectChargesOverTime;
import l2next.gameserver.skills.effects.EffectChargesTen;
import l2next.gameserver.skills.effects.EffectCharmOfCourage;
import l2next.gameserver.skills.effects.EffectClanBirth;
import l2next.gameserver.skills.effects.EffectCombatPointHealOverTime;
import l2next.gameserver.skills.effects.EffectConsumeSoulsOverTime;
import l2next.gameserver.skills.effects.EffectCubic;
import l2next.gameserver.skills.effects.EffectCurseOfLifeFlow;
import l2next.gameserver.skills.effects.EffectDamOverTime;
import l2next.gameserver.skills.effects.EffectDamOverTimeLethal;
import l2next.gameserver.skills.effects.EffectDebuffImmunity;
import l2next.gameserver.skills.effects.EffectDestroySummon;
import l2next.gameserver.skills.effects.EffectDevilsSway;
import l2next.gameserver.skills.effects.EffectDisarm;
import l2next.gameserver.skills.effects.EffectDiscord;
import l2next.gameserver.skills.effects.EffectDispelEffects;
import l2next.gameserver.skills.effects.EffectDoubleCasting;
import l2next.gameserver.skills.effects.EffectDrainHp;
import l2next.gameserver.skills.effects.EffectElementalyStance;
import l2next.gameserver.skills.effects.EffectEnervation;
import l2next.gameserver.skills.effects.EffectFakeDeath;
import l2next.gameserver.skills.effects.EffectFarAway;
import l2next.gameserver.skills.effects.EffectFear;
import l2next.gameserver.skills.effects.EffectGiantForceAura;
import l2next.gameserver.skills.effects.EffectGrow;
import l2next.gameserver.skills.effects.EffectHPDamPercent;
import l2next.gameserver.skills.effects.EffectHate;
import l2next.gameserver.skills.effects.EffectHeal;
import l2next.gameserver.skills.effects.EffectHealBlock;
import l2next.gameserver.skills.effects.EffectHealCPPercent;
import l2next.gameserver.skills.effects.EffectHealOverTime;
import l2next.gameserver.skills.effects.EffectHealPercent;
import l2next.gameserver.skills.effects.EffectHellBinding;
import l2next.gameserver.skills.effects.EffectHourglass;
import l2next.gameserver.skills.effects.EffectImmobilize;
import l2next.gameserver.skills.effects.EffectInterrupt;
import l2next.gameserver.skills.effects.EffectInvisible;
import l2next.gameserver.skills.effects.EffectInvulnerable;
import l2next.gameserver.skills.effects.EffectKnockBack;
import l2next.gameserver.skills.effects.EffectKnockDown;
import l2next.gameserver.skills.effects.EffectLDManaDamOverTime;
import l2next.gameserver.skills.effects.EffectLockInventory;
import l2next.gameserver.skills.effects.EffectMPDamPercent;
import l2next.gameserver.skills.effects.EffectManaDamOverTime;
import l2next.gameserver.skills.effects.EffectManaHeal;
import l2next.gameserver.skills.effects.EffectManaHealOverTime;
import l2next.gameserver.skills.effects.EffectManaHealPercent;
import l2next.gameserver.skills.effects.EffectMeditation;
import l2next.gameserver.skills.effects.EffectMute;
import l2next.gameserver.skills.effects.EffectMuteAll;
import l2next.gameserver.skills.effects.EffectMuteAttack;
import l2next.gameserver.skills.effects.EffectMutePhisycal;
import l2next.gameserver.skills.effects.EffectNegateEffects;
import l2next.gameserver.skills.effects.EffectNegateMusic;
import l2next.gameserver.skills.effects.EffectParalyze;
import l2next.gameserver.skills.effects.EffectPetrification;
import l2next.gameserver.skills.effects.EffectRandomHate;
import l2next.gameserver.skills.effects.EffectRelax;
import l2next.gameserver.skills.effects.EffectRemoveTarget;
import l2next.gameserver.skills.effects.EffectRestoration;
import l2next.gameserver.skills.effects.EffectRestorationRandom;
import l2next.gameserver.skills.effects.EffectRoot;
import l2next.gameserver.skills.effects.EffectSalvation;
import l2next.gameserver.skills.effects.EffectServitorShare;
import l2next.gameserver.skills.effects.EffectSilentMove;
import l2next.gameserver.skills.effects.EffectSleep;
import l2next.gameserver.skills.effects.EffectStun;
import l2next.gameserver.skills.effects.EffectSymbol;
import l2next.gameserver.skills.effects.EffectTargetLock;
import l2next.gameserver.skills.effects.EffectTargetRemove;
import l2next.gameserver.skills.effects.EffectTargetToMe;
import l2next.gameserver.skills.effects.EffectTargetToOwner;
import l2next.gameserver.skills.effects.EffectTemplate;
import l2next.gameserver.skills.effects.EffectThrowHorizontal;
import l2next.gameserver.skills.effects.EffectTransformation;
import l2next.gameserver.skills.effects.EffectTurner;
import l2next.gameserver.skills.effects.EffectUnAggro;
import l2next.gameserver.skills.effects.EffectVitality;
import l2next.gameserver.stats.Env;
import l2next.gameserver.stats.Stats;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum EffectType
{
	// Основные эффекты
	AddSkills(EffectAddSkills.class, null, false),
	AgathionResurrect(EffectAgathionRes.class, null, true),
	Aggression(EffectAggression.class, null, true),
	Betray(EffectBetray.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	BlessNoblesse(EffectBlessNoblesse.class, null, true),
	BlockStat(EffectBlockStat.class, null, true),
	Buff(EffectBuff.class, null, false),
	Bluff(EffectBluff.class, AbnormalEffect.NULL, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	DebuffImmunity(EffectDebuffImmunity.class, null, true),
	DispelEffects(EffectDispelEffects.class, null, Stats.CANCEL_RESIST, Stats.CANCEL_POWER, true),
	DrainHp(EffectDrainHp.class, null, false),
	CallSkills(EffectCallSkills.class, null, false),
	CallSkillsForTimeBomb(EffectCallSkillsForTimeBomb.class, null, false),
	Change(EffectChange.class, null, false),
	ChangeTarget(EffectChangeTarget.class, null, true),
	CombatPointHealOverTime(EffectCombatPointHealOverTime.class, null, true),
	ConsumeSoulsOverTime(EffectConsumeSoulsOverTime.class, null, true),
	Charge(EffectCharge.class, null, false),
	ChargesOverTime(EffectChargesOverTime.class, null, true),
	ChargesTen(EffectChargesTen.class, null, true),
	CharmOfCourage(EffectCharmOfCourage.class, null, true),
	CPDamPercent(EffectCPDamPercent.class, null, true),
	Cubic(EffectCubic.class, null, true),
	DamOverTime(EffectDamOverTime.class, null, false),
	DamOverTimeLethal(EffectDamOverTimeLethal.class, null, false),
	DestroySummon(EffectDestroySummon.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	Disarm(EffectDisarm.class, null, true),
	Discord(EffectDiscord.class, AbnormalEffect.CONFUSED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	Enervation(EffectEnervation.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, false),
	FakeDeath(EffectFakeDeath.class, null, true),
	FarAway(EffectFarAway.class, null, true),
	Fear(EffectFear.class, AbnormalEffect.AFFRAID, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	Grow(EffectGrow.class, AbnormalEffect.GROW, false),
	GiantForceAura(EffectGiantForceAura.class, null, false),
	Hate(EffectHate.class, null, false),
	Heal(EffectHeal.class, null, false),
	HealBlock(EffectHealBlock.class, null, true),
	HealCPPercent(EffectHealCPPercent.class, null, true),
	HealOverTime(EffectHealOverTime.class, null, false),
	HealPercent(EffectHealPercent.class, null, false),
	HellBinding(EffectHellBinding.class, null, false),
	HPDamPercent(EffectHPDamPercent.class, null, true),
	IgnoreSkill(EffectBuff.class, null, false),
	Immobilize(EffectImmobilize.class, null, true),
	Interrupt(EffectInterrupt.class, null, true),
	Invulnerable(EffectInvulnerable.class, null, false),
	Invisible(EffectInvisible.class, null, false),
	LockInventory(EffectLockInventory.class, null, false),
	CurseOfLifeFlow(EffectCurseOfLifeFlow.class, null, true),
	LDManaDamOverTime(EffectLDManaDamOverTime.class, null, true),
	ManaDamOverTime(EffectManaDamOverTime.class, null, true),
	ManaHeal(EffectManaHeal.class, null, false),
	ManaHealOverTime(EffectManaHealOverTime.class, null, false),
	ManaHealPercent(EffectManaHealPercent.class, null, false),
	Meditation(EffectMeditation.class, null, false),
	MPDamPercent(EffectMPDamPercent.class, null, true),
	Mute(EffectMute.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	MuteAll(EffectMuteAll.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	MuteAttack(EffectMuteAttack.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	MutePhisycal(EffectMutePhisycal.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	NegateEffects(EffectNegateEffects.class, null, false),
	NegateMusic(EffectNegateMusic.class, null, false),
	Paralyze(EffectParalyze.class, AbnormalEffect.HOLD_1, Stats.PARALYZE_RESIST, Stats.PARALYZE_POWER, true),
	Petrification(EffectPetrification.class, AbnormalEffect.HOLD_2, Stats.PARALYZE_RESIST, Stats.PARALYZE_POWER, true),
	RandomHate(EffectRandomHate.class, null, true),
	Relax(EffectRelax.class, null, true),
	RemoveTarget(EffectRemoveTarget.class, null, true),
	RestorationRandom(EffectRestorationRandom.class, null, true), // TODO задействовать
	Restoration(EffectRestoration.class, null, true), // TODO задействовать
	Root(EffectRoot.class, AbnormalEffect.ROOT, Stats.ROOT_RESIST, Stats.ROOT_POWER, true),
	Hourglass(EffectHourglass.class, null, true),
	Salvation(EffectSalvation.class, null, true),
	ServitorShare(EffectServitorShare.class, null, true),
	SilentMove(EffectSilentMove.class, AbnormalEffect.STEALTH, true),
	Sleep(EffectSleep.class, AbnormalEffect.SLEEP, Stats.SLEEP_RESIST, Stats.SLEEP_POWER, true),
	Stun(EffectStun.class, AbnormalEffect.STUN, Stats.STUN_RESIST, Stats.STUN_POWER, true),
	Symbol(EffectSymbol.class, null, false),
	// Talisman(EffectTalisman.class, null, true),
	Transformation(EffectTransformation.class, null, true),
	TargetLock(EffectTargetLock.class, null, true),
	UnAggro(EffectUnAggro.class, null, true),
	Vitality(EffectVitality.class, AbnormalEffect.VITALITY, true),
	VitalityFreeze(EffectVitality.class, null, true),
	Turner(EffectTurner.class, null, false), // TODO задействовать
	ClanBirth(EffectClanBirth.class, null, true),
	TargetToMe(EffectTargetToMe.class, null, true),
	TargetRemove(EffectTargetRemove.class, null, true),
	TargetToOwner(EffectTargetToOwner.class, null, true),
	ThrowHorizontal(EffectThrowHorizontal.class, null, true),

	// Производные от основных эффектов
	Poison(EffectDamOverTime.class, null, Stats.POISON_RESIST, Stats.POISON_POWER, false),
	PoisonLethal(EffectDamOverTimeLethal.class, null, Stats.POISON_RESIST, Stats.POISON_POWER, false),
	Bleed(EffectDamOverTime.class, null, Stats.BLEED_RESIST, Stats.BLEED_POWER, false),
	Debuff(EffectBuff.class, null, false),
	IgnoreDeath(EffectBuff.class, null, false),
	WatcherGaze(EffectBuff.class, null, false),
	KnockDown(EffectKnockDown.class, null, true),
	KnockBack(EffectKnockBack.class, null, true),
	SubClassPenalty(EffectBuff.class, null, false),
	ElementalyStance(EffectElementalyStance.class, null, false),
	DoubleCasting(EffectDoubleCasting.class, null, false),
	AbsorbDamageToEffector(EffectBuff.class, null, false), // абсорбирует часть дамага к еффектора еффекта
	AbsorbDamageToMp(EffectBuff.class, null, false), // абсорбирует часть дамага в мп
	AbsorbDamageToSummon(EffectLDManaDamOverTime.class, null, true), // абсорбирует часть дамага к сумону
	DevilsSway(EffectDevilsSway.class, null, true);

	private final Constructor<? extends Effect> _constructor;
	private final AbnormalEffect _abnormal;
	private final Stats _resistType;
	private final Stats _attributeType;
	private final boolean _isRaidImmune;

	private EffectType(Class<? extends Effect> clazz, AbnormalEffect abnormal, boolean isRaidImmune)
	{
		this(clazz, abnormal, null, null, isRaidImmune);
	}

	private EffectType(Class<? extends Effect> clazz, AbnormalEffect abnormal, Stats resistType, Stats attributeType, boolean isRaidImmune)
	{
		try
		{
			_constructor = clazz.getConstructor(Env.class, EffectTemplate.class);
		}
		catch(NoSuchMethodException e)
		{
			throw new Error(e);
		}
		_abnormal = abnormal;
		_resistType = resistType;
		_attributeType = attributeType;
		_isRaidImmune = isRaidImmune;
	}

	public AbnormalEffect getAbnormal()
	{
		return _abnormal;
	}

	public Stats getResistType()
	{
		return _resistType;
	}

	public Stats getAttributeType()
	{
		return _attributeType;
	}

	public boolean isRaidImmune()
	{
		return _isRaidImmune;
	}

	public Effect makeEffect(Env env, EffectTemplate template) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		return _constructor.newInstance(env, template);
	}
}