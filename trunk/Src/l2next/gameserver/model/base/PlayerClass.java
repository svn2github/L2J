package l2next.gameserver.model.base;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import static l2next.gameserver.model.base.ClassLevel.First;
import static l2next.gameserver.model.base.ClassLevel.Fourth;
import static l2next.gameserver.model.base.ClassLevel.Second;
import static l2next.gameserver.model.base.ClassLevel.Third;
import static l2next.gameserver.model.base.ClassType.FIGHTER;
import static l2next.gameserver.model.base.ClassType.MYSTIC;
import static l2next.gameserver.model.base.ClassType.PRIEST;
import static l2next.gameserver.model.base.Race.darkelf;
import static l2next.gameserver.model.base.Race.dwarf;
import static l2next.gameserver.model.base.Race.elf;
import static l2next.gameserver.model.base.Race.human;
import static l2next.gameserver.model.base.Race.kamael;
import static l2next.gameserver.model.base.Race.orc;

public enum PlayerClass
{
	HumanFIGHTER(human, FIGHTER, First), //0
	Warrior(human, FIGHTER, Second), //1
	Gladiator(human, FIGHTER, Third), //2
	Warlord(human, FIGHTER, Third), //3
	HumanKnight(human, FIGHTER, Second), //4
	Paladin(human, FIGHTER, Third), //5
	DarkAvenger(human, FIGHTER, Third), //6
	Rogue(human, FIGHTER, Second), //7
	TreasureHunter(human, FIGHTER, Third), //8
	Hawkeye(human, FIGHTER, Third), //9
	HumanMystic(human, MYSTIC, First), //10
	HumanWizard(human, MYSTIC, Second), //11
	Sorceror(human, MYSTIC, Third), //12
	Necromancer(human, MYSTIC, Third), //13
	Warlock(human, MYSTIC, Third), //14
	Cleric(human, PRIEST, Second), //15
	Bishop(human, PRIEST, Third), //16
	Prophet(human, PRIEST, Third), //17

	ElvenFIGHTER(elf, FIGHTER, First), //18
	ElvenKnight(elf, FIGHTER, Second), //19
	TempleKnight(elf, FIGHTER, Third), //20
	Swordsinger(elf, FIGHTER, Third), //21
	ElvenScout(elf, FIGHTER, Second), //22
	Plainswalker(elf, FIGHTER, Third), //23
	SilverRanger(elf, FIGHTER, Third), //24
	ElvenMystic(elf, MYSTIC, First), //25
	ElvenWizard(elf, MYSTIC, Second), //26
	Spellsinger(elf, MYSTIC, Third), //27
	ElementalSummoner(elf, MYSTIC, Third), //28
	ElvenOracle(elf, PRIEST, Second), //29
	ElvenElder(elf, PRIEST, Third), //30

	DarkElvenFIGHTER(darkelf, FIGHTER, First), //31
	PalusKnight(darkelf, FIGHTER, Second), //32
	ShillienKnight(darkelf, FIGHTER, Third), //33
	Bladedancer(darkelf, FIGHTER, Third), //34
	Assassin(darkelf, FIGHTER, Second), //35
	AbyssWalker(darkelf, FIGHTER, Third), //36
	PhantomRanger(darkelf, FIGHTER, Third), //37
	DarkElvenMystic(darkelf, MYSTIC, First), //38
	DarkElvenWizard(darkelf, MYSTIC, Second), //39
	Spellhowler(darkelf, MYSTIC, Third), //40
	PhantomSummoner(darkelf, MYSTIC, Third), //41
	ShillienOracle(darkelf, PRIEST, Second), //42
	ShillienElder(darkelf, PRIEST, Third), //43

	OrcFIGHTER(orc, FIGHTER, First), //44
	orcRaider(orc, FIGHTER, Second), //45
	Destroyer(orc, FIGHTER, Third), //46
	orcMonk(orc, FIGHTER, Second), //47
	Tyrant(orc, FIGHTER, Third), //48
	orcMystic(orc, MYSTIC, First), //49
	orcShaman(orc, MYSTIC, Second), //50
	Overlord(orc, MYSTIC, Third), //51
	Warcryer(orc, MYSTIC, Third), //52

