package l2next.gameserver.skills;

import java.util.NoSuchElementException;

public enum AbnormalEffect
{
	NULL("null", 0),
	BLEEDING("bleeding", 1),
	POISON("poison", 2),
	REDCIRCLE("redcircle", 3),
	ICE("ice", 4),
	AFFRAID("affraid", 5),
	CONFUSED("confused", 6),
	STUN("stun", 7),
	SLEEP("sleep", 8),
	MUTED("muted", 9),
	ROOT("root", 10),
	HOLD_1("hold1", 11),
	HOLD_2("hold2", 12), // эффект Dance of medusa
	UNKNOWN_13("unk13", 13),
	BIG_HEAD("bighead", 14),
	FLAME("flame", 15),
	UNKNOWN_16("unk16", 16), // труп с таким абнормалом становится белым
	GROW("grow", 17),
	FLOATING_ROOT("floatroot", 18),
	DANCE_STUNNED("dancestun", 19), // танцует со звездочками над головой
	FIREROOT_STUN("firerootstun", 20), // красная аура у ног со звездочками над головой
	STEALTH("shadow", 21),
	IMPRISIONING_1("imprison1", 22), // синяя аура на уровне пояса
	IMPRISIONING_2("imprison2", 23), // синяя аура на уровне пояса
	MAGIC_CIRCLE("magiccircle", 24), // большой синий круг вокруг чара
	ICE2("ice2", 25), // небольшая ледяная аура, скорее всего DOT
	EARTHQUAKE("earthquake", 26), // землетрясение
	UNKNOWN_27("unk27", 27),
	INVULNERABLE("invul1", 28), // Ultimate Defence
	VITALITY("vitality", 29), // Vitality херб, красное пламя
	REAL_TARGET("realtarget", 30), // дебафф Real Target (знак над головой)
	DEATH_MARK("deathmark", 31), // голубая морда над головой
	SOUL_SHOCK("soulshock", 32), // голубой череп над головой
	S_INVULNERABLE("invul2", 33, true), // целестиал
	S_AIR_STUN("redglow", 34, true), // непонятное красное облако
	S_AIR_ROOT("redglow2", 35, true), // непонятное красное облако
	S_BAGUETTE_SWORD("baguettesword", 36, true), // пусто
	S_YELLOW_AFFRO("yellowafro", 37, true), // Большая круглая желтая прическа с воткнутой в волосы расческой
	S_PINK_AFFRO("pinkafro", 38, true), // Большая круглая розовая прическа с воткнутой в волосы расческой
	S_BLACK_AFFRO("blackafro", 39, true), // Большая круглая черная прическа с воткнутой в волосы расческой
	S_STIGMA("stigma", 41, true), // Stigma of Shillen
	S_UNKNOWN10("sunk10", 42, true), // какой то рут
	FROZEN_PILLAR("frozenpillar", 43, true), // Frozen Pillar (Freya)
	S_UNKNOWN12("sunk12", 44, true), // пусто
	S_DESTINO_SET("vesper_red", 45, true), // Фейковый сет Веспера
	S_VESPER_SET("vesper_noble", 46, true), // фейковый сет Веспера Белый
	S_SOA_RESP("soa_respawn", 47, true), // Мобы на респе СОА появляются с таким                                     // абнормалом
	S_ARCANE_SHIELD("arcane_invul", 48, true), // Щит Арканы
	AIR_SHACKLE("airshackle", 49, true),//49     поднимает высоко в воздух, вокруг персонажа горит красная аура
	S_KNOCK_DOWN("sknockdown", 51),
	CELLED_CUBE("cubeV", 56, true),//56     персонаж попадает в тюрьму из дыма, скилл веньо
	SPECIAL_AURA("sigelAura1", 57, true),//57
	SPECIAL_AURA_1("sigelAura2", 58, true),//58
	SPECIAL_AURA_2("sigelAura3", 59, true),//59    для аур сигеля
	SPECIAL_AURA_3("sigelAura4", 60, true),//60
	S_UNKNOWN17("sunk17", 17, true), // пусто
	S_UNKNOWN18("sunk18", 18, true), // пусто
	S_UNKNOWN19("sunk19", 19, true), // пусто
	S_NAVIT("nevitSystem", 47, true), // пусто

