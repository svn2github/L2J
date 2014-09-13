package l2next.gameserver.model.base;

import l2next.gameserver.data.xml.holder.ClassDataHolder;
import l2next.gameserver.templates.player.ClassData;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines all classes (ex : HUMAN fighter, darkFighter...) that a player can chose.<BR>
 * <BR>
 * <p/>
 * Data :<BR>
 * <BR>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId for male or null if this class is the root</li>
 * <li>parent2 : The parent2 ClassId for female or null if parent2 like parent</li>
 * <li>level : The child level of this Class</li><BR>
 * <BR>
 */
public enum ClassId
{
	HUMAN_FIGHTER(0, ClassType.FIGHTER, Race.human, null, ClassLevel.First, null),
	WARRIOR(1, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.Second, null),
	GLADIATOR(2, ClassType.FIGHTER, Race.human, WARRIOR, ClassLevel.Third, ClassType2.Warrior),
	WARLORD(3, ClassType.FIGHTER, Race.human, WARRIOR, ClassLevel.Third, ClassType2.Warrior),
	KNIGHT(4, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.Second, null),
	PALADIN(5, ClassType.FIGHTER, Race.human, KNIGHT, ClassLevel.Third, ClassType2.Knight),
	DARK_AVENGER(6, ClassType.FIGHTER, Race.human, KNIGHT, ClassLevel.Third, ClassType2.Knight),
	ROGUE(7, ClassType.FIGHTER, Race.human, HUMAN_FIGHTER, ClassLevel.Second, null),
	TREASURE_HUNTER(8, ClassType.FIGHTER, Race.human, ROGUE, ClassLevel.Third, ClassType2.Rogue),
	HAWKEYE(9, ClassType.FIGHTER, Race.human, ROGUE, ClassLevel.Third, ClassType2.Rogue),

	HUMAN_MAGE(10, ClassType.MYSTIC, Race.human, null, ClassLevel.First, null),
	WIZARD(11, ClassType.MYSTIC, Race.human, HUMAN_MAGE, ClassLevel.Second, null),
	SORCERER(12, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.Third, ClassType2.Wizard),
	NECROMANCER(13, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.Third, ClassType2.Wizard),
	WARLOCK(14, ClassType.MYSTIC, Race.human, WIZARD, ClassLevel.Third, ClassType2.Summoner),
	CLERIC(15, ClassType.PRIEST, Race.human, HUMAN_MAGE, ClassLevel.Second, null),
	BISHOP(16, ClassType.PRIEST, Race.human, CLERIC, ClassLevel.Third, ClassType2.Healer),
	PROPHET(17, ClassType.PRIEST, Race.human, CLERIC, ClassLevel.Third, ClassType2.Enchanter),

	ELVEN_FIGHTER(18, ClassType.FIGHTER, Race.elf, null, ClassLevel.First, null),
	ELVEN_KNIGHT(19, ClassType.FIGHTER, Race.elf, ELVEN_FIGHTER, ClassLevel.Second, null),
	TEMPLE_KNIGHT(20, ClassType.FIGHTER, Race.elf, ELVEN_KNIGHT, ClassLevel.Third, ClassType2.Knight),
	SWORDSINGER(21, ClassType.FIGHTER, Race.elf, ELVEN_KNIGHT, ClassLevel.Third, ClassType2.Enchanter),
	ELVEN_SCOUT(22, ClassType.FIGHTER, Race.elf, ELVEN_FIGHTER, ClassLevel.Second, null),
	PLAIN_WALKER(23, ClassType.FIGHTER, Race.elf, ELVEN_SCOUT, ClassLevel.Third, ClassType2.Rogue),
	SILVER_RANGER(24, ClassType.FIGHTER, Race.elf, ELVEN_SCOUT, ClassLevel.Third, ClassType2.Rogue),

