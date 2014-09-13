package l2next.gameserver;

import gnu.trove.map.hash.TIntIntHashMap;
import l2next.commons.collections.GArray;
import l2next.commons.configuration.ExProperties;
import l2next.commons.net.AdvancedIp;
import l2next.commons.net.nio.impl.SelectorConfig;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.loginservercon.ServerType;
import l2next.gameserver.model.actor.instances.player.Bonus;
import l2next.gameserver.model.base.Experience;
import l2next.gameserver.model.base.PlayerAccess;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Config
{
	private static final Logger _log = LoggerFactory.getLogger(Config.class);

	public static final int NCPUS = Runtime.getRuntime().availableProcessors();

	/**
	 * Configuration files
	 */
	public static final String OTHER_CONFIG_FILE = "config/other.properties";
	//public static final String DINAMIC_QUEST_FILE = "config/dynquest.properties";
	public static final String RESIDENCE_CONFIG_FILE = "config/residence.properties";
	public static final String SPOIL_CONFIG_FILE = "config/spoil.properties";
	public static final String ALT_SETTINGS_FILE = "config/altsettings.properties";
	public static final String FORMULAS_CONFIGURATION_FILE = "config/formulas.properties";
	public static final String PVP_CONFIG_FILE = "config/pvp.properties";
	public static final String TELNET_CONFIGURATION_FILE = "config/telnet.properties";
	public static final String CONFIGURATION_FILE = "config/server.properties";
	public static final String AI_CONFIG_FILE = "config/ai.properties";
	public static final String GEODATA_CONFIG_FILE = "config/geodata.properties";
	public static final String SERVICES_FILE = "config/services.properties";
	public static final String OLYMPIAD = "config/olympiad.properties";
	public static final String DEVELOP_FILE = "config/develop.properties";
	public static final String EXT_FILE = "config/ext.properties";
	public static final String OLYMPIAD_DATA_FILE = "config/olympiad.properties";
	public static final String ANUSEWORDS_CONFIG_FILE = "config/abusewords.txt";
	public static final String GM_PERSONAL_ACCESS_FILE = "config/GMAccess.xml";
	public static final String GM_ACCESS_FILES_DIR = "config/GMAccess.d/";
	public static final String TOP_CONFIG_FILE = "config/top.properties";
	public static final String PAYMENT_CONFIG_FILE = "config/payment.properties";
	public static final String L2WT_CONFIG_FILE = "config/l2wt.properties";
	public static final String COMMUNITY_CONFIGURATION_FILE = "config/CommunityBoard.ini";

	public static final String EVENT_APRIL_FOOLS_CONFIG_FILE = "config/events/AprilFools.properties";
	public static final String EVENT_CAPTURE_THE_FLAG_CONFIG_FILE = "config/events/CaptureTheFlag.properties";
	public static final String EVENT_CHANGE_OF_HEART_CONFIG_FILE = "config/events/ChangeOfHeart.properties";
	public static final String EVENT_COFFER_OF_SHADOWS_CONFIG_FILE = "config/events/CofferOfShadows.properties";
	public static final String EVENT_GLITTERING_MEDAL_CONFIG_FILE = "config/events/GlitteringMedal.properties";
	public static final String EVENT_L2_DAY_CONFIG_FILE = "config/events/L2Day.properties";
	public static final String EVENT_LAST_HERO_CONFIG_FILE = "config/events/LastHero.properties";
	public static final String EVENT_MARCH_8_CONFIG_FILE = "config/events/March8.properties";
	public static final String EVENT_MASTER_OF_ENCHANING_CONFIG_FILE = "config/events/MasterOfEnchaning.properties";
	public static final String EVENT_OTHER_EVENTS_CONFIG_FILE = "config/events/OtherEvents.properties";
	public static final String EVENT_SAVING_SNOWMAN_CONFIG_FILE = "config/events/SavingSnowman.properties";
	public static final String EVENT_THE_FALL_HARVEST_CONFIG_FILE = "config/events/TheFallHarvest.properties";
	public static final String EVENT_TRICK_OF_TRANSMUTATION_CONFIG_FILE = "config/events/TrickOfTransmutation.properties";

	public static final String ADVANCED_IP_CONFIG_FILE = "config/AdvancedIp.properties";

	/** Community Board */
	public static boolean USE_BBS_BUFER_IS_COMBAT;
	public static boolean USE_BBS_BUFER_IS_EVENTS;
	public static boolean USE_BBS_TELEPORT_IS_COMBAT;
	public static boolean USE_BBS_TELEPORT_IS_EVENTS;
	public static boolean USE_BBS_PROF_IS_COMBAT;
	public static boolean USE_BBS_PROF_IS_EVENTS;
	public static boolean SAVE_BBS_TELEPORT_IS_EPIC;
	public static boolean SAVE_BBS_TELEPORT_IS_BZ;
	public static boolean BUFFER_SUMMON_ENABLE;
	public static boolean COMMUNITYBOARD_ENABLED;
	public static boolean ALLOW_COMMUNITYBOARD_IN_COMBAT;
	public static boolean COMMUNITYBOARD_BUFFER_ENABLED;
	public static boolean COMMUNITYBOARD_SELL_ENABLED;
	public static boolean COMMUNITYBOARD_SHOP_ENABLED;
	public static boolean COMMUNITYBOARD_BUFFER_PET_ENABLED;
	public static boolean COMMUNITYBOARD_BUFFER_SAVE_ENABLED;
	public static boolean COMMUNITYBOARD_ABNORMAL_ENABLED;
	public static boolean COMMUNITYBOARD_INSTANCE_ENABLED;
	public static boolean COMMUNITYBOARD_EVENTS_ENABLED;
	public static int COMMUNITYBOARD_BUFF_TIME;
	public static int COMMUNITYBOARD_BUFF_PICE;
	public static int COMMUNITYBOARD_BUFF_SAVE_PICE;
	public static List<Integer> COMMUNITYBOARD_BUFF_ALLOW = new ArrayList<Integer>();
	public static List<Integer> COMMUNITI_LIST_MAGE_SUPPORT = new ArrayList<Integer>();
	public static List<Integer> COMMUNITI_LIST_FIGHTER_SUPPORT = new ArrayList<Integer>();
	public static List<String> COMMUNITYBOARD_MULTISELL_ALLOW = new ArrayList<String>();
	public static String BBS_DEFAULT;
    public static boolean BBSHOME_PAGE_WESTEROS;
	public static boolean COMMUNITYBOARD_TELEPORT_ENABLED;
	public static int COMMUNITYBOARD_TELE_PICE;
	public static int COMMUNITYBOARD_SAVE_TELE_PICE;
	public static String communityFolder;

	public static boolean ENCHANT_ENABLED;
	public static int ENCHANTER_ITEM_ID;
	public static int MAX_ENCHANT;
	public static int[] ENCHANT_LEVELS;
	public static int[] ENCHANT_PRICE_WPN;
	public static int[] ENCHANT_PRICE_ARM;
	public static int[] ENCHANT_ATTRIBUTE_LEVELS;
	public static int[] ENCHANT_ATTRIBUTE_LEVELS_ARM;
	public static int[] ATTRIBUTE_PRICE_WPN;
	public static int[] ATTRIBUTE_PRICE_ARM;
	public static boolean ENCHANT_ATT_PVP;
	public static int BBS_STAT_UPDATE_TIME;

	public static int HTM_CACHE_MODE;
	/**
	 * GameServer ports
	 */
	public static int[] PORTS_GAME;
	public static String GAMESERVER_HOSTNAME;

	public static String DATABASE_DRIVER;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIMEOUT;
	public static int DATABASE_IDLE_TEST_PERIOD;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;

	public static String DATABASE_URL_LOGIN;
	public static String DATABASE_LOGIN_LOGIN;
	public static String DATABASE_PASSWORD_LOGIN;

	// Database additional options
	public static boolean AUTOSAVE;

	public static long USER_INFO_INTERVAL;
	public static boolean BROADCAST_STATS_INTERVAL;
	public static long BROADCAST_CHAR_INFO_INTERVAL;

	public static int EFFECT_TASK_MANAGER_COUNT;
	public static int AI_TASK_MANAGER_COUNT;

	public static int MAXIMUM_ONLINE_USERS;

	public static boolean LOAD_CUSTOM_SPAWN;
	public static boolean SAVE_GM_CUSTOM_SPAWN;
	public static boolean DONTLOADSPAWN;
	public static boolean DONTLOADQUEST;
	public static int MAX_REFLECTIONS_COUNT;

	public static int SHIFT_BY;
	public static int SHIFT_BY_Z;
	public static int MAP_MIN_Z;
	public static int MAP_MAX_Z;

	/**
	 * ChatBan
	 */
	public static int CHAT_MESSAGE_MAX_LEN;
	public static boolean ABUSEWORD_BANCHAT;
	public static int[] BAN_CHANNEL_LIST = new int[18];
	public static boolean ABUSEWORD_REPLACE;
	public static String ABUSEWORD_REPLACE_STRING;
	public static int ABUSEWORD_BANTIME;
	public static Pattern[] ABUSEWORD_LIST = {};
	public static boolean BANCHAT_ANNOUNCE;
	public static boolean BANCHAT_ANNOUNCE_FOR_ALL_WORLD;
	public static boolean BANCHAT_ANNOUNCE_NICK;

	public static int[] CHATFILTER_CHANNELS = new int[18];
	public static int CHATFILTER_MIN_LEVEL = 0;
	public static int CHATFILTER_WORK_TYPE = 1;

	public static boolean SAVING_SPS;
	public static boolean MANAHEAL_SPS_BONUS;

	public static int ALT_ADD_RECIPES;
	public static int ALT_MAX_ALLY_SIZE;

	public static int ALT_PARTY_DISTRIBUTION_RANGE;
	public static double[] ALT_PARTY_BONUS;
	public static double ALT_ABSORB_DAMAGE_MODIFIER;
	public static boolean ALT_ALL_PHYS_SKILLS_OVERHIT;

	public static double ALT_POLE_DAMAGE_MODIFIER;

	public static boolean ALT_REMOVE_SKILLS_ON_DELEVEL;
	public static boolean ALT_USE_BOW_REUSE_MODIFIER;

	public static boolean ALT_VITALITY_ENABLED;
	public static double ALT_VITALITY_RATE_EXP;
	public static double ALT_VITALITY_RATE_SP;
	public static double ALT_VITALITY_PREMIUM_RATE_EXP;
	public static double ALT_VITALITY_PREMIUM_RATE_SP;
	public static double ALT_VITALITY_RATE_DROP;
	public static double ALT_VITALITY_PREMIUM_RATE_DROP;
	public static double ALT_VITALITY_CONSUME_RATE;
	public static final int MAX_VITALITY = 140000;
	public static int MAX_VITALITY_ITEMS = 5;

	public static Calendar CASTLE_VALIDATION_DATE;
	public static int[] CASTLE_SELECT_HOURS;

	public static boolean ALT_PCBANG_POINTS_ENABLED;
	public static double ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE;
	public static int ALT_PCBANG_POINTS_BONUS;
	public static int ALT_PCBANG_POINTS_DELAY;
	public static int ALT_PCBANG_POINTS_MIN_LVL;

	public static boolean ALT_DEBUG_ENABLED;
	public static boolean ALT_DEBUG_PVP_ENABLED;
	public static boolean ALT_DEBUG_PVP_DUEL_ONLY;
	public static boolean ALT_DEBUG_PVE_ENABLED;

	public static double CRAFT_MASTERWORK_CHANCE;
	public static double CRAFT_DOUBLECRAFT_CHANCE;

	/**
	 * Thread pools size
	 */
	public static int SCHEDULED_THREAD_POOL_SIZE;
	public static int EXECUTOR_THREAD_POOL_SIZE;

	/**
	 * Network settings
	 */
	public static SelectorConfig SELECTOR_CONFIG = new SelectorConfig();

	public static boolean AUTO_LOOT;
	public static boolean AUTO_LOOT_HERBS;
	public static boolean AUTO_LOOT_INDIVIDUAL;
	public static boolean AUTO_LOOT_FROM_RAIDS;

	/**
	 * Auto-loot for/from players with karma also?
	 */
	public static boolean AUTO_LOOT_PK;

	/**
	 * Character name template
	 */
	public static String CNAME_TEMPLATE;

	public static int CNAME_MAXLEN = 32;

	/**
	 * Clan name template
	 */
	public static String CLAN_NAME_TEMPLATE;

	/**
	 * Clan title template
	 */
	public static String CLAN_TITLE_TEMPLATE;

	/**
	 * Ally name template
	 */
	public static String ALLY_NAME_TEMPLATE;

	/**
	 * Global chat state
	 */
	public static boolean GLOBAL_SHOUT;
	public static boolean GLOBAL_TRADE_CHAT;
	public static int CHAT_RANGE;
	public static int SHOUT_OFFSET;
	public static boolean PREMIUM_HEROCHAT;

	public static GArray<String> TRADE_WORDS;
	public static boolean TRADE_CHATS_REPLACE;

	/**
	 * For test servers - evrybody has admin rights
	 */
	public static boolean EVERYBODY_HAS_ADMIN_RIGHTS;

	public static double ALT_RAID_RESPAWN_MULTIPLIER;

	public static boolean ALT_ALLOW_AUGMENT_ALL;
	public static boolean ALT_ALLOW_DROP_AUGMENTED;

	public static boolean ALT_GAME_UNREGISTER_RECIPE;

	/**
	 * Delay for announce SS period (in minutes)
	 */
	public static int SS_ANNOUNCE_PERIOD;

	/**
	 * Petition manager
	 */
	public static boolean PETITIONING_ALLOWED;
	public static int MAX_PETITIONS_PER_PLAYER;
	public static int MAX_PETITIONS_PENDING;

	/**
	 * Show mob stats/droplist to players?
	 */
	public static boolean ALT_GAME_SHOW_DROPLIST;
	public static boolean ALT_FULL_NPC_STATS_PAGE;
	public static boolean ALLOW_NPC_SHIFTCLICK;

	public static boolean ALT_ALLOW_SELL_COMMON;
	public static boolean ALT_ALLOW_SHADOW_WEAPONS;
	public static int[] ALT_DISABLED_MULTISELL;
	public static int[] ALT_SHOP_PRICE_LIMITS;
	public static int[] ALT_SHOP_UNALLOWED_ITEMS;

	public static int[] ALT_ALLOWED_PET_POTIONS;

	public static double SKILLS_CHANCE_MOD;
	public static double SKILLS_CHANCE_MIN;
	public static double SKILLS_CHANCE_POW;
	public static double SKILLS_CHANCE_CAP;
	public static boolean ALT_SAVE_UNSAVEABLE;
	public static int ALT_SAVE_EFFECTS_REMAINING_TIME;
	public static boolean ALT_SHOW_REUSE_MSG;
	public static boolean ALT_DELETE_SA_BUFFS;
	public static int SKILLS_CAST_TIME_MIN;

	/**
	 * Титул при создании чара
	 */
	public static boolean CHAR_TITLE;
	public static String ADD_CHAR_TITLE;

	/**
	 * Таймаут на использование social action
	 */
	public static boolean ALT_SOCIAL_ACTION_REUSE;

	/**
	 * Отключение книг для изучения скилов
	 */
	public static boolean ALT_DISABLE_SPELLBOOKS;

	/**
	 * Alternative gameing - loss of XP on death
	 */
	public static boolean ALT_GAME_DELEVEL;

	/**
	 * Разрешать ли на арене бои за опыт
	 */
	public static boolean ALT_ARENA_EXP;

	public static boolean ALT_GAME_SUBCLASS_WITHOUT_QUESTS;
	public static boolean ALT_ALLOW_SUBCLASS_WITHOUT_BAIUM;
	public static int ALT_GAME_LEVEL_TO_GET_SUBCLASS;
	public static int ALT_MAX_LEVEL;
	public static int ALT_MAX_SUB_LEVEL;
	public static int ALT_GAME_SUB_ADD;
	public static boolean ALT_NO_LASTHIT;
	public static boolean ALT_KAMALOKA_NIGHTMARES_PREMIUM_ONLY;
	public static boolean ALT_KAMALOKA_NIGHTMARE_REENTER;
	public static boolean ALT_KAMALOKA_ABYSS_REENTER;
	public static boolean ALT_KAMALOKA_LAB_REENTER;
	public static boolean ALT_PET_HEAL_BATTLE_ONLY;

	public static boolean ALT_SIMPLE_SIGNS;
	public static boolean ALT_TELE_TO_CATACOMBS;
	public static boolean ALT_BS_CRYSTALLIZE;
	public static int ALT_MAMMON_EXCHANGE;
	public static int ALT_MAMMON_UPGRADE;
	public static boolean ALT_ALLOW_TATTOO;

	public static int ALT_BUFF_LIMIT;

	public static int MULTISELL_SIZE;

	public static boolean SERVICES_CHANGE_NICK_ENABLED;
	public static int SERVICES_CHANGE_NICK_PRICE;
	public static int SERVICES_CHANGE_NICK_ITEM;

	public static boolean SERVICES_CHANGE_CLAN_NAME_ENABLED;
	public static int SERVICES_CHANGE_CLAN_NAME_PRICE;
	public static int SERVICES_CHANGE_CLAN_NAME_ITEM;

	public static boolean SERVICES_CHANGE_PET_NAME_ENABLED;
	public static int SERVICES_CHANGE_PET_NAME_PRICE;
	public static int SERVICES_CHANGE_PET_NAME_ITEM;

	public static boolean SERVICES_EXCHANGE_BABY_PET_ENABLED;
	public static int SERVICES_EXCHANGE_BABY_PET_PRICE;
	public static int SERVICES_EXCHANGE_BABY_PET_ITEM;

	public static boolean SERVICES_CHANGE_SEX_ENABLED;
	public static int SERVICES_CHANGE_SEX_PRICE;
	public static int SERVICES_CHANGE_SEX_ITEM;

	public static boolean SERVICES_CHANGE_NICK_COLOR_ENABLED;
	public static int SERVICES_CHANGE_NICK_COLOR_PRICE;
	public static int SERVICES_CHANGE_NICK_COLOR_ITEM;
	public static String[] SERVICES_CHANGE_NICK_COLOR_LIST;
	
  public static boolean SERVICES_CHANGE_TITLE_COLOR_ENABLED;
  public static int SERVICES_CHANGE_TITLE_COLOR_PRICE;
  public static int SERVICES_CHANGE_TITLE_COLOR_ITEM;
  public static String[] SERVICES_CHANGE_TITLE_COLOR_LIST;

	public static boolean SERVICES_BASH_ENABLED;
	public static boolean SERVICES_BASH_SKIP_DOWNLOAD;
	public static int SERVICES_BASH_RELOAD_TIME;

	public static int SERVICES_RATE_TYPE;
	public static int[] SERVICES_RATE_BONUS_PRICE;
	public static int[] SERVICES_RATE_BONUS_ITEM;
	public static double[] SERVICES_RATE_BONUS_VALUE;
	public static int[] SERVICES_RATE_BONUS_DAYS;

	public static boolean SERVICES_NOBLESS_SELL_ENABLED;
	public static int SERVICES_NOBLESS_SELL_PRICE;
	public static int SERVICES_NOBLESS_SELL_ITEM;

	public static boolean SERVICES_EXPAND_INVENTORY_ENABLED;
	public static int SERVICES_EXPAND_INVENTORY_PRICE;
	public static int SERVICES_EXPAND_INVENTORY_ITEM;
	public static int SERVICES_EXPAND_INVENTORY_MAX;

	public static boolean SERVICES_EXPAND_WAREHOUSE_ENABLED;
	public static int SERVICES_EXPAND_WAREHOUSE_PRICE;
	public static int SERVICES_EXPAND_WAREHOUSE_ITEM;

	public static boolean SERVICES_EXPAND_CWH_ENABLED;
	public static int SERVICES_EXPAND_CWH_PRICE;
	public static int SERVICES_EXPAND_CWH_ITEM;

	public static String SERVICES_SELLPETS;

	public static boolean SERVICES_OFFLINE_TRADE_ALLOW;
	public static boolean SERVICES_OFFLINE_TRADE_ALLOW_OFFSHORE;
	public static int SERVICES_OFFLINE_TRADE_MIN_LEVEL;
	public static int SERVICES_OFFLINE_TRADE_NAME_COLOR;
	public static int SERVICES_OFFLINE_TRADE_PRICE;
	public static int SERVICES_OFFLINE_TRADE_PRICE_ITEM;
	public static long SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK;
	public static boolean SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART;
	public static boolean SERVICES_GIRAN_HARBOR_ENABLED;
	public static boolean SERVICES_PARNASSUS_ENABLED;
	public static boolean SERVICES_PARNASSUS_NOTAX;
	public static long SERVICES_PARNASSUS_PRICE;

	public static boolean SERVICES_ALLOW_LOTTERY;
	public static int SERVICES_LOTTERY_PRIZE;
	public static int SERVICES_ALT_LOTTERY_PRICE;
	public static int SERVICES_LOTTERY_TICKET_PRICE;
	public static double SERVICES_LOTTERY_5_NUMBER_RATE;
	public static double SERVICES_LOTTERY_4_NUMBER_RATE;
	public static double SERVICES_LOTTERY_3_NUMBER_RATE;
	public static int SERVICES_LOTTERY_2_AND_1_NUMBER_PRIZE;

	public static boolean SERVICES_ALLOW_ROULETTE;
	public static long SERVICES_ROULETTE_MIN_BET;
	public static long SERVICES_ROULETTE_MAX_BET;

	public static boolean ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE;
	public static boolean ALT_ALLOW_CLAN_COMMAND_ONLY_FOR_CLAN_LEADER;
	public static boolean ALT_GAME_REQUIRE_CLAN_CASTLE;
	public static boolean ALT_GAME_REQUIRE_CASTLE_DAWN;
	public static boolean ALT_GAME_ALLOW_ADENA_DAWN;

	/**
	 * Olympiad Compitition Starting time
	 */
	public static int ALT_OLY_START_TIME;
	/**
	 * Olympiad Compition Min
	 */
	public static int ALT_OLY_MIN;
	/**
	 * Olympaid Comptetition Period
	 */
	public static long ALT_OLY_CPERIOD;
	/**
	 * Olympaid Weekly Period
	 */
	public static long ALT_OLY_WPERIOD;
	/**
	 * Olympaid Validation Period
	 */
	public static long ALT_OLY_VPERIOD;

	public static boolean ENABLE_OLYMPIAD;
	public static boolean ENABLE_OLYMPIAD_SPECTATING;

	public static int CLASS_GAME_MIN;
	public static int NONCLASS_GAME_MIN;

	public static int GAME_MAX_LIMIT;
	public static int GAME_CLASSES_COUNT_LIMIT;
	public static int GAME_NOCLASSES_COUNT_LIMIT;

	public static int ALT_OLY_REG_DISPLAY;
	public static int ALT_OLY_BATTLE_REWARD_ITEM;
	public static int ALT_OLY_CLASSED_RITEM_C;
	public static int ALT_OLY_NONCLASSED_RITEM_C;
	public static int TEAM_GAME_MIN;
	public static boolean ALT_OLY_DAYS;
	public static int ALT_OLY_TEAM_RITEM_C;
	public static int ALT_OLY_COMP_RITEM;
	public static int ALT_OLY_GP_PER_POINT;
	public static int ALT_OLY_HERO_POINTS;
	public static int ALT_OLY_RANK1_POINTS;
	public static int ALT_OLY_RANK2_POINTS;
	public static int ALT_OLY_RANK3_POINTS;
	public static int ALT_OLY_RANK4_POINTS;
	public static int ALT_OLY_RANK5_POINTS;
	public static int OLYMPIAD_STADIAS_COUNT;
	public static int OLYMPIAD_BATTLES_FOR_REWARD;
	public static int OLYMPIAD_POINTS_DEFAULT;
	public static int OLYMPIAD_POINTS_WEEKLY;
	public static boolean OLYMPIAD_OLDSTYLE_STAT;

	public static long NONOWNER_ITEM_PICKUP_DELAY;

	/**
	 * Logging Chat Window
	 */
	public static boolean LOG_CHAT;

	public static Map<Integer, PlayerAccess> gmlist = new HashMap<Integer, PlayerAccess>();

	/**
	 * Rate control
	 */
	public static double RATE_XP;
	public static double RATE_SP;
	public static double RATE_QUESTS_REWARD;
	public static double RATE_QUESTS_DROP;
	public static double RATE_CLAN_REP_SCORE;
	public static int RATE_CLAN_REP_SCORE_MAX_AFFECTED;
	public static double RATE_DROP_ADENA;
	public static double RATE_DROP_ITEMS;
	public static double RATE_DROP_COMMON_ITEMS;
	public static double RATE_DROP_RAIDBOSS;
	public static double RATE_DROP_SPOIL;
	public static int[] NO_RATE_ITEMS;
	public static boolean NO_RATE_EQUIPMENT;
	public static boolean NO_RATE_KEY_MATERIAL;
	public static boolean NO_RATE_RECIPES;
	public static double RATE_DROP_SIEGE_GUARD;
	public static double RATE_MANOR;
	public static double RATE_FISH_DROP_COUNT;
	public static boolean RATE_PARTY_MIN;
	public static double RATE_HELLBOUND_CONFIDENCE;

	public static int RATE_MOB_SPAWN;
	public static int RATE_MOB_SPAWN_MIN_LEVEL;
	public static int RATE_MOB_SPAWN_MAX_LEVEL;

	/**
	 * Player Drop Rate control
	 */
	public static boolean KARMA_DROP_GM;
	public static boolean KARMA_NEEDED_TO_DROP;

	public static int KARMA_DROP_ITEM_LIMIT;

	public static int KARMA_RANDOM_DROP_LOCATION_LIMIT;

	public static double KARMA_DROPCHANCE_BASE;
	public static double KARMA_DROPCHANCE_MOD;
	public static double NORMAL_DROPCHANCE_BASE;
	public static int DROPCHANCE_EQUIPMENT;
	public static int DROPCHANCE_EQUIPPED_WEAPON;
	public static int DROPCHANCE_ITEM;

	public static int AUTODESTROY_ITEM_AFTER;

	public static int DELETE_DAYS;

	public static int PURGE_BYPASS_TASK_FREQUENCY;

	/**
	 * Datapack root directory
	 */
	public static File DATAPACK_ROOT;

	public static double CLANHALL_BUFFTIME_MODIFIER;
	public static double SONGDANCETIME_MODIFIER;

	public static double MAXLOAD_MODIFIER;
	public static double GATEKEEPER_MODIFIER;
	public static boolean ALT_IMPROVED_PETS_LIMITED_USE;
	public static int GATEKEEPER_FREE;
	public static int CRUMA_GATEKEEPER_LVL;

	public static double ALT_CHAMPION_CHANCE1;
	public static double ALT_CHAMPION_CHANCE2;
	public static boolean ALT_CHAMPION_CAN_BE_AGGRO;
	public static boolean ALT_CHAMPION_CAN_BE_SOCIAL;
	public static int ALT_CHAMPION_TOP_LEVEL;

	public static boolean ALLOW_DISCARDITEM;
	public static boolean ALLOW_MAIL;
	public static boolean ALLOW_WAREHOUSE;
	public static boolean ALLOW_WATER;
	public static boolean ALLOW_CURSED_WEAPONS;
	public static boolean DROP_CURSED_WEAPONS_ON_KICK;
	public static boolean ALLOW_NOBLE_TP_TO_ALL;

	/**
	 * Pets
	 */
	public static int SWIMING_SPEED;

	/**
	 * protocol revision
	 */
	public static int MIN_PROTOCOL_REVISION;
	public static int MAX_PROTOCOL_REVISION;

	/**
	 * random animation interval
	 */
	public static int MIN_NPC_ANIMATION;
	public static int MAX_NPC_ANIMATION;

	public static String DEFAULT_LANG;

	/**
	 * Время запланированного на определенное время суток рестарта
	 */
	public static String RESTART_AT_TIME;

	public static int GAME_SERVER_LOGIN_PORT;
	public static boolean GAME_SERVER_LOGIN_CRYPT;
	public static String GAME_SERVER_LOGIN_HOST;
	public static String INTERNAL_HOSTNAME;
	public static String EXTERNAL_HOSTNAME;

	public static boolean SERVER_SIDE_NPC_NAME;
	public static boolean SERVER_SIDE_NPC_TITLE;
	public static boolean SERVER_SIDE_NPC_TITLE_ETC;

	public static String CLASS_MASTERS_PRICE;
	public static String CLASS_MASTERS_PRICE_ITEM;
	public static int[] CLASS_MASTERS_PRICE_ITEM_LIST = new int[5];
	public static List<Integer> ALLOW_CLASS_MASTERS_LIST = new ArrayList<Integer>();
	public static int[] CLASS_MASTERS_PRICE_LIST = new int[5];
	public static boolean ALLOW_EVENT_GATEKEEPER;
	public static boolean ENABLE_ANGEL_CAT;

	public static boolean ITEM_BROKER_ITEM_SEARCH;

	/**
	 * Inventory slots limits
	 */
	public static int INVENTORY_MAXIMUM_NO_DWARF;
	public static int INVENTORY_MAXIMUM_DWARF;
	public static int INVENTORY_MAXIMUM_GM;
	public static int QUEST_INVENTORY_MAXIMUM;

	/**
	 * Warehouse slots limits
	 */
	public static int WAREHOUSE_SLOTS_NO_DWARF;
	public static int WAREHOUSE_SLOTS_DWARF;
	public static int WAREHOUSE_SLOTS_CLAN;

	public static int FREIGHT_SLOTS;

	/**
	 * Spoil Rates
	 */
	public static double BASE_SPOIL_RATE;
	public static double MINIMUM_SPOIL_RATE;
	public static boolean ALT_SPOIL_FORMULA;

	/**
	 * Manor Config
	 */
	public static double MANOR_SOWING_BASIC_SUCCESS;
	public static double MANOR_SOWING_ALT_BASIC_SUCCESS;
	public static double MANOR_HARVESTING_BASIC_SUCCESS;
	public static int MANOR_DIFF_PLAYER_TARGET;
	public static double MANOR_DIFF_PLAYER_TARGET_PENALTY;
	public static int MANOR_DIFF_SEED_TARGET;
	public static double MANOR_DIFF_SEED_TARGET_PENALTY;

	/**
	 * Karma System Variables
	 */
	public static int KARMA_MIN_KARMA;
	public static int KARMA_SP_DIVIDER;
	public static int KARMA_LOST_BASE;

	public static int MIN_PK_TO_ITEMS_DROP;
	public static boolean DROP_ITEMS_ON_DIE;
	public static boolean DROP_ITEMS_AUGMENTED;

	public static List<Integer> KARMA_LIST_NONDROPPABLE_ITEMS = new ArrayList<Integer>();

	public static int PVP_TIME;

	/**
	 * Karma Punishment
	 */
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_SHOP;

	public static int ARMOR_OVERENCHANT_HPBONUS_LIMIT;
	public static boolean SHOW_ENCHANT_EFFECT_RESULT;

	public static boolean REGEN_SIT_WAIT;

	public static double RATE_RAID_REGEN;
	public static double RATE_RAID_DEFENSE;
	public static double RATE_RAID_ATTACK;
	public static double RATE_EPIC_DEFENSE;
	public static double RATE_EPIC_ATTACK;
	public static int RAID_MAX_LEVEL_DIFF;
	public static boolean PARALIZE_ON_RAID_DIFF;

	public static double ALT_PK_DEATH_RATE;
	public static int STARTING_ADENA;

	public static boolean AWAKING_FREE;

	/**
	 * Deep Blue Mobs' Drop Rules Enabled
	 */
	public static boolean DEEPBLUE_DROP_RULES;
	public static int DEEPBLUE_DROP_MAXDIFF;
	public static int DEEPBLUE_DROP_RAID_MAXDIFF;
	public static boolean UNSTUCK_SKILL;

	/**
	 * telnet enabled
	 */
	public static boolean IS_TELNET_ENABLED;
	public static String TELNET_DEFAULT_ENCODING;
	public static String TELNET_PASSWORD;
	public static String TELNET_HOSTNAME;
	public static int TELNET_PORT;

	/**
	 * Percent CP is restore on respawn
	 */
	public static double RESPAWN_RESTORE_CP;
	/**
	 * Percent HP is restore on respawn
	 */
	public static double RESPAWN_RESTORE_HP;
	/**
	 * Percent MP is restore on respawn
	 */
	public static double RESPAWN_RESTORE_MP;

	/**
	 * Maximum number of available slots for pvt stores (sell/buy) - Dwarves
	 */
	public static int MAX_PVTSTORE_SLOTS_DWARF;
	/**
	 * Maximum number of available slots for pvt stores (sell/buy) - Others
	 */
	public static int MAX_PVTSTORE_SLOTS_OTHER;
	public static int MAX_PVTCRAFT_SLOTS;

	public static boolean SENDSTATUS_TRADE_JUST_OFFLINE;
	public static double SENDSTATUS_TRADE_MOD;

	public static boolean ALLOW_CH_DOOR_OPEN_ON_CLICK;
	public static boolean ALT_CH_ALL_BUFFS;
	public static boolean ALT_CH_ALLOW_1H_BUFFS;
	public static boolean ALT_CH_SIMPLE_DIALOG;

	public static int CH_BID_GRADE1_MINCLANLEVEL;
	public static int CH_BID_GRADE1_MINCLANMEMBERS;
	public static int CH_BID_GRADE1_MINCLANMEMBERSLEVEL;
	public static int CH_BID_GRADE2_MINCLANLEVEL;
	public static int CH_BID_GRADE2_MINCLANMEMBERS;
	public static int CH_BID_GRADE2_MINCLANMEMBERSLEVEL;
	public static int CH_BID_GRADE3_MINCLANLEVEL;
	public static int CH_BID_GRADE3_MINCLANMEMBERS;
	public static int CH_BID_GRADE3_MINCLANMEMBERSLEVEL;
	public static double RESIDENCE_LEASE_FUNC_MULTIPLIER;
	public static double RESIDENCE_LEASE_MULTIPLIER;

	public static boolean ACCEPT_ALTERNATE_ID;
	public static int REQUEST_ID;

	public static boolean ANNOUNCE_MAMMON_SPAWN;

	public static int GM_NAME_COLOUR;
	public static boolean GM_HERO_AURA;
	public static int NORMAL_NAME_COLOUR;
	public static int CLANLEADER_NAME_COLOUR;

	/**
	 * AI
	 */
	public static long AI_TASK_ATTACK_DELAY;
	public static long AI_TASK_ACTIVE_DELAY;
	public static boolean BLOCK_ACTIVE_TASKS;
	public static boolean ALWAYS_TELEPORT_HOME;
	public static boolean RND_WALK;
	public static int RND_WALK_RATE;
	public static int RND_ANIMATION_RATE;

	public static int AGGRO_CHECK_INTERVAL;
	public static long NONAGGRO_TIME_ONTELEPORT;

	/**
	 * Maximum range mobs can randomly go from spawn point
	 */
	public static int MAX_DRIFT_RANGE;

	/**
	 * Maximum range mobs can pursue agressor from spawn point
	 */
	public static int MAX_PURSUE_RANGE;
	public static int MAX_PURSUE_UNDERGROUND_RANGE;
	public static int MAX_PURSUE_RANGE_RAID;

	public static boolean ALT_DEATH_PENALTY;
	public static boolean ALLOW_DEATH_PENALTY_C5;
	public static int ALT_DEATH_PENALTY_C5_CHANCE;
	public static boolean ALT_DEATH_PENALTY_C5_CHAOTIC_RECOVERY;
	public static int ALT_DEATH_PENALTY_C5_EXPERIENCE_PENALTY;
	public static int ALT_DEATH_PENALTY_C5_KARMA_PENALTY;

	public static boolean HIDE_GM_STATUS;
	public static boolean SHOW_GM_LOGIN;
	public static boolean SAVE_GM_EFFECTS; // Silence, gmspeed, etc...

	public static boolean AUTO_LEARN_SKILLS;
	public static boolean AUTO_LEARN_FORGOTTEN_SKILLS;

	public static boolean ENABLE_MODIFY_SKILL_DURATION;
	public static TIntIntHashMap SKILL_DURATION_LIST;

	public static int MOVE_PACKET_DELAY;
	public static int ATTACK_PACKET_DELAY;

	public static boolean DAMAGE_FROM_FALLING;

	/**
	 * Wedding Options
	 */
	public static boolean ALLOW_WEDDING;
	public static int WEDDING_PRICE;
	public static boolean WEDDING_PUNISH_INFIDELITY;
	public static boolean WEDDING_TELEPORT;
	public static int WEDDING_TELEPORT_PRICE;
	public static int WEDDING_TELEPORT_INTERVAL;
	public static boolean WEDDING_SAMESEX;
	public static boolean WEDDING_FORMALWEAR;
	public static int WEDDING_DIVORCE_COSTS;

	/**
	 * Augmentations *
	 */
	public static int AUGMENTATION_NG_SKILL_CHANCE; // Chance to get a skill
	// while using a NoGrade
	// Life Stone
	public static int AUGMENTATION_NG_GLOW_CHANCE; // Chance to get a Glow
	// effect while using a
	// NoGrade Life Stone(only if
	// you get a skill)
	public static int AUGMENTATION_MID_SKILL_CHANCE; // Chance to get a skill
	// while using a MidGrade
	// Life Stone
	public static int AUGMENTATION_MID_GLOW_CHANCE; // Chance to get a Glow
	// effect while using a
	// MidGrade Life Stone(only
	// if you get a skill)
	public static int AUGMENTATION_HIGH_SKILL_CHANCE; // Chance to get a skill
	// while using a HighGrade
	// Life Stone
	public static int AUGMENTATION_HIGH_GLOW_CHANCE; // Chance to get a Glow
	// effect while using a
	// HighGrade Life Stone
	public static int AUGMENTATION_TOP_SKILL_CHANCE; // Chance to get a skill
	// while using a TopGrade
	// Life Stone
	public static int AUGMENTATION_TOP_GLOW_CHANCE; // Chance to get a Glow
	// effect while using a
	// TopGrade Life Stone
	public static int AUGMENTATION_BASESTAT_CHANCE; // Chance to get a
	// BaseStatModifier in the
	// augmentation process
	public static int AUGMENTATION_ACC_SKILL_CHANCE;

	public static int FOLLOW_RANGE;

	public static boolean ALT_ITEM_AUCTION_ENABLED;
	public static boolean ALT_ITEM_AUCTION_CAN_REBID;
	public static boolean ALT_ITEM_AUCTION_START_ANNOUNCE;
	public static int ALT_ITEM_AUCTION_BID_ITEM_ID;
	public static long ALT_ITEM_AUCTION_MAX_BID;
	public static int ALT_ITEM_AUCTION_MAX_CANCEL_TIME_IN_MILLIS;

	public static boolean ALT_FISH_CHAMPIONSHIP_ENABLED;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_ITEM;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_1;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_2;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_3;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_4;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_5;

	public static boolean ALT_ENABLE_BLOCK_CHECKER_EVENT;
	public static int ALT_MIN_BLOCK_CHECKER_TEAM_MEMBERS;
	public static double ALT_RATE_COINS_REWARD_BLOCK_CHECKER;
	public static boolean ALT_HBCE_FAIR_PLAY;
	public static int ALT_PET_INVENTORY_LIMIT;
	public static boolean ALT_IF_VOICE_COMMAND_ALLOWED;

	/**
	 * limits of stats *
	 */
	public static int LIM_PATK;
	public static int LIM_MATK;
	public static int LIM_PDEF;
	public static int LIM_MDEF;
	public static int LIM_MATK_SPD;
	public static int LIM_PATK_SPD;
	public static int LIM_CRIT_DAM;
	public static int LIM_CRIT;
	public static int LIM_MCRIT;
	public static int LIM_ACCURACY;
	public static int LIM_EVASION;
	public static int LIM_MOVE;
	public static int LIM_FAME;

	public static double ALT_NPC_PATK_MODIFIER;
	public static double ALT_NPC_MATK_MODIFIER;
	public static double ALT_NPC_MAXHP_MODIFIER;
	public static double ALT_NPC_MAXMP_MODIFIER;

	/**
	 * Enchant Config *
	 */
	public static int SAFE_ENCHANT_COMMON;
	public static int SAFE_ENCHANT_FULL_BODY;

	public static int FESTIVAL_MIN_PARTY_SIZE;
	public static double FESTIVAL_RATE_PRICE;

	/**
	 * Dimensional Rift Config *
	 */
	public static int RIFT_MIN_PARTY_SIZE;
	public static int RIFT_SPAWN_DELAY; // Time in ms the party has to wait
	// until the mobs spawn
	public static int RIFT_MAX_JUMPS;
	public static int RIFT_AUTO_JUMPS_TIME;
	public static int RIFT_AUTO_JUMPS_TIME_RAND;
	public static int RIFT_ENTER_COST_RECRUIT;
	public static int RIFT_ENTER_COST_SOLDIER;
	public static int RIFT_ENTER_COST_OFFICER;
	public static int RIFT_ENTER_COST_CAPTAIN;
	public static int RIFT_ENTER_COST_COMMANDER;
	public static int RIFT_ENTER_COST_HERO;

	public static boolean ALLOW_TALK_WHILE_SITTING;

	public static boolean PARTY_LEADER_ONLY_CAN_INVITE;

	/**
	 * Разрешены ли клановые скилы? *
	 */
	public static boolean ALLOW_CLANSKILLS;

	/**
	 * Разрешено ли изучение скилов трансформации и саб классов без наличия выполненного квеста
	 */
	public static boolean ALLOW_LEARN_TRANS_SKILLS_WO_QUEST;

	/**
	 * Allow Manor system
	 */
	public static boolean ALLOW_MANOR;

	/**
	 * Manor Refresh Starting time
	 */
	public static int MANOR_REFRESH_TIME;

	/**
	 * Manor Refresh Min
	 */
	public static int MANOR_REFRESH_MIN;

	/**
	 * Manor Next Period Approve Starting time
	 */
	public static int MANOR_APPROVE_TIME;

	/**
	 * Manor Next Period Approve Min
	 */
	public static int MANOR_APPROVE_MIN;

	/**
	 * Manor Maintenance Time
	 */
	public static int MANOR_MAINTENANCE_PERIOD;

	public static boolean SERVICES_NO_TRADE_ONLY_OFFLINE;
	public static double SERVICES_TRADE_TAX;
	public static double SERVICES_OFFSHORE_TRADE_TAX;
	public static boolean SERVICES_OFFSHORE_NO_CASTLE_TAX;
	public static boolean SERVICES_TRADE_TAX_ONLY_OFFLINE;
	public static boolean SERVICES_TRADE_ONLY_FAR;
	public static int SERVICES_TRADE_RADIUS;
	public static int SERVICES_TRADE_MIN_LEVEL;

	public static boolean SERVICES_ENABLE_NO_CARRIER;
	public static int SERVICES_NO_CARRIER_DEFAULT_TIME;
	public static int SERVICES_NO_CARRIER_MAX_TIME;
	public static int SERVICES_NO_CARRIER_MIN_TIME;

	public static boolean ALT_OPEN_CLOAK_SLOT;

	public static boolean ALT_SHOW_SERVER_TIME;

	/**
	 * Geodata config
	 */
	public static int GEO_X_FIRST, GEO_Y_FIRST, GEO_X_LAST, GEO_Y_LAST;
	public static String GEOFILES_PATTERN;
	public static boolean ALLOW_GEODATA;
	public static boolean ALLOW_FALL_FROM_WALLS;
	public static boolean ALLOW_KEYBOARD_MOVE;
	public static boolean COMPACT_GEO;
	public static int CLIENT_Z_SHIFT;
	public static int MAX_Z_DIFF;
	public static int MIN_LAYER_HEIGHT;

	/**
	 * Geodata (Pathfind) config
	 */
	public static int PATHFIND_BOOST;
	public static boolean PATHFIND_DIAGONAL;
	public static boolean PATH_CLEAN;
	public static int PATHFIND_MAX_Z_DIFF;
	public static long PATHFIND_MAX_TIME;
	public static String PATHFIND_BUFFERS;

	public static boolean DEBUG;

	/* Item-Mall Configs */
	public static int GAME_POINT_ITEM_ID;

	public static int WEAR_DELAY;

	public static boolean GOODS_INVENTORY_ENABLED = true;
	public static boolean EX_NEW_PETITION_SYSTEM;
	public static boolean EX_JAPAN_MINIGAME;
	public static boolean EX_LECTURE_MARK;

	public static boolean LOGIN_SERVER_GM_ONLY;
	public static boolean ACCESS_ENTER_ONLY_PREMIUM;
	public static boolean LOGIN_SERVER_BRACKETS;
	public static boolean LOGIN_SERVER_IS_PVP;
	public static int LOGIN_SERVER_AGE_LIMIT;
	public static int LOGIN_SERVER_SERVER_TYPE;

	public static boolean L2TOP_MANAGER_ENABLED;
	public static int L2TOP_MANAGER_INTERVAL;
	public static String L2TOP_WEB_ADDRESS;
	public static String L2TOP_SMS_ADDRESS;
	public static String L2TOP_SERVER_ADDRESS;
	public static int L2TOP_SAVE_DAYS;
	public static String L2TOP_REWARD;
	public static boolean SELL_ALL_ITEMS_FREE;

	public static boolean SECOND_AUTH_ENABLED;
	public static int SECOND_AUTH_MAX_ATTEMPTS;
	public static int SECOND_AUTH_BAN_TIME;
	public static String SECOND_AUTH_REC_LINK;

	public static ArrayList<String> ExcludeDataFiles;

	public static int GARBAGE_COLLECTOR_INTERVAL;
	public static int THREAD_P_MOVE;
	public static int NPC_AI_MAX_THREAD;
	public static int PLAYER_AI_MAX_THREAD;
	public static int THREAD_P_PATHFIND;
	public static boolean ALLOW_FAKE_PLAYERS;
	public static int FAKE_PLAYERS_PERCENT;

	public static boolean L2_TOP_MANAGER_ENABLED;
	public static int L2_TOP_MANAGER_INTERVAL;
	public static String L2_TOP_WEB_ADDRESS;
	public static String L2_TOP_SMS_ADDRESS;
	public static String L2_TOP_SERVER_ADDRESS;
	public static int L2_TOP_SAVE_DAYS;
	public static int[] L2_TOP_REWARD;
	public static String L2_TOP_SERVER_PREFIX;
	public static int[] L2_TOP_REWARD_NO_CLAN;

	public static boolean MMO_TOP_MANAGER_ENABLED;
	public static int MMO_TOP_MANAGER_INTERVAL;
	public static String MMO_TOP_WEB_ADDRESS;
	public static String MMO_TOP_SERVER_ADDRESS;
	public static int MMO_TOP_SAVE_DAYS;
	public static int[] MMO_TOP_REWARD;
	public static int[] MMO_TOP_REWARD_NO_CLAN;

	public static boolean SMS_PAYMENT_MANAGER_ENABLED;
	public static String SMS_PAYMENT_WEB_ADDRESS;
	public static int SMS_PAYMENT_MANAGER_INTERVAL;
	public static int SMS_PAYMENT_SAVE_DAYS;
	public static String SMS_PAYMENT_SERVER_ADDRESS;
	public static int[] SMS_PAYMENT_REWARD;
	public static int[] SMS_PAYMENT_REWARD_NO_CLAN;
	public static boolean SMS_PAYMENT_TYPE;
	public static String SMS_PAYMENT_PREFIX;
	// Events
	public static double EVENT_APIL_FOOLS_DROP_CHANCE;

	public static int EVENT_CAPTURE_THE_FLAG_REWARD_ITEM_ID;
	public static double EVENT_CAPTURE_THE_FLAG_ITEM_COUNT;
	public static int EVENT_CAPTURE_THE_FLAG_TIME;
	public static boolean EVENT_CAPTURE_THE_FLAG_ITEM_RATE;
	public static int EVENT_CAPTURE_THE_FLAG_CHANCE_TO_START;

	public static double EVENT_CHANGE_OF_HEART_HEARTS_DROP_CHANCE;

	public static double EVENT_COFFER_OF_SHADOWS_PRICE_RATE;
	public static double EVENT_COFFER_OF_SHADOWS_REWARD_RATE;

	public static double EVENT_GLITTERING_MEDAL_MEDAL_DROP_CHANCE;
	public static double EVENT_GLITTERING_MEDAL_GLITTERING_DROP_CHANCE;

	public static double EVENT_L2_DAY_LETTER_DROP_CHANCE;

	public static int EVENT_LAST_HERO_ON_KILL_REWARD_ITEM_ID;
	public static double EVENT_LAST_HERO_ON_KILL_REWARD_ITEM_COUNT;
	public static int EVENT_LAST_HERO_TIME_TO_START;
	public static boolean EVENT_LAST_HERO_ON_KILL_ITEM_RATE;
	public static double EVENT_LAST_HERO_ON_FINISH_ITEM_COUNT;
	public static boolean EVENT_LAST_HERO_ON_FINISH_ITEM_RATE;
	public static int EVENT_LAST_HERO_CHANCE_TO_START;

	public static double EVENT_MARCH_8_DROP_CHANCE;
	public static double EVENT_MARCH_8_PRICE_RATE;

	public static int EVENT_MASTER_OF_ENCHANING_ENCHANT_CHANCE;
	public static int EVENT_MASTER_OF_ENCHANING_MAX_ENCHANT_LEVEL;
	public static int EVENT_MASTER_OF_ENCHANING_SAFE_ENCHANT_LEVEL;

	public static boolean EVENT_OTHER_EVENTS_BOUNTY_HUNTERS_ENABLED;

	public static long EVENT_SAVING_SNOWMAN_LOTERY_PRICE;
	public static int EVENT_SAVING_SNOWMAN_SANTA_SPAWN_CHANCE;

	public static double EVENT_THE_FALL_HARVEST_POLLEN_DROP_CHANCE;

	public static double EVENT_TRICK_OF_TRANS_KEYS_DROP_CHANCE;

	public static boolean CHARACTER_CREATE_NOBLE;
	public static boolean RANDOM_NPC_ENCHANT_EFFECT;

	public static boolean WindowsCountEnable;
	public static int WindowsCount;

	public static void loadServerConfig()
	{
		ExProperties serverSettings = load(CONFIGURATION_FILE);

		GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHost", "127.0.0.1");
		GAME_SERVER_LOGIN_PORT = serverSettings.getProperty("LoginPort", 9013);
		GAME_SERVER_LOGIN_CRYPT = serverSettings.getProperty("LoginUseCrypt", true);

		LOGIN_SERVER_AGE_LIMIT = serverSettings.getProperty("ServerAgeLimit", 0);
		LOGIN_SERVER_GM_ONLY = serverSettings.getProperty("ServerGMOnly", false);
		ACCESS_ENTER_ONLY_PREMIUM = serverSettings.getProperty("ServerAccessPremiumOnly", false);
		LOGIN_SERVER_BRACKETS = serverSettings.getProperty("ServerBrackets", false);
		LOGIN_SERVER_IS_PVP = serverSettings.getProperty("PvPServer", false);
		for(String a : serverSettings.getProperty("ServerType", ArrayUtils.EMPTY_STRING_ARRAY))
		{
			if(a.trim().isEmpty())
			{
				continue;
			}

			ServerType t = ServerType.valueOf(a.toUpperCase());
			LOGIN_SERVER_SERVER_TYPE |= t.getMask();
		}

		INTERNAL_HOSTNAME = serverSettings.getProperty("InternalHostname", "*");
		EXTERNAL_HOSTNAME = serverSettings.getProperty("ExternalHostname", "*");

		REQUEST_ID = serverSettings.getProperty("RequestServerID", 0);
		ACCEPT_ALTERNATE_ID = serverSettings.getProperty("AcceptAlternateID", true);

		GAMESERVER_HOSTNAME = serverSettings.getProperty("GameserverHostname");
		PORTS_GAME = serverSettings.getProperty("GameserverPort", new int[]{7777});

		EVERYBODY_HAS_ADMIN_RIGHTS = serverSettings.getProperty("EverybodyHasAdminRights", false);

		HIDE_GM_STATUS = serverSettings.getProperty("HideGMStatus", false);
		SHOW_GM_LOGIN = serverSettings.getProperty("ShowGMLogin", true);
		SAVE_GM_EFFECTS = serverSettings.getProperty("SaveGMEffects", false);

		CNAME_TEMPLATE = serverSettings.getProperty("CnameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{2,16}");
		CLAN_NAME_TEMPLATE = serverSettings.getProperty("ClanNameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{3,16}");
		CLAN_TITLE_TEMPLATE = serverSettings.getProperty("ClanTitleTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f \\p{Punct}]{1,16}");
		ALLY_NAME_TEMPLATE = serverSettings.getProperty("AllyNameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{3,16}");

		GLOBAL_SHOUT = serverSettings.getProperty("GlobalShout", false);
		GLOBAL_TRADE_CHAT = serverSettings.getProperty("GlobalTradeChat", false);
		CHAT_RANGE = serverSettings.getProperty("ChatRange", 1250);
		SHOUT_OFFSET = serverSettings.getProperty("ShoutOffset", 0);
		PREMIUM_HEROCHAT = serverSettings.getProperty("PremiumHeroChat", true);

		TRADE_WORDS = new GArray<String>();

		String T_WORLD = serverSettings.getProperty("TradeWords", "продаю,проgаю,пр0даю,продам,проgам,пр0дам,покупаю,куплю,кyплю,обменяю,выменяю,ВТТ,ВТС,ВТБ,WTB,WTT,WTS");
		String[] T_WORLDS = T_WORLD.split(",", -1);
		for(String w : T_WORLDS)
		{
			TRADE_WORDS.add(w);
		}
		System.out.println("Loaded " + TRADE_WORDS.size() + " trade words.");

		TRADE_CHATS_REPLACE = serverSettings.getProperty("TradeChatsReplace", false);

		LOG_CHAT = serverSettings.getProperty("LogChat", false);

		AUTODESTROY_ITEM_AFTER = serverSettings.getProperty("AutoDestroyDroppedItemAfter", 60) * 1000;
		DELETE_DAYS = serverSettings.getProperty("DeleteCharAfterDays", 7);
		PURGE_BYPASS_TASK_FREQUENCY = serverSettings.getProperty("PurgeTaskFrequency", 60);

		try
		{
			DATAPACK_ROOT = new File(serverSettings.getProperty("DatapackRoot", ".")).getCanonicalFile();
		}
		catch(IOException e)
		{
			_log.error("", e);
		}

		ALLOW_DISCARDITEM = serverSettings.getProperty("AllowDiscardItem", true);
		ALLOW_MAIL = serverSettings.getProperty("AllowMail", true);
		ALLOW_WAREHOUSE = serverSettings.getProperty("AllowWarehouse", true);
		ALLOW_WATER = serverSettings.getProperty("AllowWater", true);
		ALLOW_CURSED_WEAPONS = serverSettings.getProperty("AllowCursedWeapons", false);
		DROP_CURSED_WEAPONS_ON_KICK = serverSettings.getProperty("DropCursedWeaponsOnKick", false);

		MIN_PROTOCOL_REVISION = serverSettings.getProperty("MinProtocolRevision", 267);
		MAX_PROTOCOL_REVISION = serverSettings.getProperty("MaxProtocolRevision", 271);

		MIN_NPC_ANIMATION = serverSettings.getProperty("MinNPCAnimation", 5);
		MAX_NPC_ANIMATION = serverSettings.getProperty("MaxNPCAnimation", 90);

		SERVER_SIDE_NPC_NAME = serverSettings.getProperty("ServerSideNpcName", false);
		SERVER_SIDE_NPC_TITLE = serverSettings.getProperty("ServerSideNpcTitle", false);
		SERVER_SIDE_NPC_TITLE_ETC = serverSettings.getProperty("ServerSideNpcTitleEtc", false);

		AUTOSAVE = serverSettings.getProperty("Autosave", true);

		MAXIMUM_ONLINE_USERS = serverSettings.getProperty("MaximumOnlineUsers", 3000);

		DATABASE_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
		DATABASE_MAX_CONNECTIONS = serverSettings.getProperty("MaximumDbConnections", 10);
		DATABASE_MAX_IDLE_TIMEOUT = serverSettings.getProperty("MaxIdleConnectionTimeout", 600);
		DATABASE_IDLE_TEST_PERIOD = serverSettings.getProperty("IdleConnectionTestPeriod", 60);

		DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/l2jdb");
		DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
		DATABASE_PASSWORD = serverSettings.getProperty("Password", "");

		DATABASE_URL_LOGIN = serverSettings.getProperty("URL_Login", "jdbc:mysql://localhost/l2jdb");
		DATABASE_LOGIN_LOGIN = serverSettings.getProperty("Login_Login", "root");
		DATABASE_PASSWORD_LOGIN = serverSettings.getProperty("Password_Login", "");

		USER_INFO_INTERVAL = serverSettings.getProperty("UserInfoInterval", 100L);
		BROADCAST_STATS_INTERVAL = serverSettings.getProperty("BroadcastStatsInterval", true);
		BROADCAST_CHAR_INFO_INTERVAL = serverSettings.getProperty("BroadcastCharInfoInterval", 100L);

		EFFECT_TASK_MANAGER_COUNT = serverSettings.getProperty("EffectTaskManagers", 2);
		AI_TASK_MANAGER_COUNT = serverSettings.getProperty("AiTaskManagers", 2);

		SCHEDULED_THREAD_POOL_SIZE = serverSettings.getProperty("ScheduledThreadPoolSize", NCPUS * 4);
		EXECUTOR_THREAD_POOL_SIZE = serverSettings.getProperty("ExecutorThreadPoolSize", NCPUS * 2);

		SELECTOR_CONFIG.SLEEP_TIME = serverSettings.getProperty("SelectorSleepTime", 10L);
		SELECTOR_CONFIG.INTEREST_DELAY = serverSettings.getProperty("InterestDelay", 30L);
		SELECTOR_CONFIG.MAX_SEND_PER_PASS = serverSettings.getProperty("MaxSendPerPass", 32);
		SELECTOR_CONFIG.READ_BUFFER_SIZE = serverSettings.getProperty("ReadBufferSize", 65536);
		SELECTOR_CONFIG.WRITE_BUFFER_SIZE = serverSettings.getProperty("WriteBufferSize", 131072);
		SELECTOR_CONFIG.HELPER_BUFFER_COUNT = serverSettings.getProperty("BufferPoolSize", 64);

		CHAT_MESSAGE_MAX_LEN = serverSettings.getProperty("ChatMessageLimit", 1000);
		ABUSEWORD_BANCHAT = serverSettings.getProperty("ABUSEWORD_BANCHAT", false);
		int counter = 0;
		for(int id : serverSettings.getProperty("ABUSEWORD_BAN_CHANNEL", new int[]{0}))
		{
			BAN_CHANNEL_LIST[counter] = id;
			counter++;
		}
		ABUSEWORD_REPLACE = serverSettings.getProperty("ABUSEWORD_REPLACE", false);
		ABUSEWORD_REPLACE_STRING = serverSettings.getProperty("ABUSEWORD_REPLACE_STRING", "[censored]");
		BANCHAT_ANNOUNCE = serverSettings.getProperty("BANCHAT_ANNOUNCE", true);
		BANCHAT_ANNOUNCE_FOR_ALL_WORLD = serverSettings.getProperty("BANCHAT_ANNOUNCE_FOR_ALL_WORLD", true);
		BANCHAT_ANNOUNCE_NICK = serverSettings.getProperty("BANCHAT_ANNOUNCE_NICK", true);
		ABUSEWORD_BANTIME = serverSettings.getProperty("ABUSEWORD_UNBAN_TIMER", 30);

		CHATFILTER_MIN_LEVEL = serverSettings.getProperty("ChatFilterMinLevel", 0);
		counter = 0;
		for(int id : serverSettings.getProperty("ChatFilterChannels", new int[]{
			1,
			8
		}))
		{
			CHATFILTER_CHANNELS[counter] = id;
			counter++;
		}
		CHATFILTER_WORK_TYPE = serverSettings.getProperty("ChatFilterWorkType", 1);

		DEFAULT_LANG = serverSettings.getProperty("DefaultLang", "ru");
		RESTART_AT_TIME = serverSettings.getProperty("AutoRestartAt", "0 5 * * *");
		SHIFT_BY = serverSettings.getProperty("HShift", 12);
		SHIFT_BY_Z = serverSettings.getProperty("VShift", 11);
		MAP_MIN_Z = serverSettings.getProperty("MapMinZ", -32768);
		MAP_MAX_Z = serverSettings.getProperty("MapMaxZ", 32767);

		MOVE_PACKET_DELAY = serverSettings.getProperty("MovePacketDelay", 100);
		ATTACK_PACKET_DELAY = serverSettings.getProperty("AttackPacketDelay", 500);

		DAMAGE_FROM_FALLING = serverSettings.getProperty("DamageFromFalling", true);

		ALLOW_WEDDING = serverSettings.getProperty("AllowWedding", false);
		WEDDING_PRICE = serverSettings.getProperty("WeddingPrice", 500000);
		WEDDING_PUNISH_INFIDELITY = serverSettings.getProperty("WeddingPunishInfidelity", true);
		WEDDING_TELEPORT = serverSettings.getProperty("WeddingTeleport", true);
		WEDDING_TELEPORT_PRICE = serverSettings.getProperty("WeddingTeleportPrice", 500000);
		WEDDING_TELEPORT_INTERVAL = serverSettings.getProperty("WeddingTeleportInterval", 120);
		WEDDING_SAMESEX = serverSettings.getProperty("WeddingAllowSameSex", true);
		WEDDING_FORMALWEAR = serverSettings.getProperty("WeddingFormalWear", true);
		WEDDING_DIVORCE_COSTS = serverSettings.getProperty("WeddingDivorceCosts", 20);

		LOAD_CUSTOM_SPAWN = serverSettings.getProperty("StartWithCustomSpawn", false);
		SAVE_GM_CUSTOM_SPAWN = serverSettings.getProperty("SaveGmSpawnCustom", false);
		DONTLOADSPAWN = serverSettings.getProperty("StartWithoutSpawn", false);
		DONTLOADQUEST = serverSettings.getProperty("StartWithoutQuest", false);

		MAX_REFLECTIONS_COUNT = serverSettings.getProperty("MaxReflectionsCount", 300);

		WEAR_DELAY = serverSettings.getProperty("WearDelay", 5);

		HTM_CACHE_MODE = serverSettings.getProperty("HtmCacheMode", HtmCache.LAZY);

		SELL_ALL_ITEMS_FREE = serverSettings.getProperty("SellAllItemsFree", false);

		GARBAGE_COLLECTOR_INTERVAL = serverSettings.getProperty("GarbageCollectorInterval", 30) * 60000;

		THREAD_P_MOVE = serverSettings.getProperty("ThreadPoolSizeMove", 25);
		THREAD_P_PATHFIND = serverSettings.getProperty("ThreadPoolSizePathfind", 10);
		NPC_AI_MAX_THREAD = serverSettings.getProperty("NpcAiMaxThread", 10);
		PLAYER_AI_MAX_THREAD = serverSettings.getProperty("PlayerAiMaxThread", 20);

		ALLOW_FAKE_PLAYERS = serverSettings.getProperty("AllowFakePlayers", false);
		FAKE_PLAYERS_PERCENT = serverSettings.getProperty("FakePlayersPercent", 100);

		WindowsCountEnable = serverSettings.getProperty("WindowsCountEnable", false);
		WindowsCount = serverSettings.getProperty("WindowsCount", 1);

		SECOND_AUTH_ENABLED = serverSettings.getProperty("SecondAuthEnabled", false);
		SECOND_AUTH_MAX_ATTEMPTS = serverSettings.getProperty("SecondAuthMaxAttempts", 5);
		SECOND_AUTH_BAN_TIME = serverSettings.getProperty("SecondAuthBanTime", 480);
		SECOND_AUTH_REC_LINK = serverSettings.getProperty("SecondAuthRecoveryLink", "http://la2era.ru/");

		ExcludeDataFiles = new ArrayList<>();
		for(String s : serverSettings.getProperty("ExcludeDataFiles", "").split(","))
		{
			ExcludeDataFiles.add(s);
		}
	}

	public static void loadTelnetConfig()
	{
		ExProperties telnetSettings = load(TELNET_CONFIGURATION_FILE);

		IS_TELNET_ENABLED = telnetSettings.getProperty("EnableTelnet", false);
		TELNET_DEFAULT_ENCODING = telnetSettings.getProperty("TelnetEncoding", "UTF-8");
		TELNET_PORT = telnetSettings.getProperty("Port", 7000);
		TELNET_HOSTNAME = telnetSettings.getProperty("BindAddress", "127.0.0.1");
		TELNET_PASSWORD = telnetSettings.getProperty("Password", "");
	}

	public static void loadTopConfig()
	{
		ExProperties topSetting = load(TOP_CONFIG_FILE);

		L2_TOP_MANAGER_ENABLED = topSetting.getProperty("L2TopManagerEnabled", false);
		L2_TOP_MANAGER_INTERVAL = topSetting.getProperty("L2TopManagerInterval", 300000);
		L2_TOP_WEB_ADDRESS = topSetting.getProperty("L2TopWebAddress", "");
		L2_TOP_SMS_ADDRESS = topSetting.getProperty("L2TopSmsAddress", "");
		L2_TOP_SERVER_ADDRESS = topSetting.getProperty("L2TopServerAddress", "revolt-team.com");
		L2_TOP_SAVE_DAYS = topSetting.getProperty("L2TopSaveDays", 30);
		L2_TOP_REWARD = topSetting.getProperty("L2TopReward", new int[0]);
		L2_TOP_SERVER_PREFIX = topSetting.getProperty("L2TopServerPrefix", "");
		L2_TOP_REWARD_NO_CLAN = topSetting.getProperty("L2TopRewardNoClan", new int[0]);

		MMO_TOP_MANAGER_ENABLED = topSetting.getProperty("MMOTopEnable", false);
		MMO_TOP_MANAGER_INTERVAL = topSetting.getProperty("MMOTopManagerInterval", 300000);
		MMO_TOP_WEB_ADDRESS = topSetting.getProperty("MMOTopUrl", "");
		MMO_TOP_SERVER_ADDRESS = topSetting.getProperty("MMOTopServerAddress", "revolt-team.com");
		MMO_TOP_SAVE_DAYS = topSetting.getProperty("MMOTopSaveDays", 30);
		MMO_TOP_REWARD = topSetting.getProperty("MMOTopReward", new int[0]);
		MMO_TOP_REWARD_NO_CLAN = topSetting.getProperty("MMOTopRewardNoClan", new int[0]);
	}

	public static void loadPaymentConfig()
	{
		ExProperties paymentSetting = load(PAYMENT_CONFIG_FILE);

		SMS_PAYMENT_MANAGER_ENABLED = paymentSetting.getProperty("SMSPaymentEnabled", false);
		SMS_PAYMENT_WEB_ADDRESS = paymentSetting.getProperty("SMSPaymentWebAddress", "");
		SMS_PAYMENT_MANAGER_INTERVAL = paymentSetting.getProperty("SMSPaymentManagerInterval", 300000);
		SMS_PAYMENT_SAVE_DAYS = paymentSetting.getProperty("SMSPaymentSaveDays", 30);
		SMS_PAYMENT_SERVER_ADDRESS = paymentSetting.getProperty("SMSPaymentServerAddress", "revolt-team.com");
		SMS_PAYMENT_REWARD = paymentSetting.getProperty("SMSPaymentReward", new int[0]);
		SMS_PAYMENT_REWARD_NO_CLAN = paymentSetting.getProperty("SMSPaymentRewardNoClan", new int[0]);
		SMS_PAYMENT_TYPE = paymentSetting.getProperty("SMSPaymentProfitOrSum", true);
		SMS_PAYMENT_PREFIX = paymentSetting.getProperty("SMSPaymentPrefix", "");
	}

	public static void loadResidenceConfig()
	{
		ExProperties residenceSettings = load(RESIDENCE_CONFIG_FILE);

		CH_BID_GRADE1_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanLevel", 2);
		CH_BID_GRADE1_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanMembers", 1);
		CH_BID_GRADE1_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanMembersAvgLevel", 1);
		CH_BID_GRADE2_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanLevel", 2);
		CH_BID_GRADE2_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanMembers", 1);
		CH_BID_GRADE2_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanMembersAvgLevel", 1);
		CH_BID_GRADE3_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanLevel", 2);
		CH_BID_GRADE3_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanMembers", 1);
		CH_BID_GRADE3_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanMembersAvgLevel", 1);
		RESIDENCE_LEASE_FUNC_MULTIPLIER = residenceSettings.getProperty("ResidenceLeaseFuncMultiplier", 1.);
		RESIDENCE_LEASE_MULTIPLIER = residenceSettings.getProperty("ResidenceLeaseMultiplier", 1.);

		CASTLE_SELECT_HOURS = residenceSettings.getProperty("CastleSelectHours", new int[]{
			16,
			20
		});
		int[] tempCastleValidatonTime = residenceSettings.getProperty("CastleValidationDate", new int[]{
			2,
			4,
			2003
		});
		CASTLE_VALIDATION_DATE = Calendar.getInstance();
		CASTLE_VALIDATION_DATE.set(Calendar.DAY_OF_MONTH, tempCastleValidatonTime[0]);
		CASTLE_VALIDATION_DATE.set(Calendar.MONTH, tempCastleValidatonTime[1] - 1);
		CASTLE_VALIDATION_DATE.set(Calendar.YEAR, tempCastleValidatonTime[2]);
		CASTLE_VALIDATION_DATE.set(Calendar.HOUR_OF_DAY, 0);
		CASTLE_VALIDATION_DATE.set(Calendar.MINUTE, 0);
		CASTLE_VALIDATION_DATE.set(Calendar.SECOND, 0);
		CASTLE_VALIDATION_DATE.set(Calendar.MILLISECOND, 0);
	}

	public static void loadOtherConfig()
	{
		ExProperties otherSettings = load(OTHER_CONFIG_FILE);

		AWAKING_FREE = otherSettings.getProperty("AwakingFree", false);

		DEEPBLUE_DROP_RULES = otherSettings.getProperty("UseDeepBlueDropRules", true);
		DEEPBLUE_DROP_MAXDIFF = otherSettings.getProperty("DeepBlueDropMaxDiff", 8);
		DEEPBLUE_DROP_RAID_MAXDIFF = otherSettings.getProperty("DeepBlueDropRaidMaxDiff", 2);

		SWIMING_SPEED = otherSettings.getProperty("SwimingSpeedTemplate", 50);

		/* Inventory slots limits */
		INVENTORY_MAXIMUM_NO_DWARF = otherSettings.getProperty("MaximumSlotsForNoDwarf", 80);
		INVENTORY_MAXIMUM_DWARF = otherSettings.getProperty("MaximumSlotsForDwarf", 100);
		INVENTORY_MAXIMUM_GM = otherSettings.getProperty("MaximumSlotsForGMPlayer", 250);
		QUEST_INVENTORY_MAXIMUM = otherSettings.getProperty("MaximumSlotsForQuests", 100);

		MULTISELL_SIZE = otherSettings.getProperty("MultisellPageSize", 10);

		/* Warehouse slots limits */
		WAREHOUSE_SLOTS_NO_DWARF = otherSettings.getProperty("BaseWarehouseSlotsForNoDwarf", 100);
		WAREHOUSE_SLOTS_DWARF = otherSettings.getProperty("BaseWarehouseSlotsForDwarf", 120);
		WAREHOUSE_SLOTS_CLAN = otherSettings.getProperty("MaximumWarehouseSlotsForClan", 200);
		FREIGHT_SLOTS = otherSettings.getProperty("MaximumFreightSlots", 10);

		ARMOR_OVERENCHANT_HPBONUS_LIMIT = otherSettings.getProperty("ArmorOverEnchantHPBonusLimit", 10) - 3;

		SHOW_ENCHANT_EFFECT_RESULT = otherSettings.getProperty("ShowEnchantEffectResult", true);

		REGEN_SIT_WAIT = otherSettings.getProperty("RegenSitWait", false);

		STARTING_ADENA = otherSettings.getProperty("StartingAdena", 0);
		UNSTUCK_SKILL = otherSettings.getProperty("UnstuckSkill", true);

		/* Amount of HP, MP, and CP is restored */
		RESPAWN_RESTORE_CP = otherSettings.getProperty("RespawnRestoreCP", 0.) / 100;
		RESPAWN_RESTORE_HP = otherSettings.getProperty("RespawnRestoreHP", 65.) / 100;
		RESPAWN_RESTORE_MP = otherSettings.getProperty("RespawnRestoreMP", 0.) / 100;

		/* Maximum number of available slots for pvt stores */
		MAX_PVTSTORE_SLOTS_DWARF = otherSettings.getProperty("MaxPvtStoreSlotsDwarf", 5);
		MAX_PVTSTORE_SLOTS_OTHER = otherSettings.getProperty("MaxPvtStoreSlotsOther", 4);
		MAX_PVTCRAFT_SLOTS = otherSettings.getProperty("MaxPvtManufactureSlots", 20);

		SENDSTATUS_TRADE_JUST_OFFLINE = otherSettings.getProperty("SendStatusTradeJustOffline", false);
		SENDSTATUS_TRADE_MOD = otherSettings.getProperty("SendStatusTradeMod", 1.);

		ANNOUNCE_MAMMON_SPAWN = otherSettings.getProperty("AnnounceMammonSpawn", true);

		GM_NAME_COLOUR = Integer.decode("0x" + otherSettings.getProperty("GMNameColour", "FFFFFF"));
		GM_HERO_AURA = otherSettings.getProperty("GMHeroAura", false);
		NORMAL_NAME_COLOUR = Integer.decode("0x" + otherSettings.getProperty("NormalNameColour", "FFFFFF"));
		CLANLEADER_NAME_COLOUR = Integer.decode("0x" + otherSettings.getProperty("ClanleaderNameColour", "FFFFFF"));

		GAME_POINT_ITEM_ID = otherSettings.getProperty("GamePointItemId", -1);
	}

	public static void loadSpoilConfig()
	{
		ExProperties spoilSettings = load(SPOIL_CONFIG_FILE);

		BASE_SPOIL_RATE = spoilSettings.getProperty("BasePercentChanceOfSpoilSuccess", 78.);
		MINIMUM_SPOIL_RATE = spoilSettings.getProperty("MinimumPercentChanceOfSpoilSuccess", 1.);
		ALT_SPOIL_FORMULA = spoilSettings.getProperty("AltFormula", false);
		MANOR_SOWING_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfSowingSuccess", 100.);
		MANOR_SOWING_ALT_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfSowingAltSuccess", 10.);
		MANOR_HARVESTING_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfHarvestingSuccess", 90.);
		MANOR_DIFF_PLAYER_TARGET = spoilSettings.getProperty("MinDiffPlayerMob", 5);
		MANOR_DIFF_PLAYER_TARGET_PENALTY = spoilSettings.getProperty("DiffPlayerMobPenalty", 5.);
		MANOR_DIFF_SEED_TARGET = spoilSettings.getProperty("MinDiffSeedMob", 5);
		MANOR_DIFF_SEED_TARGET_PENALTY = spoilSettings.getProperty("DiffSeedMobPenalty", 5.);
		ALLOW_MANOR = spoilSettings.getProperty("AllowManor", true);
		MANOR_REFRESH_TIME = spoilSettings.getProperty("AltManorRefreshTime", 20);
		MANOR_REFRESH_MIN = spoilSettings.getProperty("AltManorRefreshMin", 00);
		MANOR_APPROVE_TIME = spoilSettings.getProperty("AltManorApproveTime", 6);
		MANOR_APPROVE_MIN = spoilSettings.getProperty("AltManorApproveMin", 00);
		MANOR_MAINTENANCE_PERIOD = spoilSettings.getProperty("AltManorMaintenancePeriod", 360000);
	}

	public static void loadFormulasConfig()
	{
		ExProperties formulasSettings = load(FORMULAS_CONFIGURATION_FILE);

		SKILLS_CHANCE_MOD = formulasSettings.getProperty("SkillsChanceMod", 11.);
		SKILLS_CHANCE_POW = formulasSettings.getProperty("SkillsChancePow", 0.5);
		SKILLS_CHANCE_MIN = formulasSettings.getProperty("SkillsChanceMin", 5.);
		SKILLS_CHANCE_CAP = formulasSettings.getProperty("SkillsChanceCap", 95.);
		SKILLS_CAST_TIME_MIN = formulasSettings.getProperty("SkillsCastTimeMin", 333);

		ALT_ABSORB_DAMAGE_MODIFIER = formulasSettings.getProperty("AbsorbDamageModifier", 1.0);

		LIM_PATK = formulasSettings.getProperty("LimitPatk", 20000);
		LIM_MATK = formulasSettings.getProperty("LimitMAtk", 25000);
		LIM_PDEF = formulasSettings.getProperty("LimitPDef", 15000);
		LIM_MDEF = formulasSettings.getProperty("LimitMDef", 15000);
		LIM_PATK_SPD = formulasSettings.getProperty("LimitPatkSpd", 1500);
		LIM_MATK_SPD = formulasSettings.getProperty("LimitMatkSpd", 1999);
		LIM_CRIT_DAM = formulasSettings.getProperty("LimitCriticalDamage", 2000);
		LIM_CRIT = formulasSettings.getProperty("LimitCritical", 500);
		LIM_MCRIT = formulasSettings.getProperty("LimitMCritical", 20);
		LIM_ACCURACY = formulasSettings.getProperty("LimitAccuracy", 200);
		LIM_EVASION = formulasSettings.getProperty("LimitEvasion", 200);
		LIM_MOVE = formulasSettings.getProperty("LimitMove", 250);

		LIM_FAME = formulasSettings.getProperty("LimitFame", 50000);

		ALT_NPC_PATK_MODIFIER = formulasSettings.getProperty("NpcPAtkModifier", 1.0);
		ALT_NPC_MATK_MODIFIER = formulasSettings.getProperty("NpcMAtkModifier", 1.0);
		ALT_NPC_MAXHP_MODIFIER = formulasSettings.getProperty("NpcMaxHpModifier", 1.58);
		ALT_NPC_MAXMP_MODIFIER = formulasSettings.getProperty("NpcMapMpModifier", 1.11);

		ALT_POLE_DAMAGE_MODIFIER = formulasSettings.getProperty("PoleDamageModifier", 1.0);
	}

	public static void loadExtSettings()
	{
		ExProperties properties = load(EXT_FILE);

		EX_NEW_PETITION_SYSTEM = properties.getProperty("NewPetitionSystem", false);
		EX_JAPAN_MINIGAME = properties.getProperty("JapanMinigame", false);
		EX_LECTURE_MARK = properties.getProperty("LectureMark", false);
	}

	public static void loadAltSettings()
	{
		ExProperties altSettings = load(ALT_SETTINGS_FILE);

		ALT_ARENA_EXP = altSettings.getProperty("ArenaExp", true);
		ALT_GAME_DELEVEL = altSettings.getProperty("Delevel", true);
		AUTO_LOOT = altSettings.getProperty("AutoLoot", false);
		AUTO_LOOT_HERBS = altSettings.getProperty("AutoLootHerbs", false);
		AUTO_LOOT_INDIVIDUAL = altSettings.getProperty("AutoLootIndividual", false);
		AUTO_LOOT_FROM_RAIDS = altSettings.getProperty("AutoLootFromRaids", false);
		AUTO_LOOT_PK = altSettings.getProperty("AutoLootPK", false);
		ALT_GAME_KARMA_PLAYER_CAN_SHOP = altSettings.getProperty("AltKarmaPlayerCanShop", false);
		SAVING_SPS = altSettings.getProperty("SavingSpS", false);
		MANAHEAL_SPS_BONUS = altSettings.getProperty("ManahealSpSBonus", false);
		CRAFT_MASTERWORK_CHANCE = altSettings.getProperty("CraftMasterworkChance", 3.);
		CRAFT_DOUBLECRAFT_CHANCE = altSettings.getProperty("CraftDoubleCraftChance", 3.);
		ALT_RAID_RESPAWN_MULTIPLIER = altSettings.getProperty("AltRaidRespawnMultiplier", 1.0);
		ALT_ALLOW_AUGMENT_ALL = altSettings.getProperty("AugmentAll", false);
		ALT_ALLOW_DROP_AUGMENTED = altSettings.getProperty("AlowDropAugmented", false);
		ALT_GAME_UNREGISTER_RECIPE = altSettings.getProperty("AltUnregisterRecipe", true);
		ALT_GAME_SHOW_DROPLIST = altSettings.getProperty("AltShowDroplist", true);
		ALLOW_NPC_SHIFTCLICK = altSettings.getProperty("AllowShiftClick", true);
		ALT_FULL_NPC_STATS_PAGE = altSettings.getProperty("AltFullStatsPage", false);
		ALT_GAME_SUBCLASS_WITHOUT_QUESTS = altSettings.getProperty("AltAllowSubClassWithoutQuest", false);
		ALT_ALLOW_SUBCLASS_WITHOUT_BAIUM = altSettings.getProperty("AltAllowSubClassWithoutBaium", true);
		ALT_GAME_LEVEL_TO_GET_SUBCLASS = altSettings.getProperty("AltLevelToGetSubclass", 75);
		ALT_GAME_SUB_ADD = altSettings.getProperty("AltSubAdd", 0);
		ALT_MAX_LEVEL = Math.min(altSettings.getProperty("AltMaxLevel", 99), Experience.LEVEL.length - 1);
		ALT_MAX_SUB_LEVEL = Math.min(altSettings.getProperty("AltMaxSubLevel", 80), Experience.LEVEL.length - 1);
		ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE = altSettings.getProperty("AltAllowOthersWithdrawFromClanWarehouse", false);
		ALT_ALLOW_CLAN_COMMAND_ONLY_FOR_CLAN_LEADER = altSettings.getProperty("AltAllowClanCommandOnlyForClanLeader", true);

		ALT_GAME_REQUIRE_CLAN_CASTLE = altSettings.getProperty("AltRequireClanCastle", false);
		ALT_GAME_REQUIRE_CASTLE_DAWN = altSettings.getProperty("AltRequireCastleDawn", true);
		ALT_GAME_ALLOW_ADENA_DAWN = altSettings.getProperty("AltAllowAdenaDawn", true);
		ALT_ADD_RECIPES = altSettings.getProperty("AltAddRecipes", 0);
		SS_ANNOUNCE_PERIOD = altSettings.getProperty("SSAnnouncePeriod", 0);
		PETITIONING_ALLOWED = altSettings.getProperty("PetitioningAllowed", true);
		MAX_PETITIONS_PER_PLAYER = altSettings.getProperty("MaxPetitionsPerPlayer", 5);
		MAX_PETITIONS_PENDING = altSettings.getProperty("MaxPetitionsPending", 25);
		ALT_SOCIAL_ACTION_REUSE = altSettings.getProperty("AltSocialActionReuse", false);
		ALT_SIMPLE_SIGNS = altSettings.getProperty("PushkinSignsOptions", false);
		ALT_TELE_TO_CATACOMBS = altSettings.getProperty("TeleToCatacombs", false);
		ALT_BS_CRYSTALLIZE = altSettings.getProperty("BSCrystallize", false);
		ALT_MAMMON_UPGRADE = altSettings.getProperty("MammonUpgrade", 6680500);
		ALT_MAMMON_EXCHANGE = altSettings.getProperty("MammonExchange", 10091400);
		ALT_ALLOW_TATTOO = altSettings.getProperty("AllowTattoo", false);
		ALT_DEATH_PENALTY = altSettings.getProperty("EnableAltDeathPenalty", false);
		ALLOW_DEATH_PENALTY_C5 = altSettings.getProperty("EnableDeathPenaltyC5", true);
		ALT_DEATH_PENALTY_C5_CHANCE = altSettings.getProperty("DeathPenaltyC5Chance", 10);
		ALT_DEATH_PENALTY_C5_CHAOTIC_RECOVERY = altSettings.getProperty("ChaoticCanUseScrollOfRecovery", false);
		ALT_DEATH_PENALTY_C5_EXPERIENCE_PENALTY = altSettings.getProperty("DeathPenaltyC5RateExpPenalty", 1);
		ALT_DEATH_PENALTY_C5_KARMA_PENALTY = altSettings.getProperty("DeathPenaltyC5RateKarma", 1);
		ALT_PK_DEATH_RATE = altSettings.getProperty("AltPKDeathRate", 0.);
		NONOWNER_ITEM_PICKUP_DELAY = altSettings.getProperty("NonOwnerItemPickupDelay", 15L) * 1000L;
		ALT_NO_LASTHIT = altSettings.getProperty("NoLasthitOnRaid", false);
		ALT_KAMALOKA_NIGHTMARES_PREMIUM_ONLY = altSettings.getProperty("KamalokaNightmaresPremiumOnly", false);
		ALT_KAMALOKA_NIGHTMARE_REENTER = altSettings.getProperty("SellReenterNightmaresTicket", true);
		ALT_KAMALOKA_ABYSS_REENTER = altSettings.getProperty("SellReenterAbyssTicket", true);
		ALT_KAMALOKA_LAB_REENTER = altSettings.getProperty("SellReenterLabyrinthTicket", true);
		ALT_PET_HEAL_BATTLE_ONLY = altSettings.getProperty("PetsHealOnlyInBattle", true);
		CHAR_TITLE = altSettings.getProperty("CharTitle", false);
		ADD_CHAR_TITLE = altSettings.getProperty("CharAddTitle", "");

		ALT_ALLOW_SELL_COMMON = altSettings.getProperty("AllowSellCommon", true);
		ALT_ALLOW_SHADOW_WEAPONS = altSettings.getProperty("AllowShadowWeapons", true);
		ALT_DISABLED_MULTISELL = altSettings.getProperty("DisabledMultisells", ArrayUtils.EMPTY_INT_ARRAY);
		ALT_SHOP_PRICE_LIMITS = altSettings.getProperty("ShopPriceLimits", ArrayUtils.EMPTY_INT_ARRAY);
		ALT_SHOP_UNALLOWED_ITEMS = altSettings.getProperty("ShopUnallowedItems", ArrayUtils.EMPTY_INT_ARRAY);

		ALT_ALLOWED_PET_POTIONS = altSettings.getProperty("AllowedPetPotions", new int[]{
			735,
			1060,
			1061,
			1062,
			1374,
			1375,
			1539,
			1540,
			6035,
			6036
		});

		FESTIVAL_MIN_PARTY_SIZE = altSettings.getProperty("FestivalMinPartySize", 5);
		FESTIVAL_RATE_PRICE = altSettings.getProperty("FestivalRatePrice", 1.0);

		RIFT_MIN_PARTY_SIZE = altSettings.getProperty("RiftMinPartySize", 5);
		RIFT_SPAWN_DELAY = altSettings.getProperty("RiftSpawnDelay", 10000);
		RIFT_MAX_JUMPS = altSettings.getProperty("MaxRiftJumps", 4);
		RIFT_AUTO_JUMPS_TIME = altSettings.getProperty("AutoJumpsDelay", 8);
		RIFT_AUTO_JUMPS_TIME_RAND = altSettings.getProperty("AutoJumpsDelayRandom", 120000);

		RIFT_ENTER_COST_RECRUIT = altSettings.getProperty("RecruitFC", 18);
		RIFT_ENTER_COST_SOLDIER = altSettings.getProperty("SoldierFC", 21);
		RIFT_ENTER_COST_OFFICER = altSettings.getProperty("OfficerFC", 24);
		RIFT_ENTER_COST_CAPTAIN = altSettings.getProperty("CaptainFC", 27);
		RIFT_ENTER_COST_COMMANDER = altSettings.getProperty("CommanderFC", 30);
		RIFT_ENTER_COST_HERO = altSettings.getProperty("HeroFC", 33);
		PARTY_LEADER_ONLY_CAN_INVITE = altSettings.getProperty("PartyLeaderOnlyCanInvite", true);
		ALLOW_TALK_WHILE_SITTING = altSettings.getProperty("AllowTalkWhileSitting", true);
		ALLOW_NOBLE_TP_TO_ALL = altSettings.getProperty("AllowNobleTPToAll", false);

		GATEKEEPER_MODIFIER = altSettings.getProperty("GkCostMultiplier", 1.0);
		GATEKEEPER_FREE = altSettings.getProperty("GkFree", 40);
		CRUMA_GATEKEEPER_LVL = altSettings.getProperty("GkCruma", 65);
		ALT_IMPROVED_PETS_LIMITED_USE = altSettings.getProperty("ImprovedPetsLimitedUse", false);

		ALT_CHAMPION_CHANCE1 = altSettings.getProperty("AltChampionChance1", 0.);
		ALT_CHAMPION_CHANCE2 = altSettings.getProperty("AltChampionChance2", 0.);
		ALT_CHAMPION_CAN_BE_AGGRO = altSettings.getProperty("AltChampionAggro", false);
		ALT_CHAMPION_CAN_BE_SOCIAL = altSettings.getProperty("AltChampionSocial", false);
		ALT_CHAMPION_TOP_LEVEL = altSettings.getProperty("AltChampionTopLevel", 75);

		ALT_VITALITY_ENABLED = altSettings.getProperty("AltVitalityEnabled", true);
		ALT_VITALITY_RATE_EXP = altSettings.getProperty("AltVitalityRateExp", 2.0);
		ALT_VITALITY_RATE_SP = altSettings.getProperty("AltVitalityRateSp", 2.0);
		ALT_VITALITY_PREMIUM_RATE_EXP = altSettings.getProperty("AltVitalityPremiumRateExp", 3.0);
		ALT_VITALITY_PREMIUM_RATE_SP = altSettings.getProperty("AltVitalityPremiumRateSp", 2.0);
		ALT_VITALITY_RATE_DROP = altSettings.getProperty("AltVitalityRateDrop", 3.0);
		ALT_VITALITY_PREMIUM_RATE_DROP = altSettings.getProperty("AltVitalityPremiumRateDrop", 2.0);
		ALT_VITALITY_CONSUME_RATE = altSettings.getProperty("AltVitalityConsumeRate", 1.);
		MAX_VITALITY_ITEMS = altSettings.getProperty("AltVitalityMaxItems", 5);

		ALT_PCBANG_POINTS_ENABLED = altSettings.getProperty("AltPcBangPointsEnabled", false);
		ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE = altSettings.getProperty("AltPcBangPointsDoubleChance", 10.);
		ALT_PCBANG_POINTS_BONUS = altSettings.getProperty("AltPcBangPointsBonus", 0);
		ALT_PCBANG_POINTS_DELAY = altSettings.getProperty("AltPcBangPointsDelay", 20);
		ALT_PCBANG_POINTS_MIN_LVL = altSettings.getProperty("AltPcBangPointsMinLvl", 1);

		ALT_DEBUG_ENABLED = altSettings.getProperty("AltDebugEnabled", false);
		ALT_DEBUG_PVP_ENABLED = altSettings.getProperty("AltDebugPvPEnabled", false);
		ALT_DEBUG_PVP_DUEL_ONLY = altSettings.getProperty("AltDebugPvPDuelOnly", true);
		ALT_DEBUG_PVE_ENABLED = altSettings.getProperty("AltDebugPvEEnabled", false);

		ALT_MAX_ALLY_SIZE = altSettings.getProperty("AltMaxAllySize", 3);
		ALT_PARTY_DISTRIBUTION_RANGE = altSettings.getProperty("AltPartyDistributionRange", 1500);
		ALT_PARTY_BONUS = altSettings.getProperty("AltPartyBonus", new double[]{
			1.00,
			1.10,
			1.20,
			1.30,
			1.40,
			1.50,
			2.00,
			2.10,
			2.20
		});

		ALT_ALL_PHYS_SKILLS_OVERHIT = altSettings.getProperty("AltAllPhysSkillsOverhit", true);
		ALT_REMOVE_SKILLS_ON_DELEVEL = altSettings.getProperty("AltRemoveSkillsOnDelevel", true);
		ALT_USE_BOW_REUSE_MODIFIER = altSettings.getProperty("AltUseBowReuseModifier", true);
		ALLOW_CH_DOOR_OPEN_ON_CLICK = altSettings.getProperty("AllowChDoorOpenOnClick", true);
		ALT_CH_ALL_BUFFS = altSettings.getProperty("AltChAllBuffs", false);
		ALT_CH_ALLOW_1H_BUFFS = altSettings.getProperty("AltChAllowHourBuff", false);
		ALT_CH_SIMPLE_DIALOG = altSettings.getProperty("AltChSimpleDialog", false);

		AUGMENTATION_NG_SKILL_CHANCE = altSettings.getProperty("AugmentationNGSkillChance", 15);
		AUGMENTATION_NG_GLOW_CHANCE = altSettings.getProperty("AugmentationNGGlowChance", 0);
		AUGMENTATION_MID_SKILL_CHANCE = altSettings.getProperty("AugmentationMidSkillChance", 30);
		AUGMENTATION_MID_GLOW_CHANCE = altSettings.getProperty("AugmentationMidGlowChance", 40);
		AUGMENTATION_HIGH_SKILL_CHANCE = altSettings.getProperty("AugmentationHighSkillChance", 45);
		AUGMENTATION_HIGH_GLOW_CHANCE = altSettings.getProperty("AugmentationHighGlowChance", 70);
		AUGMENTATION_TOP_SKILL_CHANCE = altSettings.getProperty("AugmentationTopSkillChance", 60);
		AUGMENTATION_TOP_GLOW_CHANCE = altSettings.getProperty("AugmentationTopGlowChance", 100);
		AUGMENTATION_BASESTAT_CHANCE = altSettings.getProperty("AugmentationBaseStatChance", 1);
		AUGMENTATION_ACC_SKILL_CHANCE = altSettings.getProperty("AugmentationAccSkillChance", 10);

		ALT_OPEN_CLOAK_SLOT = altSettings.getProperty("OpenCloakSlot", false);

		ALT_SHOW_SERVER_TIME = altSettings.getProperty("ShowServerTime", false);

		FOLLOW_RANGE = altSettings.getProperty("FollowRange", 100);

		ALT_ITEM_AUCTION_ENABLED = altSettings.getProperty("AltItemAuctionEnabled", true);
		ALT_ITEM_AUCTION_CAN_REBID = altSettings.getProperty("AltItemAuctionCanRebid", false);
		ALT_ITEM_AUCTION_START_ANNOUNCE = altSettings.getProperty("AltItemAuctionAnnounce", true);
		ALT_ITEM_AUCTION_BID_ITEM_ID = altSettings.getProperty("AltItemAuctionBidItemId", 57);
		ALT_ITEM_AUCTION_MAX_BID = altSettings.getProperty("AltItemAuctionMaxBid", 1000000L);
		ALT_ITEM_AUCTION_MAX_CANCEL_TIME_IN_MILLIS = altSettings.getProperty("AltItemAuctionMaxCancelTimeInMillis", 604800000);

		ALT_FISH_CHAMPIONSHIP_ENABLED = altSettings.getProperty("AltFishChampionshipEnabled", true);
		ALT_FISH_CHAMPIONSHIP_REWARD_ITEM = altSettings.getProperty("AltFishChampionshipRewardItemId", 57);
		ALT_FISH_CHAMPIONSHIP_REWARD_1 = altSettings.getProperty("AltFishChampionshipReward1", 800000);
		ALT_FISH_CHAMPIONSHIP_REWARD_2 = altSettings.getProperty("AltFishChampionshipReward2", 500000);
		ALT_FISH_CHAMPIONSHIP_REWARD_3 = altSettings.getProperty("AltFishChampionshipReward3", 300000);
		ALT_FISH_CHAMPIONSHIP_REWARD_4 = altSettings.getProperty("AltFishChampionshipReward4", 200000);
		ALT_FISH_CHAMPIONSHIP_REWARD_5 = altSettings.getProperty("AltFishChampionshipReward5", 100000);

		ALT_ENABLE_BLOCK_CHECKER_EVENT = altSettings.getProperty("EnableBlockCheckerEvent", true);
		ALT_MIN_BLOCK_CHECKER_TEAM_MEMBERS = Math.min(Math.max(altSettings.getProperty("BlockCheckerMinTeamMembers", 1), 1), 6);
		ALT_RATE_COINS_REWARD_BLOCK_CHECKER = altSettings.getProperty("BlockCheckerRateCoinReward", 1.);

		ALT_HBCE_FAIR_PLAY = altSettings.getProperty("HBCEFairPlay", false);

		ALT_PET_INVENTORY_LIMIT = altSettings.getProperty("AltPetInventoryLimit", 12);

		ALT_IF_VOICE_COMMAND_ALLOWED = altSettings.getProperty("IfVoiceCommandAllowed", true);
	}

	public static void loadServicesSettings()
	{
		ExProperties servicesSettings = load(SERVICES_FILE);

		for(int id : servicesSettings.getProperty("AllowClassMasters", ArrayUtils.EMPTY_INT_ARRAY))
		{
			if(id != 0)
			{
				ALLOW_CLASS_MASTERS_LIST.add(id);
			}
		}

		CLASS_MASTERS_PRICE = servicesSettings.getProperty("ClassMastersPrice", "0,0,0,0");
		if(CLASS_MASTERS_PRICE.length() >= 5)
		{
			int level = 1;
			for(String id : CLASS_MASTERS_PRICE.split(","))
			{
				CLASS_MASTERS_PRICE_LIST[level] = Integer.parseInt(id);
				level++;
			}
		}
		CLASS_MASTERS_PRICE_ITEM = servicesSettings.getProperty("ClassMastersPriceItem", "0,0,0,0");
		if(CLASS_MASTERS_PRICE_ITEM.length() >= 7)
		{
			int level = 1;
			for(String id : CLASS_MASTERS_PRICE_ITEM.split(","))
			{
				CLASS_MASTERS_PRICE_ITEM_LIST[level] = Integer.parseInt(id);
				level++;
			}
		}

		SERVICES_CHANGE_NICK_ENABLED = servicesSettings.getProperty("NickChangeEnabled", false);
		SERVICES_CHANGE_NICK_PRICE = servicesSettings.getProperty("NickChangePrice", 100);
		SERVICES_CHANGE_NICK_ITEM = servicesSettings.getProperty("NickChangeItem", 4037);

		SERVICES_CHANGE_CLAN_NAME_ENABLED = servicesSettings.getProperty("ClanNameChangeEnabled", false);
		SERVICES_CHANGE_CLAN_NAME_PRICE = servicesSettings.getProperty("ClanNameChangePrice", 100);
		SERVICES_CHANGE_CLAN_NAME_ITEM = servicesSettings.getProperty("ClanNameChangeItem", 4037);

		SERVICES_CHANGE_PET_NAME_ENABLED = servicesSettings.getProperty("PetNameChangeEnabled", false);
		SERVICES_CHANGE_PET_NAME_PRICE = servicesSettings.getProperty("PetNameChangePrice", 100);
		SERVICES_CHANGE_PET_NAME_ITEM = servicesSettings.getProperty("PetNameChangeItem", 4037);

		SERVICES_EXCHANGE_BABY_PET_ENABLED = servicesSettings.getProperty("BabyPetExchangeEnabled", false);
		SERVICES_EXCHANGE_BABY_PET_PRICE = servicesSettings.getProperty("BabyPetExchangePrice", 100);
		SERVICES_EXCHANGE_BABY_PET_ITEM = servicesSettings.getProperty("BabyPetExchangeItem", 4037);

		SERVICES_CHANGE_SEX_ENABLED = servicesSettings.getProperty("SexChangeEnabled", false);
		SERVICES_CHANGE_SEX_PRICE = servicesSettings.getProperty("SexChangePrice", 100);
		SERVICES_CHANGE_SEX_ITEM = servicesSettings.getProperty("SexChangeItem", 4037);

		SERVICES_CHANGE_NICK_COLOR_ENABLED = servicesSettings.getProperty("NickColorChangeEnabled", false);
		SERVICES_CHANGE_NICK_COLOR_PRICE = servicesSettings.getProperty("NickColorChangePrice", 100);
		SERVICES_CHANGE_NICK_COLOR_ITEM = servicesSettings.getProperty("NickColorChangeItem", 4037);
		SERVICES_CHANGE_NICK_COLOR_LIST = servicesSettings.getProperty("NickColorChangeList", new String[]{"00FF00"});
		
		SERVICES_CHANGE_TITLE_COLOR_ENABLED = servicesSettings.getProperty("TitleColorChangeEnabled", false);
    SERVICES_CHANGE_TITLE_COLOR_PRICE = servicesSettings.getProperty("TitleColorChangePrice", 100);
    SERVICES_CHANGE_TITLE_COLOR_ITEM = servicesSettings.getProperty("TitleColorChangeItem", 4037);
    SERVICES_CHANGE_TITLE_COLOR_LIST = servicesSettings.getProperty("TitleColorChangeList", new String[] { "00FF00" });

		SERVICES_BASH_ENABLED = servicesSettings.getProperty("BashEnabled", false);
		SERVICES_BASH_SKIP_DOWNLOAD = servicesSettings.getProperty("BashSkipDownload", false);
		SERVICES_BASH_RELOAD_TIME = servicesSettings.getProperty("BashReloadTime", 24);

		SERVICES_RATE_TYPE = servicesSettings.getProperty("RateBonusType", Bonus.NO_BONUS);
		SERVICES_RATE_BONUS_PRICE = servicesSettings.getProperty("RateBonusPrice", new int[]{1500});
		SERVICES_RATE_BONUS_ITEM = servicesSettings.getProperty("RateBonusItem", new int[]{4037});
		SERVICES_RATE_BONUS_VALUE = servicesSettings.getProperty("RateBonusValue", new double[]{1.25});
		SERVICES_RATE_BONUS_DAYS = servicesSettings.getProperty("RateBonusTime", new int[]{30});

		SERVICES_NOBLESS_SELL_ENABLED = servicesSettings.getProperty("NoblessSellEnabled", false);
		SERVICES_NOBLESS_SELL_PRICE = servicesSettings.getProperty("NoblessSellPrice", 1000);
		SERVICES_NOBLESS_SELL_ITEM = servicesSettings.getProperty("NoblessSellItem", 4037);

		SERVICES_EXPAND_INVENTORY_ENABLED = servicesSettings.getProperty("ExpandInventoryEnabled", false);
		SERVICES_EXPAND_INVENTORY_PRICE = servicesSettings.getProperty("ExpandInventoryPrice", 1000);
		SERVICES_EXPAND_INVENTORY_ITEM = servicesSettings.getProperty("ExpandInventoryItem", 4037);
		SERVICES_EXPAND_INVENTORY_MAX = servicesSettings.getProperty("ExpandInventoryMax", 250);

		SERVICES_EXPAND_WAREHOUSE_ENABLED = servicesSettings.getProperty("ExpandWarehouseEnabled", false);
		SERVICES_EXPAND_WAREHOUSE_PRICE = servicesSettings.getProperty("ExpandWarehousePrice", 1000);
		SERVICES_EXPAND_WAREHOUSE_ITEM = servicesSettings.getProperty("ExpandWarehouseItem", 4037);

		SERVICES_EXPAND_CWH_ENABLED = servicesSettings.getProperty("ExpandCWHEnabled", false);
		SERVICES_EXPAND_CWH_PRICE = servicesSettings.getProperty("ExpandCWHPrice", 1000);
		SERVICES_EXPAND_CWH_ITEM = servicesSettings.getProperty("ExpandCWHItem", 4037);

		SERVICES_SELLPETS = servicesSettings.getProperty("SellPets", "");

		SERVICES_OFFLINE_TRADE_ALLOW = servicesSettings.getProperty("AllowOfflineTrade", false);
		SERVICES_OFFLINE_TRADE_ALLOW_OFFSHORE = servicesSettings.getProperty("AllowOfflineTradeOnlyOffshore", true);
		SERVICES_OFFLINE_TRADE_MIN_LEVEL = servicesSettings.getProperty("OfflineMinLevel", 0);
		SERVICES_OFFLINE_TRADE_NAME_COLOR = Integer.decode("0x" + servicesSettings.getProperty("OfflineTradeNameColor", "B0FFFF"));
		SERVICES_OFFLINE_TRADE_PRICE_ITEM = servicesSettings.getProperty("OfflineTradePriceItem", 0);
		SERVICES_OFFLINE_TRADE_PRICE = servicesSettings.getProperty("OfflineTradePrice", 0);
		SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK = servicesSettings.getProperty("OfflineTradeDaysToKick", 14) * 86400L;
		SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART = servicesSettings.getProperty("OfflineRestoreAfterRestart", true);

		SERVICES_NO_TRADE_ONLY_OFFLINE = servicesSettings.getProperty("NoTradeOnlyOffline", false);
		SERVICES_TRADE_TAX = servicesSettings.getProperty("TradeTax", 0.0);
		SERVICES_OFFSHORE_TRADE_TAX = servicesSettings.getProperty("OffshoreTradeTax", 0.0);
		SERVICES_TRADE_TAX_ONLY_OFFLINE = servicesSettings.getProperty("TradeTaxOnlyOffline", false);
		SERVICES_OFFSHORE_NO_CASTLE_TAX = servicesSettings.getProperty("NoCastleTaxInOffshore", false);
		SERVICES_TRADE_ONLY_FAR = servicesSettings.getProperty("TradeOnlyFar", false);
		SERVICES_TRADE_MIN_LEVEL = servicesSettings.getProperty("MinLevelForTrade", 0);
		SERVICES_TRADE_RADIUS = servicesSettings.getProperty("TradeRadius", 30);

		SERVICES_GIRAN_HARBOR_ENABLED = servicesSettings.getProperty("GiranHarborZone", false);
		SERVICES_PARNASSUS_ENABLED = servicesSettings.getProperty("ParnassusZone", false);
		SERVICES_PARNASSUS_NOTAX = servicesSettings.getProperty("ParnassusNoTax", false);
		SERVICES_PARNASSUS_PRICE = servicesSettings.getProperty("ParnassusPrice", 500000);

		SERVICES_ALLOW_LOTTERY = servicesSettings.getProperty("AllowLottery", false);
		SERVICES_LOTTERY_PRIZE = servicesSettings.getProperty("LotteryPrize", 50000);
		SERVICES_ALT_LOTTERY_PRICE = servicesSettings.getProperty("AltLotteryPrice", 2000);
		SERVICES_LOTTERY_TICKET_PRICE = servicesSettings.getProperty("LotteryTicketPrice", 2000);
		SERVICES_LOTTERY_5_NUMBER_RATE = servicesSettings.getProperty("Lottery5NumberRate", 0.6);
		SERVICES_LOTTERY_4_NUMBER_RATE = servicesSettings.getProperty("Lottery4NumberRate", 0.4);
		SERVICES_LOTTERY_3_NUMBER_RATE = servicesSettings.getProperty("Lottery3NumberRate", 0.2);
		SERVICES_LOTTERY_2_AND_1_NUMBER_PRIZE = servicesSettings.getProperty("Lottery2and1NumberPrize", 200);

		SERVICES_ALLOW_ROULETTE = servicesSettings.getProperty("AllowRoulette", false);
		SERVICES_ROULETTE_MIN_BET = servicesSettings.getProperty("RouletteMinBet", 1L);
		SERVICES_ROULETTE_MAX_BET = servicesSettings.getProperty("RouletteMaxBet", Long.MAX_VALUE);

		SERVICES_ENABLE_NO_CARRIER = servicesSettings.getProperty("EnableNoCarrier", false);
		SERVICES_NO_CARRIER_MIN_TIME = servicesSettings.getProperty("NoCarrierMinTime", 0);
		SERVICES_NO_CARRIER_MAX_TIME = servicesSettings.getProperty("NoCarrierMaxTime", 90);
		SERVICES_NO_CARRIER_DEFAULT_TIME = servicesSettings.getProperty("NoCarrierDefaultTime", 60);

		ITEM_BROKER_ITEM_SEARCH = servicesSettings.getProperty("UseItemBrokerItemSearch", false);

		ALLOW_EVENT_GATEKEEPER = servicesSettings.getProperty("AllowEventGatekeeper", false);

		ENABLE_ANGEL_CAT = servicesSettings.getProperty("EnableAngelCat", false);
	}

	public static void loadPvPSettings()
	{
		ExProperties pvpSettings = load(PVP_CONFIG_FILE);

		/* KARMA SYSTEM */
		KARMA_MIN_KARMA = pvpSettings.getProperty("MinKarma", 240);
		KARMA_SP_DIVIDER = pvpSettings.getProperty("SPDivider", 7);
		KARMA_LOST_BASE = pvpSettings.getProperty("BaseKarmaLost", 0);

		KARMA_DROP_GM = pvpSettings.getProperty("CanGMDropEquipment", false);
		KARMA_NEEDED_TO_DROP = pvpSettings.getProperty("KarmaNeededToDrop", true);
		DROP_ITEMS_ON_DIE = pvpSettings.getProperty("DropOnDie", false);
		DROP_ITEMS_AUGMENTED = pvpSettings.getProperty("DropAugmented", false);

		KARMA_DROP_ITEM_LIMIT = pvpSettings.getProperty("MaxItemsDroppable", 10);
		MIN_PK_TO_ITEMS_DROP = pvpSettings.getProperty("MinPKToDropItems", 5);

		KARMA_RANDOM_DROP_LOCATION_LIMIT = pvpSettings.getProperty("MaxDropThrowDistance", 70);

		KARMA_DROPCHANCE_BASE = pvpSettings.getProperty("ChanceOfPKDropBase", 20.);
		KARMA_DROPCHANCE_MOD = pvpSettings.getProperty("ChanceOfPKsDropMod", 1.);
		NORMAL_DROPCHANCE_BASE = pvpSettings.getProperty("ChanceOfNormalDropBase", 1.);
		DROPCHANCE_EQUIPPED_WEAPON = pvpSettings.getProperty("ChanceOfDropWeapon", 3);
		DROPCHANCE_EQUIPMENT = pvpSettings.getProperty("ChanceOfDropEquippment", 17);
		DROPCHANCE_ITEM = pvpSettings.getProperty("ChanceOfDropOther", 80);

		KARMA_LIST_NONDROPPABLE_ITEMS = new ArrayList<Integer>();
		for(int id : pvpSettings.getProperty("ListOfNonDroppableItems", new int[]{
			57,
			1147,
			425,
			1146,
			461,
			10,
			2368,
			7,
			6,
			2370,
			2369,
			3500,
			3501,
			3502,
			4422,
			4423,
			4424,
			2375,
			6648,
			6649,
			6650,
			6842,
			6834,
			6835,
			6836,
			6837,
			6838,
			6839,
			6840,
			5575,
			7694,
			6841,
			8181
		}))
		{
			KARMA_LIST_NONDROPPABLE_ITEMS.add(id);
		}

		PVP_TIME = pvpSettings.getProperty("PvPTime", 40000);
	}

	public static void loadAISettings()
	{
		ExProperties aiSettings = load(AI_CONFIG_FILE);

		AI_TASK_ATTACK_DELAY = aiSettings.getProperty("AiTaskDelay", 1000);
		AI_TASK_ACTIVE_DELAY = aiSettings.getProperty("AiTaskActiveDelay", 1000);
		BLOCK_ACTIVE_TASKS = aiSettings.getProperty("BlockActiveTasks", false);
		ALWAYS_TELEPORT_HOME = aiSettings.getProperty("AlwaysTeleportHome", false);

		RND_WALK = aiSettings.getProperty("RndWalk", true);
		RND_WALK_RATE = aiSettings.getProperty("RndWalkRate", 1);
		RND_ANIMATION_RATE = aiSettings.getProperty("RndAnimationRate", 2);

		AGGRO_CHECK_INTERVAL = aiSettings.getProperty("AggroCheckInterval", 250);
		NONAGGRO_TIME_ONTELEPORT = aiSettings.getProperty("NonAggroTimeOnTeleport", 15000);
		MAX_DRIFT_RANGE = aiSettings.getProperty("MaxDriftRange", 100);
		MAX_PURSUE_RANGE = aiSettings.getProperty("MaxPursueRange", 4000);
		MAX_PURSUE_UNDERGROUND_RANGE = aiSettings.getProperty("MaxPursueUndergoundRange", 2000);
		MAX_PURSUE_RANGE_RAID = aiSettings.getProperty("MaxPursueRangeRaid", 5000);
	}

	public static void loadGeodataSettings()
	{
		ExProperties geodataSettings = load(GEODATA_CONFIG_FILE);

		GEO_X_FIRST = geodataSettings.getProperty("GeoFirstX", 11);
		GEO_Y_FIRST = geodataSettings.getProperty("GeoFirstY", 10);
		GEO_X_LAST = geodataSettings.getProperty("GeoLastX", 28);
		GEO_Y_LAST = geodataSettings.getProperty("GeoLastY", 26);

		GEOFILES_PATTERN = geodataSettings.getProperty("GeoFilesPattern", "(\\d{2}_\\d{2})\\.l2j");
		ALLOW_GEODATA = geodataSettings.getProperty("AllowGeodata", true);
		ALLOW_FALL_FROM_WALLS = geodataSettings.getProperty("AllowFallFromWalls", false);
		ALLOW_KEYBOARD_MOVE = geodataSettings.getProperty("AllowMoveWithKeyboard", true);
		COMPACT_GEO = geodataSettings.getProperty("CompactGeoData", false);
		CLIENT_Z_SHIFT = geodataSettings.getProperty("ClientZShift", 16);
		PATHFIND_BOOST = geodataSettings.getProperty("PathFindBoost", 2);
		PATHFIND_DIAGONAL = geodataSettings.getProperty("PathFindDiagonal", true);
		PATH_CLEAN = geodataSettings.getProperty("PathClean", true);
		PATHFIND_MAX_Z_DIFF = geodataSettings.getProperty("PathFindMaxZDiff", 32);
		MAX_Z_DIFF = geodataSettings.getProperty("MaxZDiff", 64);
		MIN_LAYER_HEIGHT = geodataSettings.getProperty("MinLayerHeight", 64);
		PATHFIND_MAX_TIME = geodataSettings.getProperty("PathFindMaxTime", 10000000);
		PATHFIND_BUFFERS = geodataSettings.getProperty("PathFindBuffers", "8x96;8x128;8x160;8x192;4x224;4x256;4x288;2x320;2x384;2x352;1x512");
	}

	public static void loadOlympiadSettings()
	{
		ExProperties olympSettings = load(OLYMPIAD);

		ENABLE_OLYMPIAD = olympSettings.getProperty("EnableOlympiad", true);
		ENABLE_OLYMPIAD_SPECTATING = olympSettings.getProperty("EnableOlympiadSpectating", true);
		ALT_OLY_DAYS = olympSettings.getProperty("AltOlyDays", false);
		ALT_OLY_START_TIME = olympSettings.getProperty("AltOlyStartTime", 18);
		ALT_OLY_MIN = olympSettings.getProperty("AltOlyMin", 0);
		ALT_OLY_CPERIOD = olympSettings.getProperty("AltOlyCPeriod", 21600000);
		ALT_OLY_WPERIOD = olympSettings.getProperty("AltOlyWPeriod", 604800000);
		ALT_OLY_VPERIOD = olympSettings.getProperty("AltOlyVPeriod", 43200000);
		CLASS_GAME_MIN = olympSettings.getProperty("ClassGameMin", 5);
		NONCLASS_GAME_MIN = olympSettings.getProperty("NonClassGameMin", 9);
		TEAM_GAME_MIN = olympSettings.getProperty("TeamGameMin", 4);

		GAME_MAX_LIMIT = olympSettings.getProperty("GameMaxLimit", 50);
		GAME_CLASSES_COUNT_LIMIT = olympSettings.getProperty("GameClassesCountLimit", 20);
		GAME_NOCLASSES_COUNT_LIMIT = olympSettings.getProperty("GameNoClassesCountLimit", 40);

		ALT_OLY_REG_DISPLAY = olympSettings.getProperty("AltOlyRegistrationDisplayNumber", 100);
		ALT_OLY_BATTLE_REWARD_ITEM = olympSettings.getProperty("AltOlyBattleRewItem", 13722);
		ALT_OLY_CLASSED_RITEM_C = olympSettings.getProperty("AltOlyClassedRewItemCount", 50);
		ALT_OLY_NONCLASSED_RITEM_C = olympSettings.getProperty("AltOlyNonClassedRewItemCount", 40);
		ALT_OLY_TEAM_RITEM_C = olympSettings.getProperty("AltOlyTeamRewItemCount", 50);
		ALT_OLY_COMP_RITEM = olympSettings.getProperty("AltOlyCompRewItem", 13722);
		ALT_OLY_GP_PER_POINT = olympSettings.getProperty("AltOlyGPPerPoint", 1000);
		ALT_OLY_HERO_POINTS = olympSettings.getProperty("AltOlyHeroPoints", 180);
		ALT_OLY_RANK1_POINTS = olympSettings.getProperty("AltOlyRank1Points", 120);
		ALT_OLY_RANK2_POINTS = olympSettings.getProperty("AltOlyRank2Points", 80);
		ALT_OLY_RANK3_POINTS = olympSettings.getProperty("AltOlyRank3Points", 55);
		ALT_OLY_RANK4_POINTS = olympSettings.getProperty("AltOlyRank4Points", 35);
		ALT_OLY_RANK5_POINTS = olympSettings.getProperty("AltOlyRank5Points", 20);
		OLYMPIAD_STADIAS_COUNT = olympSettings.getProperty("OlympiadStadiasCount", 160);
		OLYMPIAD_BATTLES_FOR_REWARD = olympSettings.getProperty("OlympiadBattlesForReward", 15);
		OLYMPIAD_POINTS_DEFAULT = olympSettings.getProperty("OlympiadPointsDefault", 50);
		OLYMPIAD_POINTS_WEEKLY = olympSettings.getProperty("OlympiadPointsWeekly", 10);
		OLYMPIAD_OLDSTYLE_STAT = olympSettings.getProperty("OlympiadOldStyleStat", false);
	}

	private static final String SKILLS_CONFIG_FILE = "config/skills.properties";

	public static void loadSkillsConfig()
	{
		ExProperties skillsSettings = load(SKILLS_CONFIG_FILE);

		ALT_DISABLE_SPELLBOOKS = skillsSettings.getProperty("AltDisableSpellbooks", false);
		ALT_SAVE_UNSAVEABLE = skillsSettings.getProperty("AltSaveUnsaveable", false);
		ALT_SAVE_EFFECTS_REMAINING_TIME = skillsSettings.getProperty("AltSaveEffectsRemainingTime", 5);
		ALT_SHOW_REUSE_MSG = skillsSettings.getProperty("AltShowSkillReuseMessage", true);
		ALT_DELETE_SA_BUFFS = skillsSettings.getProperty("AltDeleteSABuffs", false);
		AUTO_LEARN_SKILLS = skillsSettings.getProperty("AutoLearnSkills", false);
		AUTO_LEARN_FORGOTTEN_SKILLS = skillsSettings.getProperty("AutoLearnForgottenSkills", false);
		ALT_BUFF_LIMIT = skillsSettings.getProperty("BuffLimit", 20);
		ALLOW_CLANSKILLS = skillsSettings.getProperty("AllowClanSkills", true);
		ALLOW_LEARN_TRANS_SKILLS_WO_QUEST = skillsSettings.getProperty("AllowLearnTransSkillsWOQuest", false);
		CLANHALL_BUFFTIME_MODIFIER = skillsSettings.getProperty("ClanHallBuffTimeModifier", 1.0);
		SONGDANCETIME_MODIFIER = skillsSettings.getProperty("SongDanceTimeModifier", 1.0);
		MAXLOAD_MODIFIER = skillsSettings.getProperty("MaxLoadModifier", 1.0);
		ENABLE_MODIFY_SKILL_DURATION = skillsSettings.getProperty("EnableModifySkillDuration", false);
		String[] propertySplit = skillsSettings.getProperty("SkillDurationList", "").split(";");
		SKILL_DURATION_LIST = new TIntIntHashMap(propertySplit.length);
		for(String skill : propertySplit)
		{
			String[] skillSplit = skill.split(",");
			if(skillSplit.length != 2)
			{
				_log.warn("[SkillDurationList]: invalid config property -> SkillDurationList \"" + skill + "\"");
			}
			else
			{
				try
				{
					SKILL_DURATION_LIST.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
				}
				catch(NumberFormatException nfe)
				{
					if(!skill.isEmpty())
					{
						_log.warn("[SkillDurationList]: invalid config property -> SkillList \"" + skillSplit[0] + "\"" + skillSplit[1]);
					}
				}
			}
		}
	}

	private static final String RATES_CONFIG_FILE = "config/rates.properties";

	public static void loadRatesConfig()
	{
		ExProperties ratesSettings = load(RATES_CONFIG_FILE);

		RATE_XP = ratesSettings.getProperty("RateXp", 1.);
		RATE_SP = ratesSettings.getProperty("RateSp", 1.);
		RATE_QUESTS_REWARD = ratesSettings.getProperty("RateQuestsReward", 1.);
		RATE_QUESTS_DROP = ratesSettings.getProperty("RateQuestsDrop", 1.);
		RATE_CLAN_REP_SCORE = ratesSettings.getProperty("RateClanRepScore", 1.);
		RATE_CLAN_REP_SCORE_MAX_AFFECTED = ratesSettings.getProperty("RateClanRepScoreMaxAffected", 2);
		RATE_DROP_ADENA = ratesSettings.getProperty("RateDropAdena", 1.);
		RATE_DROP_ITEMS = ratesSettings.getProperty("RateDropItems", 1.);
		RATE_DROP_COMMON_ITEMS = ratesSettings.getProperty("RateDropCommonItems", 1.);
		RATE_DROP_RAIDBOSS = ratesSettings.getProperty("RateRaidBoss", 1.);
		RATE_DROP_SPOIL = ratesSettings.getProperty("RateDropSpoil", 1.);
		NO_RATE_ITEMS = ratesSettings.getProperty("NoRateItemIds", new int[]{
			6660,
			6662,
			6661,
			6659,
			6656,
			6658,
			8191,
			6657,
			10170,
			10314,
			16025,
			16026
		});
		NO_RATE_EQUIPMENT = ratesSettings.getProperty("NoRateEquipment", true);
		NO_RATE_KEY_MATERIAL = ratesSettings.getProperty("NoRateKeyMaterial", true);
		NO_RATE_RECIPES = ratesSettings.getProperty("NoRateRecipes", true);
		RATE_DROP_SIEGE_GUARD = ratesSettings.getProperty("RateSiegeGuard", 1.);
		RATE_MANOR = ratesSettings.getProperty("RateManor", 1.);
		RATE_FISH_DROP_COUNT = ratesSettings.getProperty("RateFishDropCount", 1.);
		RATE_PARTY_MIN = ratesSettings.getProperty("RatePartyMin", false);
		RATE_HELLBOUND_CONFIDENCE = ratesSettings.getProperty("RateHellboundConfidence", 1.);

		RATE_MOB_SPAWN = ratesSettings.getProperty("RateMobSpawn", 1);
		RATE_MOB_SPAWN_MIN_LEVEL = ratesSettings.getProperty("RateMobMinLevel", 1);
		RATE_MOB_SPAWN_MAX_LEVEL = ratesSettings.getProperty("RateMobMaxLevel", 100);

		RATE_RAID_REGEN = ratesSettings.getProperty("RateRaidRegen", 1.);
		RATE_RAID_DEFENSE = ratesSettings.getProperty("RateRaidDefense", 1.);
		RATE_RAID_ATTACK = ratesSettings.getProperty("RateRaidAttack", 1.);
		RATE_EPIC_DEFENSE = ratesSettings.getProperty("RateEpicDefense", RATE_RAID_DEFENSE);
		RATE_EPIC_ATTACK = ratesSettings.getProperty("RateEpicAttack", RATE_RAID_ATTACK);
		RAID_MAX_LEVEL_DIFF = ratesSettings.getProperty("RaidMaxLevelDiff", 8);
		PARALIZE_ON_RAID_DIFF = ratesSettings.getProperty("ParalizeOnRaidLevelDiff", true);
	}

	public static void loadCommunityConfig()
	{
		ExProperties communitySettings = load(COMMUNITY_CONFIGURATION_FILE);

		COMMUNITYBOARD_ENABLED = communitySettings.getProperty("CommunityBoardEnable", true);
		if(COMMUNITYBOARD_ENABLED)
		{
			ALLOW_COMMUNITYBOARD_IN_COMBAT = communitySettings.getProperty("AllowInCombat", false);
			COMMUNITYBOARD_ABNORMAL_ENABLED = communitySettings.getProperty("AllowAbnormalState", false);
			BBS_DEFAULT = communitySettings.getProperty("BBSStartPage", "_bbshome");
            BBSHOME_PAGE_WESTEROS  = communitySettings.getProperty("BBSHPWesteros", false);
            COMMUNITYBOARD_SHOP_ENABLED = communitySettings.getProperty("CommunityShopEnable", false);
			COMMUNITYBOARD_SELL_ENABLED = communitySettings.getProperty("CommunitySellEnable", false);
			for(String name : communitySettings.getProperty("AllowMultisell", ArrayUtils.EMPTY_STRING_ARRAY))
			{
				COMMUNITYBOARD_MULTISELL_ALLOW.add(name);
			}
			COMMUNITYBOARD_BUFFER_ENABLED = communitySettings.getProperty("CommunityBufferEnable", false);
			COMMUNITYBOARD_BUFFER_PET_ENABLED = communitySettings.getProperty("CommunityBufferPetEnable", false);
			COMMUNITYBOARD_BUFFER_SAVE_ENABLED = communitySettings.getProperty("CommunityBufferSaveEnable", false);
			COMMUNITYBOARD_INSTANCE_ENABLED = communitySettings.getProperty("CommunityBufferInstancesEnable", false);
			COMMUNITYBOARD_EVENTS_ENABLED = communitySettings.getProperty("CommunityBufferEventsEnable", false);
			COMMUNITYBOARD_BUFF_TIME = communitySettings.getProperty("CommunityBuffTime", 20) * 60000;
			COMMUNITYBOARD_BUFF_PICE = communitySettings.getProperty("CommunityBuffPice", 5000);
			COMMUNITYBOARD_BUFF_SAVE_PICE = communitySettings.getProperty("CommunityBuffSavePice", 50000);
			for(int id : communitySettings.getProperty("AllowEffect", new int[]{
				1085,
				1048,
				1045
			}))
			{
				COMMUNITYBOARD_BUFF_ALLOW.add(Integer.valueOf(id));
			}
			for(int id : communitySettings.getProperty("MageScheme", new int[]{1085}))
			{
				COMMUNITI_LIST_MAGE_SUPPORT.add(Integer.valueOf(id));
			}
			for(int id : communitySettings.getProperty("FighterScheme", new int[]{1085}))
			{
				COMMUNITI_LIST_FIGHTER_SUPPORT.add(Integer.valueOf(id));
			}

			COMMUNITYBOARD_TELEPORT_ENABLED = communitySettings.getProperty("CommunityTeleportEnable", false);
			COMMUNITYBOARD_TELE_PICE = communitySettings.getProperty("CommunityTeleportPice", 10000);
			COMMUNITYBOARD_SAVE_TELE_PICE = communitySettings.getProperty("CommunitySaveTeleportPice", 50000);
			USE_BBS_BUFER_IS_COMBAT = communitySettings.getProperty("UseBBSBuferIsCombat", false);
			USE_BBS_BUFER_IS_EVENTS = communitySettings.getProperty("UseBBSBuferIsEvents", false);
			USE_BBS_TELEPORT_IS_COMBAT = communitySettings.getProperty("UseBBSTeleportIsCombat", false);
			USE_BBS_TELEPORT_IS_EVENTS = communitySettings.getProperty("UseBBSTeleportIsEvents", false);
			USE_BBS_PROF_IS_COMBAT = communitySettings.getProperty("UseBBSProfIsCombat", false);
			USE_BBS_PROF_IS_EVENTS = communitySettings.getProperty("UseBBSProfIsEvents", false);
			SAVE_BBS_TELEPORT_IS_EPIC = communitySettings.getProperty("SaveBBSTeleportIsEpic", false);
			SAVE_BBS_TELEPORT_IS_BZ = communitySettings.getProperty("SaveBBSTeleportIsBZ", false);
			BUFFER_SUMMON_ENABLE = communitySettings.getProperty("CommunityBufferSummon", false);

			ENCHANT_ENABLED = communitySettings.getProperty("Enchant_enabled", false);
			ENCHANTER_ITEM_ID = communitySettings.getProperty("CBEnchantItem", 4037);
			MAX_ENCHANT = communitySettings.getProperty("CBEnchantItem", 20);
			ENCHANT_LEVELS = communitySettings.getProperty("CBEnchantLvl", new int[]{1});
			ENCHANT_PRICE_WPN = communitySettings.getProperty("CBEnchantPriceWeapon", new int[]{1});
			ENCHANT_PRICE_ARM = communitySettings.getProperty("CBEnchantPriceArmor", new int[]{1});
			ENCHANT_ATTRIBUTE_LEVELS = communitySettings.getProperty("CBEnchantAtributeLvlWeapon", new int[]{1});
			ENCHANT_ATTRIBUTE_LEVELS_ARM = communitySettings.getProperty("CBEnchantAtributeLvlArmor", new int[]{1});
			ATTRIBUTE_PRICE_WPN = communitySettings.getProperty("CBEnchantAtributePriceWeapon", new int[]{1});
			ATTRIBUTE_PRICE_ARM = communitySettings.getProperty("CBEnchantAtributePriceArmor", new int[]{1});
			ENCHANT_ATT_PVP = communitySettings.getProperty("CBEnchantAtributePvP", false);
			BBS_STAT_UPDATE_TIME = communitySettings.getProperty("UpdateTime", 5);

			communityFolder = communitySettings.getProperty("communityFolder", "original");
		}
	}

	public static void loadAprilFoolsSettings()
	{
		ExProperties eventAprilFoolsSettings = load(EVENT_APRIL_FOOLS_CONFIG_FILE);

		EVENT_APIL_FOOLS_DROP_CHANCE = eventAprilFoolsSettings.getProperty("AprilFollsDropChance", 50.);
	}

	public static void loadCaptureTheFlagSettings()
	{
		ExProperties eventCaptureTheFlagSettings = load(EVENT_CAPTURE_THE_FLAG_CONFIG_FILE);

		EVENT_CAPTURE_THE_FLAG_REWARD_ITEM_ID = eventCaptureTheFlagSettings.getProperty("RewardItemId", 57);
		EVENT_CAPTURE_THE_FLAG_ITEM_COUNT = eventCaptureTheFlagSettings.getProperty("RewardItemCount", 5000.);
		EVENT_CAPTURE_THE_FLAG_TIME = eventCaptureTheFlagSettings.getProperty("TimeToStart", 3);
		EVENT_CAPTURE_THE_FLAG_ITEM_RATE = eventCaptureTheFlagSettings.getProperty("RewardItemRate", true);
		EVENT_CAPTURE_THE_FLAG_CHANCE_TO_START = eventCaptureTheFlagSettings.getProperty("ChanceToStart", 5);
	}

	public static void loadChangeOfHeartSettings()
	{
		ExProperties eventChangeOfHeartSettings = load(EVENT_CHANGE_OF_HEART_CONFIG_FILE);

		EVENT_CHANGE_OF_HEART_HEARTS_DROP_CHANCE = eventChangeOfHeartSettings.getProperty("HeartsDropCance", 5.);
	}

	public static void loadCofferOfShadowsSettings()
	{
		ExProperties eventCofferOfShadowsSettings = load(EVENT_COFFER_OF_SHADOWS_CONFIG_FILE);

		EVENT_COFFER_OF_SHADOWS_PRICE_RATE = eventCofferOfShadowsSettings.getProperty("PriceRate", 1.);
		EVENT_COFFER_OF_SHADOWS_REWARD_RATE = eventCofferOfShadowsSettings.getProperty("RewardRate", 1.);
	}

	public static void loadGlitteringMedalSettings()
	{
		ExProperties eventGlitteringMedalSettings = load(EVENT_GLITTERING_MEDAL_CONFIG_FILE);

		EVENT_GLITTERING_MEDAL_MEDAL_DROP_CHANCE = eventGlitteringMedalSettings.getProperty("MedalDropChance", 10.);
		EVENT_GLITTERING_MEDAL_GLITTERING_DROP_CHANCE = eventGlitteringMedalSettings.getProperty("GlitteringMedalChance", 0.1);
	}

	public static void loadL2DaySettings()
	{
		ExProperties eventL2DaySettings = load(EVENT_L2_DAY_CONFIG_FILE);

		EVENT_L2_DAY_LETTER_DROP_CHANCE = eventL2DaySettings.getProperty("LetterDropChance", 1.);
	}

	public static void loadLastHeroSettings()
	{
		ExProperties eventLastHeroSettings = load(EVENT_LAST_HERO_CONFIG_FILE);

		EVENT_LAST_HERO_ON_KILL_REWARD_ITEM_ID = eventLastHeroSettings.getProperty("OnKillRewardItemId", 57);
		EVENT_LAST_HERO_ON_KILL_REWARD_ITEM_COUNT = eventLastHeroSettings.getProperty("OnKillRewardItemCount", 5000.);
		EVENT_LAST_HERO_TIME_TO_START = eventLastHeroSettings.getProperty("TimeToStart", 3);
		EVENT_LAST_HERO_ON_KILL_ITEM_RATE = eventLastHeroSettings.getProperty("OnKillRewardItemRate", true);
		EVENT_LAST_HERO_CHANCE_TO_START = eventLastHeroSettings.getProperty("ChanceToStart", 5);
		EVENT_LAST_HERO_ON_FINISH_ITEM_COUNT = eventLastHeroSettings.getProperty("OnFinishRewardItemCount", 10000.);
		EVENT_LAST_HERO_ON_FINISH_ITEM_RATE = eventLastHeroSettings.getProperty("OnFinishRewardRate", true);
	}

	public static void loadMarch8Settings()
	{
		ExProperties eventMarch8Settings = load(EVENT_MARCH_8_CONFIG_FILE);

		EVENT_MARCH_8_DROP_CHANCE = eventMarch8Settings.getProperty("DropChance", 10.);
		EVENT_MARCH_8_PRICE_RATE = eventMarch8Settings.getProperty("PriceRate", 1.);
	}

	public static void loadMasterOfEnchaningSettings()
	{
		ExProperties eventMasterOfEnchaningSettings = load(EVENT_MASTER_OF_ENCHANING_CONFIG_FILE);

		EVENT_MASTER_OF_ENCHANING_ENCHANT_CHANCE = eventMasterOfEnchaningSettings.getProperty("EnchantChance", 66);
		EVENT_MASTER_OF_ENCHANING_MAX_ENCHANT_LEVEL = eventMasterOfEnchaningSettings.getProperty("MaxEnchantLevel", 28);
		EVENT_MASTER_OF_ENCHANING_SAFE_ENCHANT_LEVEL = eventMasterOfEnchaningSettings.getProperty("SafeEnchantLevel", 3);
	}

	public static void loadOtherEventsSettings()
	{
		ExProperties eventOtherEventsSettings = load(EVENT_OTHER_EVENTS_CONFIG_FILE);

		EVENT_OTHER_EVENTS_BOUNTY_HUNTERS_ENABLED = eventOtherEventsSettings.getProperty("BountyHuntersEnabled", true);
	}

	public static void loadSavingSnowmanSettings()
	{
		ExProperties eventSavingSnowmanSettings = load(EVENT_SAVING_SNOWMAN_CONFIG_FILE);

		EVENT_SAVING_SNOWMAN_LOTERY_PRICE = eventSavingSnowmanSettings.getProperty("LoteryPrice", 50000);
		EVENT_SAVING_SNOWMAN_SANTA_SPAWN_CHANCE = eventSavingSnowmanSettings.getProperty("SantaSpawnChance", 2);
	}

	public static void loadTheFallHarvestSettings()
	{
		ExProperties eventTheFallHarvestSettings = load(EVENT_THE_FALL_HARVEST_CONFIG_FILE);

		EVENT_THE_FALL_HARVEST_POLLEN_DROP_CHANCE = eventTheFallHarvestSettings.getProperty("PollenDropChance", 5.);
	}

	public static void loadTrickOfTransmutationSettings()
	{
		ExProperties eventTrickOfTransmutationSettings = load(EVENT_TRICK_OF_TRANSMUTATION_CONFIG_FILE);

		EVENT_TRICK_OF_TRANS_KEYS_DROP_CHANCE = eventTrickOfTransmutationSettings.getProperty("KeysDropChance", 10.);
	}

	public static void loadL2wtSetting()
	{
		// TODO не удалять. Это просто "метка"
		ExProperties l2wtSettings = load(L2WT_CONFIG_FILE);

		CHARACTER_CREATE_NOBLE = l2wtSettings.getProperty("CharacterCreateNoble", false);
		RANDOM_NPC_ENCHANT_EFFECT = l2wtSettings.getProperty("RandomNpcEnchantEffect", false);
	}

	public static boolean advancedIp;
	public static ArrayList<AdvancedIp> advancedIps = new ArrayList<>();

	public static void loadAdvancedIp()
	{
		ExProperties properties = load(ADVANCED_IP_CONFIG_FILE);
		advancedIp = properties.getProperty("advancedIp", false);
		if(advancedIp)
		{
			String NetMask;
			String ip;
			for(int i = 0; i < (properties.size() - 1) / 2; i++)
			{
				NetMask = properties.getProperty("NetMask" + (i + 1));
				ip = properties.getProperty("IPAdress" + (i + 1));
				for(String mask : NetMask.split(","))
				{
					AdvancedIp advip = new AdvancedIp();
					advip.setIpAdress(ip);
					advip.setIpMask(mask.split("/")[0]);
					advip.setBitMask(mask.split("/")[1]);
					advancedIps.add(advip);
				}
			}
		}
	}

	public static void load()
	{
		loadServerConfig();
		loadSkillsConfig();
		loadRatesConfig();
		loadTelnetConfig();
		loadResidenceConfig();
		loadOtherConfig();
		loadSpoilConfig();
		loadFormulasConfig();
		loadAltSettings();
		loadServicesSettings();
		loadPvPSettings();
		loadAISettings();
		loadGeodataSettings();
		loadOlympiadSettings();
		loadExtSettings();
		loadCommunityConfig();
		loadTopConfig();
		loadPaymentConfig();
		abuseLoad();
		loadGMAccess();
		// Load Events config's files
		loadAprilFoolsSettings();
		loadCaptureTheFlagSettings();
		loadChangeOfHeartSettings();
		loadCofferOfShadowsSettings();
		loadGlitteringMedalSettings();
		loadL2DaySettings();
		loadLastHeroSettings();
		loadMarch8Settings();
		loadMasterOfEnchaningSettings();
		loadOtherEventsSettings();
		loadSavingSnowmanSettings();
		loadTheFallHarvestSettings();
		loadTrickOfTransmutationSettings();
		loadAdvancedIp();
		_log.info("Server Configuration: Loaded");
	}

	private Config()
	{
	}

	public static void abuseLoad()
	{
		List<Pattern> tmp = new ArrayList<Pattern>();

		LineNumberReader lnr = null;
		try
		{
			String line;

			lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(ANUSEWORDS_CONFIG_FILE), "UTF-8"));

			while((line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, "\n\r");
				if(st.hasMoreTokens())
				{
					tmp.add(Pattern.compile(".*" + st.nextToken() + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
				}
			}

			ABUSEWORD_LIST = tmp.toArray(new Pattern[tmp.size()]);
			tmp.clear();
			_log.info("Abuse: Loaded " + ABUSEWORD_LIST.length + " abuse words.");
		}
		catch(IOException e1)
		{
			_log.warn("Error reading abuse: " + e1);
		}
		finally
		{
			try
			{
				if(lnr != null)
				{
					lnr.close();
				}
			}
			catch(Exception e2)
			{
				// nothing
			}
		}
	}

	public static void loadGMAccess()
	{
		gmlist.clear();
		loadGMAccess(new File(GM_PERSONAL_ACCESS_FILE));
		File dir = new File(GM_ACCESS_FILES_DIR);
		if(!dir.exists() || !dir.isDirectory())
		{
			_log.info("Dir " + dir.getAbsolutePath() + " not exists.");
			return;
		}
		for(File f : dir.listFiles())
		// hidden файлы НЕ игнорируем
		{
			if(!f.isDirectory() && f.getName().endsWith(".xml"))
			{
				loadGMAccess(f);
			}
		}
	}

	public static void loadGMAccess(File file)
	{
		try
		{
			Field fld;
			// File file = new File(filename);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(file);

			for(Node z = doc.getFirstChild(); z != null; z = z.getNextSibling())
			{
				for(Node n = z.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if(!n.getNodeName().equalsIgnoreCase("char"))
					{
						continue;
					}

					PlayerAccess pa = new PlayerAccess();
					for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						Class<?> cls = pa.getClass();
						String node = d.getNodeName();

						if(node.equalsIgnoreCase("#text"))
						{
							continue;
						}
						try
						{
							fld = cls.getField(node);
						}
						catch(NoSuchFieldException e)
						{
							_log.info("Not found desclarate ACCESS name: " + node + " in XML Player access Object");
							continue;
						}

						if(fld.getType().getName().equalsIgnoreCase("boolean"))
						{
							fld.setBoolean(pa, Boolean.parseBoolean(d.getAttributes().getNamedItem("set").getNodeValue()));
						}
						else if(fld.getType().getName().equalsIgnoreCase("int"))
						{
							fld.setInt(pa, Integer.valueOf(d.getAttributes().getNamedItem("set").getNodeValue()));
						}
					}
					gmlist.put(pa.PlayerID, pa);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getField(String fieldName)
	{
		Field field = FieldUtils.getField(Config.class, fieldName);

		if(field == null)
		{
			return null;
		}

		try
		{
			return String.valueOf(field.get(null));
		}
		catch(IllegalArgumentException e)
		{

		}
		catch(IllegalAccessException e)
		{

		}

		return null;
	}

	public static boolean setField(String fieldName, String value)
	{
		Field field = FieldUtils.getField(Config.class, fieldName);

		if(field == null)
		{
			return false;
		}

		try
		{
			if(field.getType() == boolean.class)
			{
				field.setBoolean(null, BooleanUtils.toBoolean(value));
			}
			else if(field.getType() == int.class)
			{
				field.setInt(null, NumberUtils.toInt(value));
			}
			else if(field.getType() == long.class)
			{
				field.setLong(null, NumberUtils.toLong(value));
			}
			else if(field.getType() == double.class)
			{
				field.setDouble(null, NumberUtils.toDouble(value));
			}
			else if(field.getType() == String.class)
			{
				field.set(null, value);
			}
			else
			{
				return false;
			}
		}
		catch(IllegalArgumentException e)
		{
			return false;
		}
		catch(IllegalAccessException e)
		{
			return false;
		}

		return true;
	}

	public static ExProperties load(String filename)
	{
		return load(new File(filename));
	}

	public static ExProperties load(File file)
	{
		ExProperties result = new ExProperties();

		try
		{
			result.load(file);
		}
		catch(IOException e)
		{
			_log.error("Error loading config : " + file.getName() + "!");
		}

		return result;
	}

	public static boolean containsAbuseWord(String s)
	{
		for(Pattern pattern : ABUSEWORD_LIST)
		{
			if(pattern.matcher(s).matches())
			{
				return true;
			}
		}
		return false;
	}
}