	S_UNKNOWN21("sunk21", 21, true), // пусто
	S_UNKNOWN22("sunk22", 22, true), // пусто
	S_UNKNOWN23("sunk23", 23, true), // пусто
	S_UNKNOWN24("sunk24", 24, true), // пусто

	S_UNKNOWN25("sunk25", 25, true), // пусто
	S_UNKNOWN26("sunk26", 26, true), // пусто
	S_UNKNOWN27("sunk27", 27, true), // пусто
	S_UNKNOWN28("sunk28", 28, true), // пусто

	S_UNKNOWN29("sunk29", 29, true), // пусто
	S_UNKNOWN30("sunk30", 30, true), // пусто
	S_UNKNOWN31("sunk31", 31, true), // пусто
	S_UNKNOWN32("sunk32", 32, true), // пусто

	// event effects
	E_AFRO_1("afrobaguette1", 0, false, true),
	E_AFRO_2("afrobaguette2", 0, false, true),
	E_AFRO_3("afrobaguette3", 0, false, true),
	E_EVASWRATH("evaswrath", 0, false, true),
	E_HEADPHONE("headphone", 0, false, true),
	FLESH_STONE("hold2", 12),
	E_VESPER_1("vesper1", 44, false, true),
	E_VESPER_2("vesper2", 45, false, true),
	E_VESPER_3("vesper3", 46, false, true),
	TALISMAN_POWER1("talismanpower1", 72, true, false),
	TALISMAN_POWER2("talismanpower2", 73, true, false),
	TALISMAN_POWER3("talismanpower3", 74, true, false),
	TALISMAN_POWER4("talismanpower4", 75, true, false),
	TALISMAN_POWER5("talismanpower5", 76, true, false),
	CLOAK("Cloak",77),
	S_TRANSFORM("transform", 78, true),    
	S_TRANSFORM1("transform1", 79, true),
	S_TRANSFORM2("transform2", 80, true),
	S_TRANSFORM3("transform3", 81, true),
	S_TRANSFORM4("transform4", 82, true), //Awakeninger: 78-82 трансофрмируют внешний вид в соответствующий грейд шмоток от нг до Ы
	S_TRANSFORM5("transform5", 85, true), //Вроде как формальный костюм и шапка деда мороза
	BIKINI1("bikini_1", 83),   
    BIKINI2("bikini_2", 84),  
    NINJA("ninja", 86), 
    TAIWAN("taiwan", 87), 
    MILITARY("military", 88),
    METAL("metal", 89),
    MAID_UNIFORM("maid_uniform", 90),
    BIKINI3("bikini_3", 91),
    LIGHT_WEAPON("light", 94), 
    JAPAN_WEAPON("japan", 95);
	

	private final int _mask;
	private final String _name;
	private final boolean _special;
	private final boolean _event;

	private AbnormalEffect(String name, int mask)
	{
		_name = name;
		_mask = mask;
		_special = false;
		_event = false;
	}

	private AbnormalEffect(String name, int mask, boolean special)
	{
		_name = name;
		_mask = mask;
		_special = special;
		_event = false;
	}

	private AbnormalEffect(String name, int mask, boolean special, boolean event)
	{
		_name = name;
		_mask = mask;
		_special = special;
		_event = event;
	}

	public final int getMask()
	{
		return _mask;
	}

	public final String getName()
	{
		return _name;
	}

	public final boolean isSpecial()
	{
		return _special;
	}

	public final boolean isEvent()
	{
		return _event;
	}

	public static AbnormalEffect getByName(String name)
	{
		for(AbnormalEffect eff : AbnormalEffect.values())
		{
			if(eff.getName().equals(name))
			{
				return eff;
			}
		}

		throw new NoSuchElementException("AbnormalEffect not found for name: '" + name + "'.\n Please check " + AbnormalEffect.class.getCanonicalName());
	}
}