	ELVEN_MAGE(25, ClassType.MYSTIC, Race.elf, null, ClassLevel.First, null),
	ELVEN_WIZARD(26, ClassType.MYSTIC, Race.elf, ELVEN_MAGE, ClassLevel.Second, null),
	SPELLSINGER(27, ClassType.MYSTIC, Race.elf, ELVEN_WIZARD, ClassLevel.Third, ClassType2.Wizard),
	ELEMENTAL_SUMMONER(28, ClassType.MYSTIC, Race.elf, ELVEN_WIZARD, ClassLevel.Third, ClassType2.Summoner),
	ORACLE(29, ClassType.PRIEST, Race.elf, ELVEN_MAGE, ClassLevel.Second, null),
	ELDER(30, ClassType.PRIEST, Race.elf, ORACLE, ClassLevel.Third, ClassType2.Healer),

	DARK_FIGHTER(31, ClassType.FIGHTER, Race.darkelf, null, ClassLevel.First, null),
	PALUS_KNIGHT(32, ClassType.FIGHTER, Race.darkelf, DARK_FIGHTER, ClassLevel.Second, null),
	SHILLEN_KNIGHT(33, ClassType.FIGHTER, Race.darkelf, PALUS_KNIGHT, ClassLevel.Third, ClassType2.Knight),
	BLADEDANCER(34, ClassType.FIGHTER, Race.darkelf, PALUS_KNIGHT, ClassLevel.Third, ClassType2.Enchanter),
	ASSASIN(35, ClassType.FIGHTER, Race.darkelf, DARK_FIGHTER, ClassLevel.Second, null),
	ABYSS_WALKER(36, ClassType.FIGHTER, Race.darkelf, ASSASIN, ClassLevel.Third, ClassType2.Rogue),
	PHANTOM_RANGER(37, ClassType.FIGHTER, Race.darkelf, ASSASIN, ClassLevel.Third, ClassType2.Rogue),

	DARK_MAGE(38, ClassType.MYSTIC, Race.darkelf, null, ClassLevel.First, null),
	DARK_WIZARD(39, ClassType.MYSTIC, Race.darkelf, DARK_MAGE, ClassLevel.Second, null),
	SPELLHOWLER(40, ClassType.MYSTIC, Race.darkelf, DARK_WIZARD, ClassLevel.Third, ClassType2.Wizard),
	PHANTOM_SUMMONER(41, ClassType.MYSTIC, Race.darkelf, DARK_WIZARD, ClassLevel.Third, ClassType2.Summoner),
	SHILLEN_ORACLE(42, ClassType.PRIEST, Race.darkelf, DARK_MAGE, ClassLevel.Second, null),
	SHILLEN_ELDER(43, ClassType.PRIEST, Race.darkelf, SHILLEN_ORACLE, ClassLevel.Third, ClassType2.Healer),

	ORC_FIGHTER(44, ClassType.FIGHTER, Race.orc, null, ClassLevel.First, null),
	ORC_RAIDER(45, ClassType.FIGHTER, Race.orc, ORC_FIGHTER, ClassLevel.Second, null),
	DESTROYER(46, ClassType.FIGHTER, Race.orc, ORC_RAIDER, ClassLevel.Third, ClassType2.Warrior),
	ORC_MONK(47, ClassType.FIGHTER, Race.orc, ORC_FIGHTER, ClassLevel.Second, null),
	TYRANT(48, ClassType.FIGHTER, Race.orc, ORC_MONK, ClassLevel.Third, ClassType2.Warrior),

	ORC_MAGE(49, ClassType.MYSTIC, Race.orc, null, ClassLevel.First, null),
	ORC_SHAMAN(50, ClassType.MYSTIC, Race.orc, ORC_MAGE, ClassLevel.Second, null),
	OVERLORD(51, ClassType.MYSTIC, Race.orc, ORC_SHAMAN, ClassLevel.Third, ClassType2.Enchanter),
	WARCRYER(52, ClassType.MYSTIC, Race.orc, ORC_SHAMAN, ClassLevel.Third, ClassType2.Enchanter),