	DwarvenFIGHTER(dwarf, FIGHTER, First), //53
	DwarvenScavenger(dwarf, FIGHTER, Second), //54
	BountyHunter(dwarf, FIGHTER, Third), //55
	DwarvenArtisan(dwarf, FIGHTER, Second), //56
	Warsmith(dwarf, FIGHTER, Third), //57

	DummyEntry1(null, null, null), //58
	DummyEntry2(null, null, null), //59
	DummyEntry3(null, null, null), //60
	DummyEntry4(null, null, null), //61
	DummyEntry5(null, null, null), //62
	DummyEntry6(null, null, null), //63
	DummyEntry7(null, null, null), //64
	DummyEntry8(null, null, null), //65
	DummyEntry9(null, null, null), //66
	DummyEntry10(null, null, null), //67
	DummyEntry11(null, null, null), //68
	DummyEntry12(null, null, null), //69
	DummyEntry13(null, null, null), //70
	DummyEntry14(null, null, null), //71
	DummyEntry15(null, null, null), //72
	DummyEntry16(null, null, null), //73
	DummyEntry17(null, null, null), //74
	DummyEntry18(null, null, null), //75
	DummyEntry19(null, null, null), //76
	DummyEntry20(null, null, null), //77
	DummyEntry21(null, null, null), //78
	DummyEntry22(null, null, null), //79
	DummyEntry23(null, null, null), //80
	DummyEntry24(null, null, null), //81
	DummyEntry25(null, null, null), //82
	DummyEntry26(null, null, null), //83
	DummyEntry27(null, null, null), //84
	DummyEntry28(null, null, null), //85
	DummyEntry29(null, null, null), //86
	DummyEntry30(null, null, null), //87

	Duelist(human, FIGHTER, Fourth), //88
	Dreadnought(human, FIGHTER, Fourth), //89
	PhoenixKnight(human, FIGHTER, Fourth), //90
	HellKnight(human, FIGHTER, Fourth), //91
	Sagittarius(human, FIGHTER, Fourth), //92
	Adventurer(human, FIGHTER, Fourth), //93
	Archmage(human, MYSTIC, Fourth), //94
	Soultaker(human, MYSTIC, Fourth), //95
	ArcanaLord(human, MYSTIC, Fourth), //96
	Cardinal(human, PRIEST, Fourth), //97
	Hierophant(human, PRIEST, Fourth), //98

	EvaTemplar(elf, FIGHTER, Fourth), //99
	SwordMuse(elf, FIGHTER, Fourth), //100
	WindRider(elf, FIGHTER, Fourth), //101
	MoonlightSentinel(elf, FIGHTER, Fourth), //102
	MysticMuse(elf, MYSTIC, Fourth), //103
	ElementalMaster(elf, MYSTIC, Fourth), //104
	EvaSaint(elf, PRIEST, Fourth), //105

	ShillienTemplar(darkelf, FIGHTER, Fourth), //106
	SpectralDancer(darkelf, FIGHTER, Fourth), //107
	GhostHunter(darkelf, FIGHTER, Fourth), //108
	GhostSentinel(darkelf, FIGHTER, Fourth), //109
	StormScreamer(darkelf, MYSTIC, Fourth), //110
	SpectralMaster(darkelf, MYSTIC, Fourth), //111
	ShillienSaint(darkelf, PRIEST, Fourth), //112

	Titan(orc, FIGHTER, Fourth), //113
	GrandKhauatari(orc, FIGHTER, Fourth), //114
	Dominator(orc, MYSTIC, Fourth), //115
	Doomcryer(orc, MYSTIC, Fourth), //116

	FortuneSeeker(dwarf, FIGHTER, Fourth), //117
	Maestro(dwarf, FIGHTER, Fourth), //118

	DummyEntry31(null, null, null), //119
	DummyEntry32(null, null, null), //120
	DummyEntry33(null, null, null), //121
	DummyEntry34(null, null, null), //122

	/** Kamael */
	MaleSoldier(kamael, FIGHTER, First), // 123
	FemaleSoldier(kamael, FIGHTER, First), //124
	Troopier(kamael, FIGHTER, Second), // 125
	Warder(kamael, FIGHTER, Second), //126
	Berserker(kamael, FIGHTER, Third), //127
	MaleSoulbreaker(kamael, FIGHTER, Third), //128
	FemaleSoulbreaker(kamael, FIGHTER, Third), //129
	Arbalester(kamael, FIGHTER, Third), //130

	/** kamael */
	Doombringer(kamael, FIGHTER, Fourth), //131
	MaleSoulHound(kamael, FIGHTER, Fourth), //132
	FemaleSoulHound(kamael, FIGHTER, Fourth), //133
	Trickster(kamael, FIGHTER, Fourth), //134
	Inspector(kamael, FIGHTER, Third), //135
	Judicator(kamael, FIGHTER, Fourth); //136

	public static final PlayerClass[] VALUES = values();

	private Race _race;
	private ClassLevel _level;
	private ClassType _type;

	private static final Set<PlayerClass> mainSubclassSet;
	private static final Set<PlayerClass> kamaelSubclassSet;
	private static final Set<PlayerClass> neverSubclassed = EnumSet.of(Overlord, Warsmith);

	private static final Set<PlayerClass> subclasseSet1 = EnumSet.of(DarkAvenger, Paladin, TempleKnight, ShillienKnight);
	private static final Set<PlayerClass> subclasseSet2 = EnumSet.of(TreasureHunter, AbyssWalker, Plainswalker);
	private static final Set<PlayerClass> subclasseSet3 = EnumSet.of(Hawkeye, SilverRanger, PhantomRanger);
	private static final Set<PlayerClass> subclasseSet4 = EnumSet.of(Warlock, ElementalSummoner, PhantomSummoner);
	private static final Set<PlayerClass> subclasseSet5 = EnumSet.of(Sorceror, Spellsinger, Spellhowler);

	/** kamael SubClasses */
	private static final Set<PlayerClass> subclasseSet6 = EnumSet.of(Inspector);

	private static final EnumMap<PlayerClass, Set<PlayerClass>> subclassSetMap = new EnumMap<PlayerClass, Set<PlayerClass>>(PlayerClass.class);

	static
	{
		kamaelSubclassSet = getSet(kamael, Third);

		Set<PlayerClass> subclasses = getSet(null, Third);
		subclasses.removeAll(neverSubclassed);
		subclasses.removeAll(kamaelSubclassSet);

		mainSubclassSet = subclasses;

		subclassSetMap.put(DarkAvenger, subclasseSet1);
		subclassSetMap.put(HellKnight, subclasseSet1);
		subclassSetMap.put(Paladin, subclasseSet1);
		subclassSetMap.put(PhoenixKnight, subclasseSet1);
		subclassSetMap.put(TempleKnight, subclasseSet1);
		subclassSetMap.put(EvaTemplar, subclasseSet1);
		subclassSetMap.put(ShillienKnight, subclasseSet1);
		subclassSetMap.put(ShillienTemplar, subclasseSet1);

		subclassSetMap.put(TreasureHunter, subclasseSet2);
		subclassSetMap.put(Adventurer, subclasseSet2);
		subclassSetMap.put(AbyssWalker, subclasseSet2);
		subclassSetMap.put(GhostHunter, subclasseSet2);
		subclassSetMap.put(Plainswalker, subclasseSet2);
		subclassSetMap.put(WindRider, subclasseSet2);

		subclassSetMap.put(Hawkeye, subclasseSet3);
		subclassSetMap.put(Sagittarius, subclasseSet3);
		subclassSetMap.put(SilverRanger, subclasseSet3);
		subclassSetMap.put(MoonlightSentinel, subclasseSet3);
		subclassSetMap.put(PhantomRanger, subclasseSet3);
		subclassSetMap.put(GhostSentinel, subclasseSet3);

		subclassSetMap.put(Warlock, subclasseSet4);
		subclassSetMap.put(ArcanaLord, subclasseSet4);
		subclassSetMap.put(ElementalSummoner, subclasseSet4);
		subclassSetMap.put(ElementalMaster, subclasseSet4);
		subclassSetMap.put(PhantomSummoner, subclasseSet4);
		subclassSetMap.put(SpectralMaster, subclasseSet4);

		subclassSetMap.put(Sorceror, subclasseSet5);
		subclassSetMap.put(Archmage, subclasseSet5);
		subclassSetMap.put(Spellsinger, subclasseSet5);
		subclassSetMap.put(MysticMuse, subclasseSet5);
		subclassSetMap.put(Spellhowler, subclasseSet5);
		subclassSetMap.put(StormScreamer, subclasseSet5);

		subclassSetMap.put(Doombringer, subclasseSet6);
		subclassSetMap.put(MaleSoulHound, subclasseSet6);
		subclassSetMap.put(FemaleSoulHound, subclasseSet6);
		subclassSetMap.put(Trickster, subclasseSet6);

		subclassSetMap.put(Duelist, EnumSet.of(Gladiator));
		subclassSetMap.put(Dreadnought, EnumSet.of(Warlord));
		subclassSetMap.put(Soultaker, EnumSet.of(Necromancer));
		subclassSetMap.put(Cardinal, EnumSet.of(Bishop));
		subclassSetMap.put(Hierophant, EnumSet.of(Prophet));
		subclassSetMap.put(SwordMuse, EnumSet.of(Swordsinger));
		subclassSetMap.put(EvaSaint, EnumSet.of(ElvenElder));
		subclassSetMap.put(SpectralDancer, EnumSet.of(Bladedancer));
		subclassSetMap.put(Titan, EnumSet.of(Destroyer));
		subclassSetMap.put(GrandKhauatari, EnumSet.of(Tyrant));
		subclassSetMap.put(Dominator, EnumSet.of(Overlord));
		subclassSetMap.put(Doomcryer, EnumSet.of(Warcryer));
	}