	DWARVEN_FIGHTER(53, ClassType.FIGHTER, Race.dwarf, null, ClassLevel.First, null),
	SCAVENGER(54, ClassType.FIGHTER, Race.dwarf, DWARVEN_FIGHTER, ClassLevel.Second, null),
	BOUNTY_HUNTER(55, ClassType.FIGHTER, Race.dwarf, SCAVENGER, ClassLevel.Third, ClassType2.Warrior),
	ARTISAN(56, ClassType.FIGHTER, Race.dwarf, DWARVEN_FIGHTER, ClassLevel.Second, null),
	WARSMITH(57, ClassType.FIGHTER, Race.dwarf, ARTISAN, ClassLevel.Third, ClassType2.Warrior),

	DUMMY_ENTRY_1(58, null, null, null, null, null),
	DUMMY_ENTRY_2(59, null, null, null, null, null),
	DUMMY_ENTRY_3(60, null, null, null, null, null),
	DUMMY_ENTRY_4(61, null, null, null, null, null),
	DUMMY_ENTRY_5(62, null, null, null, null, null),
	DUMMY_ENTRY_6(63, null, null, null, null, null),
	DUMMY_ENTRY_7(64, null, null, null, null, null),
	DUMMY_ENTRY_8(65, null, null, null, null, null),
	DUMMY_ENTRY_9(66, null, null, null, null, null),
	DUMMY_ENTRY_10(67, null, null, null, null, null),
	DUMMY_ENTRY_11(68, null, null, null, null, null),
	DUMMY_ENTRY_12(69, null, null, null, null, null),
	DUMMY_ENTRY_13(70, null, null, null, null, null),
	DUMMY_ENTRY_14(71, null, null, null, null, null),
	DUMMY_ENTRY_15(72, null, null, null, null, null),
	DUMMY_ENTRY_16(73, null, null, null, null, null),
	DUMMY_ENTRY_17(74, null, null, null, null, null),
	DUMMY_ENTRY_18(75, null, null, null, null, null),
	DUMMY_ENTRY_19(76, null, null, null, null, null),
	DUMMY_ENTRY_20(77, null, null, null, null, null),
	DUMMY_ENTRY_21(78, null, null, null, null, null),
	DUMMY_ENTRY_22(79, null, null, null, null, null),
	DUMMY_ENTRY_23(80, null, null, null, null, null),
	DUMMY_ENTRY_24(81, null, null, null, null, null),
	DUMMY_ENTRY_25(82, null, null, null, null, null),
	DUMMY_ENTRY_26(83, null, null, null, null, null),
	DUMMY_ENTRY_27(84, null, null, null, null, null),
	DUMMY_ENTRY_28(85, null, null, null, null, null),
	DUMMY_ENTRY_29(86, null, null, null, null, null),
	DUMMY_ENTRY_30(87, null, null, null, null, null),
	DUELIST(88, ClassType.FIGHTER, Race.human, GLADIATOR, ClassLevel.Fourth, ClassType2.Warrior),
	DREADNOUGHT(89, ClassType.FIGHTER, Race.human, WARLORD, ClassLevel.Fourth, ClassType2.Warrior),
	PHOENIX_KNIGHT(90, ClassType.FIGHTER, Race.human, PALADIN, ClassLevel.Fourth, ClassType2.Knight),
	HELL_KNIGHT(91, ClassType.FIGHTER, Race.human, DARK_AVENGER, ClassLevel.Fourth, ClassType2.Knight),
	SAGITTARIUS(92, ClassType.FIGHTER, Race.human, HAWKEYE, ClassLevel.Fourth, ClassType2.Rogue),
	ADVENTURER(93, ClassType.FIGHTER, Race.human, TREASURE_HUNTER, ClassLevel.Fourth, ClassType2.Rogue),
	ARCHMAGE(94, ClassType.MYSTIC, Race.human, SORCERER, ClassLevel.Fourth, ClassType2.Wizard),
	SOULTAKER(95, ClassType.MYSTIC, Race.human, NECROMANCER, ClassLevel.Fourth, ClassType2.Wizard),
	ARCANA_LORD(96, ClassType.MYSTIC, Race.human, WARLOCK, ClassLevel.Fourth, ClassType2.Summoner),
	CARDINAL(97, ClassType.PRIEST, Race.human, BISHOP, ClassLevel.Fourth, ClassType2.Healer),
	HIEROPHANT(98, ClassType.PRIEST, Race.human, PROPHET, ClassLevel.Fourth, ClassType2.Enchanter),
	EVAS_TEMPLAR(99, ClassType.FIGHTER, Race.elf, TEMPLE_KNIGHT, ClassLevel.Fourth, ClassType2.Knight),
	SWORD_MUSE(100, ClassType.FIGHTER, Race.elf, SWORDSINGER, ClassLevel.Fourth, ClassType2.Enchanter),
	WIND_RIDER(101, ClassType.FIGHTER, Race.elf, PLAIN_WALKER, ClassLevel.Fourth, ClassType2.Rogue),
	MOONLIGHT_SENTINEL(102, ClassType.FIGHTER, Race.elf, SILVER_RANGER, ClassLevel.Fourth, ClassType2.Rogue),
	MYSTIC_MUSE(103, ClassType.MYSTIC, Race.elf, SPELLSINGER, ClassLevel.Fourth, ClassType2.Wizard),
	ELEMENTAL_MASTER(104, ClassType.MYSTIC, Race.elf, ELEMENTAL_SUMMONER, ClassLevel.Fourth, ClassType2.Summoner),
	EVAS_SAINT(105, ClassType.PRIEST, Race.elf, ELDER, ClassLevel.Fourth, ClassType2.Healer),
	SHILLIEN_TEMPLAR(106, ClassType.FIGHTER, Race.darkelf, SHILLEN_KNIGHT, ClassLevel.Fourth, ClassType2.Knight),
	SPECTRAL_DANCER(107, ClassType.FIGHTER, Race.darkelf, BLADEDANCER, ClassLevel.Fourth, ClassType2.Enchanter),
	GHOST_HUNTER(108, ClassType.FIGHTER, Race.darkelf, ABYSS_WALKER, ClassLevel.Fourth, ClassType2.Rogue),
	GHOST_SENTINEL(109, ClassType.FIGHTER, Race.darkelf, PHANTOM_RANGER, ClassLevel.Fourth, ClassType2.Rogue),
	STORM_SCREAMER(110, ClassType.MYSTIC, Race.darkelf, SPELLHOWLER, ClassLevel.Fourth, ClassType2.Wizard),
	SPECTRAL_MASTER(111, ClassType.MYSTIC, Race.darkelf, PHANTOM_SUMMONER, ClassLevel.Fourth, ClassType2.Summoner),
	SHILLIEN_SAINT(112, ClassType.PRIEST, Race.darkelf, SHILLEN_ELDER, ClassLevel.Fourth, ClassType2.Healer),
	TITAN(113, ClassType.FIGHTER, Race.orc, DESTROYER, ClassLevel.Fourth, ClassType2.Warrior),
	GRAND_KHAVATARI(114, ClassType.FIGHTER, Race.orc, TYRANT, ClassLevel.Fourth, ClassType2.Warrior),
	DOMINATOR(115, ClassType.MYSTIC, Race.orc, OVERLORD, ClassLevel.Fourth, ClassType2.Enchanter),
	DOOMCRYER(116, ClassType.MYSTIC, Race.orc, WARCRYER, ClassLevel.Fourth, ClassType2.Enchanter),
	FORTUNE_SEEKER(117, ClassType.FIGHTER, Race.dwarf, BOUNTY_HUNTER, ClassLevel.Fourth, ClassType2.Warrior),
	MAESTRO(118, ClassType.FIGHTER, Race.dwarf, WARSMITH, ClassLevel.Fourth, ClassType2.Warrior),
	DUMMY_ENTRY_31(119, null, null, null, null, null),
	DUMMY_ENTRY_32(120, null, null, null, null, null),
	DUMMY_ENTRY_33(121, null, null, null, null, null),
	DUMMY_ENTRY_34(122, null, null, null, null, null),
	KAMAEL_M_SOLDIER(123, ClassType.FIGHTER, Race.kamael, null, ClassLevel.First, null),
	KAMAEL_F_SOLDIER(124, ClassType.FIGHTER, Race.kamael, null, ClassLevel.First, null),
	TROOPER(125, ClassType.FIGHTER, Race.kamael, KAMAEL_M_SOLDIER, ClassLevel.Second, null),
	WARDER(126, ClassType.FIGHTER, Race.kamael, KAMAEL_F_SOLDIER, ClassLevel.Second, null),
	BERSERKER(127, ClassType.FIGHTER, Race.kamael, TROOPER, ClassLevel.Third, ClassType2.Warrior),
	M_SOUL_BREAKER(128, ClassType.FIGHTER, Race.kamael, TROOPER, ClassLevel.Third, ClassType2.Warrior),
	F_SOUL_BREAKER(129, ClassType.FIGHTER, Race.kamael, WARDER, ClassLevel.Third, ClassType2.Warrior),
	ARBALESTER(130, ClassType.FIGHTER, Race.kamael, WARDER, ClassLevel.Third, ClassType2.Rogue),
	DOOMBRINGER(131, ClassType.FIGHTER, Race.kamael, BERSERKER, ClassLevel.Fourth, ClassType2.Warrior),
	M_SOUL_HOUND(132, ClassType.FIGHTER, Race.kamael, M_SOUL_BREAKER, ClassLevel.Fourth, ClassType2.Warrior),
	F_SOUL_HOUND(133, ClassType.FIGHTER, Race.kamael, F_SOUL_BREAKER, ClassLevel.Fourth, ClassType2.Warrior),
	TRICKSTER(134, ClassType.FIGHTER, Race.kamael, ARBALESTER, ClassLevel.Fourth, ClassType2.Rogue),
	INSPECTOR(135, ClassType.FIGHTER, Race.kamael, TROOPER, WARDER, ClassLevel.Third, ClassType2.Enchanter),
	JUDICATOR(136, ClassType.FIGHTER, Race.kamael, INSPECTOR, ClassLevel.Fourth, ClassType2.Enchanter),
	DUMMY_ENTRY_35(137, null, null, null, null, null, null),
	DUMMY_ENTRY_36(138, null, null, null, null, null, null),
	// Awaking
	SIGEL_KNIGHT(139, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Knight),
	TYRR_WARRIOR(140, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	OTHELL_ROGUE(141, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	YUL_ARCHER(142, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	FEOH_WIZARD(143, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Wizard),
	ISS_ENCHANTER(144, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Enchanter),
	WYNN_SUMMONER(145, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Summoner),
	AEORE_HEALER(146, ClassType.PRIEST, null, null, null, ClassLevel.Awaking, ClassType2.Healer),
	DUMMY_ENTRY_37(147, null, null, null, null, null, null),
	//Lindvior
	SigelKnight_PhoenixKnight(148, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Knight),
	SigelKnight_HellKnight(149, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Knight),
	SigelKnight_EvaTemplar(150, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Knight),
	SigelKnight_ShillienTemplar(151, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Knight),
	TyrrWarrior_Duelist(152, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	TyrrWarrior_Dreadnought(153, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	TyrrWarrior_Titan(154, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	TyrrWarrior_GrandKhavatari(155, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	TyrrWarrior_Maestro(156, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	TyrrWarrior_Doombringer(157, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Warrior),
	OthellRogue_Adventurer(158, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	OthellRogue_WindRider(159, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	OthellRogue_GhostHunter(160, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	OthellRogue_FortuneSeeker(161, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	YulArcher_Saggitarius(162, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	YulArcher_MoonlightSentinel(163, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	YulArcher_GhostSentinel(164, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	YulArcher_Trickster(165, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Rogue),
	FeohWizard_Archmage(166, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Wizard),
	FeohWizard_Soultaker(167, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Wizard),
	FeohWizard_MysticMuse(168, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Wizard),
	FeohWizard_StormScreamer(169, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Wizard),
	FeohWizard_SOUL_HOUND(170, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Wizard),
	IssEnchanter_Hierophant(171, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Enchanter),
	IssEnchanter_SwordMuse(172, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Enchanter),
	IssEnchanter_SpectralDancer(173, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Enchanter),
	IssEnchanter_Dominator(174, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Enchanter),
	IssEnchanter_Doomcryer(175, ClassType.FIGHTER, null, null, null, ClassLevel.Awaking, ClassType2.Enchanter),
	WynnSummoner_ArcanaLord(176, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Summoner),
	WynnSummoner_ElementalMaster(177, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Summoner),
	WynnSummoner_SpectralMaster(178, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Summoner),
	AeoreHealer_Cardinal(179, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Healer),
	AeoreHealer_EvaSaint(180, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Healer),
	AeoreHealer_ShillenSaint(181, ClassType.MYSTIC, null, null, null, ClassLevel.Awaking, ClassType2.Healer);

	public static final ClassId[] VALUES;
	private final int _id;
	public Race _race;
	private final ClassId _parent;
	private ClassId _parent2;
	private final ClassLevel _level;
	private final ClassType _type;
	private final ClassType2 _type2;

	private ClassId(int id, ClassType classType, Race race, ClassId parent, ClassLevel level, ClassType2 classType2)
	{
		this(id, classType, race, parent, null, level, classType2);
	}

	private ClassId(int id, ClassType classType, Race race, ClassId parent, ClassId parent2, ClassLevel level, ClassType2 classType2)
	{
		_id = id;
		_type = classType;
		_race = race;
		_parent = parent;
		_parent2 = parent2;
		_level = level;
		_type2 = classType2;
	}

	public final int getId()
	{
		return _id;
	}

	public final Race getRace()
	{
		return _race;
	}

	public final boolean isOfRace(Race race)
	{
		return _race == race;
	}

	public final ClassLevel getClassLevel()
	{
		return _level;
	}

	public final boolean isOfLevel(ClassLevel level)
	{
		return _level == level;
	}

	public ClassType getType()
	{
		return _type;
	}

	public final boolean isOfType(ClassType type)
	{
		return _type == type;
	}

	public ClassType2 getType2()
	{
		return _type2;
	}

	public boolean isMage()
	{
		return _type.isMagician();
	}

	public final boolean childOf(ClassId cid)
	{
		if(isOfLevel(ClassLevel.Awaking))
		{
			return cid.getType2() == getType2();
		}
		if(_parent == null)
		{
			return false;
		}
		if(_parent == cid || _parent2 == cid)
		{
			return true;
		}
		return _parent.childOf(cid);
	}
	
	/*public final boolean childOf(ClassId cid)
	{
		if(_parent == null && this._id < 139)
		{
			return false;
		}

		if(_parent == cid || _parent2 == cid)
		{
			return true;
		}

		if(this._id > 146)
		{
			return _childClasses.get(this).contains(cid);
		}

		return _parent.childOf(cid);
	}*/

	/**
	 * Child classes for 4 profession
	 * <p/>
	 */
	public static final Map<ClassId, EnumSet<ClassId>> _childClasses = new HashMap<ClassId, EnumSet<ClassId>>()
	{
		private static final long serialVersionUID = -7431801513003154229L;

		{
			put(ClassId.SigelKnight_PhoenixKnight, EnumSet.of(ClassId.PHOENIX_KNIGHT));
			put(ClassId.SigelKnight_HellKnight, EnumSet.of(ClassId.HELL_KNIGHT));
			put(ClassId.SigelKnight_EvaTemplar, EnumSet.of(ClassId.EVAS_TEMPLAR));
			put(ClassId.SigelKnight_ShillienTemplar, EnumSet.of(ClassId.SHILLIEN_TEMPLAR));

			put(ClassId.TyrrWarrior_Duelist, EnumSet.of(ClassId.DUELIST));
			put(ClassId.TyrrWarrior_Dreadnought, EnumSet.of(ClassId.DREADNOUGHT));
			put(ClassId.TyrrWarrior_Titan, EnumSet.of(ClassId.TITAN));
			put(ClassId.TyrrWarrior_GrandKhavatari, EnumSet.of(ClassId.GRAND_KHAVATARI));
			put(ClassId.TyrrWarrior_Maestro, EnumSet.of(ClassId.MAESTRO));
			put(ClassId.TyrrWarrior_Doombringer, EnumSet.of(ClassId.DOOMBRINGER));

			put(ClassId.OthellRogue_Adventurer, EnumSet.of(ClassId.ADVENTURER));
			put(ClassId.OthellRogue_WindRider, EnumSet.of(ClassId.WIND_RIDER));
			put(ClassId.OthellRogue_GhostHunter, EnumSet.of(ClassId.GHOST_HUNTER));
			put(ClassId.OthellRogue_FortuneSeeker, EnumSet.of(ClassId.FORTUNE_SEEKER));

			put(ClassId.YulArcher_Saggitarius, EnumSet.of(ClassId.SAGITTARIUS));
			put(ClassId.YulArcher_MoonlightSentinel, EnumSet.of(ClassId.MOONLIGHT_SENTINEL));
			put(ClassId.YulArcher_GhostSentinel, EnumSet.of(ClassId.GHOST_SENTINEL));
			put(ClassId.YulArcher_Trickster, EnumSet.of(ClassId.TRICKSTER));

			put(ClassId.FeohWizard_Archmage, EnumSet.of(ClassId.ARCHMAGE));
			put(ClassId.FeohWizard_Soultaker, EnumSet.of(ClassId.SOULTAKER));
			put(ClassId.FeohWizard_MysticMuse, EnumSet.of(ClassId.MYSTIC_MUSE));
			put(ClassId.FeohWizard_StormScreamer, EnumSet.of(ClassId.STORM_SCREAMER));
			put(ClassId.FeohWizard_SOUL_HOUND, EnumSet.of(ClassId.M_SOUL_HOUND, ClassId.F_SOUL_HOUND));

			put(ClassId.IssEnchanter_Hierophant, EnumSet.of(ClassId.HIEROPHANT));
			put(ClassId.IssEnchanter_SwordMuse, EnumSet.of(ClassId.SWORD_MUSE));
			put(ClassId.IssEnchanter_SpectralDancer, EnumSet.of(ClassId.SPECTRAL_DANCER));
			put(ClassId.IssEnchanter_Dominator, EnumSet.of(ClassId.DOMINATOR));
			put(ClassId.IssEnchanter_Doomcryer, EnumSet.of(ClassId.DOOMCRYER));

			put(ClassId.WynnSummoner_ArcanaLord, EnumSet.of(ClassId.ARCANA_LORD));
			put(ClassId.WynnSummoner_ElementalMaster, EnumSet.of(ClassId.ELEMENTAL_MASTER));
			put(ClassId.WynnSummoner_SpectralMaster, EnumSet.of(ClassId.SPECTRAL_MASTER));

			put(ClassId.AeoreHealer_Cardinal, EnumSet.of(ClassId.CARDINAL));
			put(ClassId.AeoreHealer_EvaSaint, EnumSet.of(ClassId.EVAS_SAINT));
			put(ClassId.AeoreHealer_ShillenSaint, EnumSet.of(ClassId.SHILLIEN_SAINT));
		}
	};

	public final boolean equalsOrChildOf(ClassId cid)
	{
		return (this == cid) || (childOf(cid));
	}

	public final ClassId getParent(int sex)
	{
		return (sex == 0) || (_parent2 == null) ? _parent : _parent2;
	}

	public ClassData getClassData()
	{
		return ClassDataHolder.getInstance().getClassData(getId());
	}

	public double getBaseCp(int level)
	{
		return getClassData().getLvlUpData(level).getCP();
	}

	public double getBaseHp(int level)
	{
		return getClassData().getLvlUpData(level).getHP();
	}

	public double getBaseMp(int level)
	{
		return getClassData().getLvlUpData(level).getMP();
	}

	static
	{
		VALUES = values();
	}

	public final int level()
	{
		if(_parent == null)
		{
			return 0;
		}

		return 1 + _parent.level();
	}

}