	PlayerClass(Race race, ClassType type, ClassLevel level)
	{
		_race = race;
		_level = level;
		_type = type;
	}

	public final Set<PlayerClass> getAvailableSubclasses()
	{
		if(_race == Race.kamael)
		{
			return EnumSet.copyOf(kamaelSubclassSet);
		}

		Set<PlayerClass> subclasses = null;

		if(_level == Third || _level == Fourth)
		{
			subclasses = EnumSet.copyOf(mainSubclassSet);

			subclasses.removeAll(neverSubclassed);
			subclasses.remove(this);

			switch(_race)
			{
				case elf:
					subclasses.removeAll(getSet(darkelf, Third));
					break;
				case darkelf:
					subclasses.removeAll(getSet(elf, Third));
					break;
			}

			Set<PlayerClass> unavailableClasses = subclassSetMap.get(this);

			if(unavailableClasses != null)
			{
				subclasses.removeAll(unavailableClasses);
			}
		}

		return subclasses;
	}

	public static EnumSet<PlayerClass> getSet(Race race, ClassLevel level)
	{
		EnumSet<PlayerClass> allOf = EnumSet.noneOf(PlayerClass.class);

		for(PlayerClass playerClass : EnumSet.allOf(PlayerClass.class))
		{
			if(race == null || playerClass.isOfRace(race))
			{
				if(level == null || playerClass.isOfLevel(level))
				{
					allOf.add(playerClass);
				}
			}
		}

		return allOf;
	}

	public final boolean isOfRace(Race race)
	{
		return _race == race;
	}

	public final boolean isOfType(ClassType type)
	{
		return _type == type;
	}

	public final boolean isOfLevel(ClassLevel level)
	{
		return _level == level;
	}

	/**
	 * Проверяет принципиальную совместимость двух сабов.
	 */
	public static boolean areClassesComportable(PlayerClass c1, PlayerClass c2)
	{
		if(c1.isOfRace(Race.kamael) != c2.isOfRace(Race.kamael))
		{
			return false; // камаэли только с камаэлями
		}
		if(c1.isOfRace(Race.elf) && c2.isOfRace(Race.darkelf) || c1.isOfRace(Race.darkelf) && c2.isOfRace(Race.elf))
		{
			return false; // эльфы несовместимы с темными
		}
		if(c1 == PlayerClass.Overlord || c1 == PlayerClass.Warsmith || c2 == PlayerClass.Overlord || c2 == PlayerClass.Warsmith)
		{
			return false; // эти вообще
		}
		if(subclassSetMap.get(c1) == subclassSetMap.get(c2))
		{
			return false; // однотипные
		}
		return true;
	}
}
