package instances;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ScheduledFuture;

import l2next.commons.collections.GArray;
import l2next.commons.threading.RunnableImpl;
import l2next.commons.util.Rnd;
import l2next.gameserver.ThreadPoolManager;
import l2next.gameserver.ai.CtrlEvent;
import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.*;
import l2next.gameserver.instancemanager.WorldStatisticsManager;
import l2next.gameserver.listener.actor.OnCurrentHpDamageListener;
import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.AggroList;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.GameObject;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Spawner;
import l2next.gameserver.model.World;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.DoorInstance;
import l2next.gameserver.model.instances.GuardInstance;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.worldstatistics.CategoryType;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.network.serverpackets.components.SceneMovie;
import l2next.gameserver.network.serverpackets.ExSendUIEvent;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2next.gameserver.network.serverpackets.ExStartScenePlayer;
import l2next.gameserver.skills.AbnormalEffect;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.PositionUtils;

/**
 * @author Iqman + GW
 */
public class Kartia extends Reflection
{	
	private ZoneListener ZoneListener = new ZoneListener();
	
	public static boolean _isPartyInstance;
	public static final int SSQ_CAMERA = 18830;
	private static final int SOLO_ROOM_DOOR = 16170002;
	private static final int SOLO_RAID_DOOR = 16170003;
	private static final int PARTY_ROOM_DOOR = 16170012;
	private static final int PARTY_RAID_DOOR = 16170003;	
	private static final Location SOLO_ENTRANCE = new Location(-108983, -10446, -11920);	
	private static final Location SOLO_UPSTAIR_TELEPORT = new Location(-110262, -10547, -11925);
	private static final Location PARTY_UPSTAIR_TELEPORT = new Location(-119830, -10547, -11925);	
	public static Map<String, Integer> SOLO85_MONSTERS = new HashMap<String, Integer>();
	public static Map<String, Integer> PARTY85_MONSTERS = new HashMap<String, Integer>();
	public static Map<String, Integer> SOLO90_MONSTERS = new HashMap<String, Integer>();
	public static Map<String, Integer> PARTY90_MONSTERS = new HashMap<String, Integer>();
	public static Map<String, Integer> SOLO95_MONSTERS = new HashMap<String, Integer>();
	public static Map<String, Integer> PARTY95_MONSTERS = new HashMap<String, Integer>();	


	public static List<NpcInstance> captivateds = new ArrayList<NpcInstance>();
	private DeathListener _deathListener = new DeathListener();
	public static NpcInstance kartiaAlthar = null;
	public static NpcInstance ssqCameraLight = null;
	public static NpcInstance ssqCameraZone = null;
	public static int currentWave = 0;
	public static int currentSubwave = 0;
	public static long waveSpawnTime;
	public static int killedSubwaves;
	public static int killedWaves = 0;
	public static int status = 0;
	public static Map<String, Integer> monsterSet;
	public static TIntIntMap monstersToKill = new TIntIntHashMap();
	public static List<NpcInstance> wave = new ArrayList<NpcInstance>();
	public static NpcInstance ruler = null;
	public static int savedCaptivateds = 0;
	public static List<NpcInstance> supports = new ArrayList<NpcInstance>();
	public static String excludedSupport;
	public static List<NpcInstance> followers = new ArrayList<NpcInstance>();
	private static ScheduledFuture<?> _healTask;
	private static ScheduledFuture<?> _supportTask;
	private static ScheduledFuture<?> _aggroCheckTask;
	private static ScheduledFuture<?> _waveMovementTask;
	private static ScheduledFuture<?> _altharCheckTask;
	private ZoneListener startZoneListener = new ZoneListener();
	public static NpcInstance warrior = null;
	public static NpcInstance archer = null;
	public static NpcInstance summoner = null;
	public static NpcInstance healer = null;
	public static NpcInstance knight = null;
	public static boolean poisonZoneEnabled = false;
	private static int _firstRoomSubwavesSize;
	private static Map<String, List<String>> _firstRoomWaveNames = null;
	private static Map<String, List<String>> _secondRoomWaveNames = null;
	private static Map<String, List<String>> _raidRoomWaveNames = null;	
	
	@Override
	protected void onCreate()
	{
		super.onCreate();

		getZone("[400061]").addListener(startZoneListener);
		getZone("[400062]").addListener(startZoneListener);
		getZone("[4600071]").addListener(startZoneListener);
		getZone("[4600072]").addListener(startZoneListener);

		SOLO85_MONSTERS.put("npc_adolph", 33608);
		SOLO85_MONSTERS.put("support_adolph", 33609);
		SOLO85_MONSTERS.put("npc_barton", 33610);
		SOLO85_MONSTERS.put("support_barton", 33611);
		SOLO85_MONSTERS.put("npc_hayuk", 33612);
		SOLO85_MONSTERS.put("support_hayuk", 33613);
		SOLO85_MONSTERS.put("npc_eliyah", 33614);
		SOLO85_MONSTERS.put("support_eliyah", 33615);
		SOLO85_MONSTERS.put("npc_elise", 33616);
		SOLO85_MONSTERS.put("support_elise", 33617);
		SOLO85_MONSTERS.put("support_eliyah_spirit", 33618);
		SOLO85_MONSTERS.put("support_troop", 33642);
		SOLO85_MONSTERS.put("captivated", 33641);
		SOLO85_MONSTERS.put("altar", 19247);

		SOLO85_MONSTERS.put("keeper", 19220);
		SOLO85_MONSTERS.put("watcher", 19221);
		SOLO85_MONSTERS.put("overseer", 19222);
		SOLO85_MONSTERS.put("ruler", 19253);

		PARTY85_MONSTERS = new HashMap<String, Integer>();

		PARTY85_MONSTERS.put("support_troop", 33642);
		PARTY85_MONSTERS.put("altar", 19248);
		PARTY85_MONSTERS.put("captivated", 33641);
		PARTY85_MONSTERS.put("altar", 19248);

		PARTY85_MONSTERS.put("keeper", 19223);
		PARTY85_MONSTERS.put("watcher", 19230);
		PARTY85_MONSTERS.put("overseer", 19225);
		PARTY85_MONSTERS.put("ruler", 25882);

		SOLO90_MONSTERS = new HashMap<String, Integer>();

		SOLO90_MONSTERS.put("npc_adolph", 33619);
		SOLO90_MONSTERS.put("support_adolph", 33620);
		SOLO90_MONSTERS.put("npc_barton", 33621);
		SOLO90_MONSTERS.put("support_barton", 33622);
		SOLO90_MONSTERS.put("npc_hayuk", 33623);
		SOLO90_MONSTERS.put("support_hayuk", 33624);
		SOLO90_MONSTERS.put("npc_eliyah", 33625);
		SOLO90_MONSTERS.put("support_eliyah", 33626);
		SOLO90_MONSTERS.put("npc_elise", 33627);
		SOLO90_MONSTERS.put("support_elise", 33628);
		SOLO90_MONSTERS.put("support_eliyah_spirit", 33629);
		SOLO90_MONSTERS.put("support_troop", 33644);
		SOLO90_MONSTERS.put("altar", 19249);
		SOLO90_MONSTERS.put("captivated", 33643);
		SOLO90_MONSTERS.put("altar", 19249);

		SOLO90_MONSTERS.put("keeper", 19226);
		SOLO90_MONSTERS.put("watcher", 19224);
		SOLO90_MONSTERS.put("overseer", 19228);
		SOLO90_MONSTERS.put("ruler", 19254);

		PARTY90_MONSTERS = new HashMap<String, Integer>();

		PARTY90_MONSTERS.put("support_troop", 33644);
		PARTY85_MONSTERS.put("altar", 19250);
		PARTY90_MONSTERS.put("captivated", 33643);
		PARTY90_MONSTERS.put("altar", 19250);

		PARTY90_MONSTERS.put("keeper", 19229);
		PARTY90_MONSTERS.put("watcher", 19233);
		PARTY90_MONSTERS.put("overseer", 19231);
		PARTY90_MONSTERS.put("ruler", 25883);

		SOLO95_MONSTERS = new HashMap<String, Integer>();

		SOLO95_MONSTERS.put("npc_adolph", 33630);
		SOLO95_MONSTERS.put("support_adolph", 33631);
		SOLO95_MONSTERS.put("npc_barton", 33632);
		SOLO95_MONSTERS.put("support_barton", 33633);
		SOLO95_MONSTERS.put("npc_hayuk", 33634);
		SOLO95_MONSTERS.put("support_hayuk", 33635);
		SOLO95_MONSTERS.put("npc_eliyah", 33636);
		SOLO95_MONSTERS.put("support_eliyah", 33637);
		SOLO95_MONSTERS.put("npc_elise", 33638);
		SOLO95_MONSTERS.put("support_elise", 33639);
		SOLO95_MONSTERS.put("support_eliyah_spirit", 33640);
		SOLO95_MONSTERS.put("support_troop", 33646);
		SOLO95_MONSTERS.put("altar", 19251);
		SOLO95_MONSTERS.put("captivated", 33645);
		SOLO95_MONSTERS.put("altar", 19251);

		SOLO95_MONSTERS.put("keeper", 19228);
		SOLO95_MONSTERS.put("watcher", 19227);
		SOLO95_MONSTERS.put("overseer", 19234);
		SOLO95_MONSTERS.put("ruler", 19255);

		PARTY95_MONSTERS = new HashMap<String, Integer>();

		PARTY95_MONSTERS.put("support_troop", 33646);
		PARTY95_MONSTERS.put("altar", 19252);
		PARTY95_MONSTERS.put("captivated", 33645);
		PARTY95_MONSTERS.put("altar", 19252);

		PARTY95_MONSTERS.put("keeper", 19235);
		PARTY95_MONSTERS.put("watcher", 19236);
		PARTY95_MONSTERS.put("overseer", 19237);
		PARTY95_MONSTERS.put("ruler", 25884);

		_firstRoomSubwavesSize = 0;
		_firstRoomWaveNames = new HashMap<String, List<String>>();
		String pf = getPrefix();
		List<String> waves = new ArrayList<String>();
		waves.add(pf + "wave1");
		_firstRoomWaveNames.put(pf + "wave1", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "wave2");
		_firstRoomWaveNames.put(pf + "wave2", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "wave3_part1");
		waves.add(pf + "wave3_part2");
		_firstRoomWaveNames.put(pf + "wave3", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "wave4_part1");
		waves.add(pf + "wave4_part2");
		_firstRoomWaveNames.put(pf + "wave4", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "wave5_part1");
		waves.add(pf + "wave5_part2");
		waves.add(pf + "wave5_part3");
		_firstRoomWaveNames.put(pf + "wave5", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "wave6_part1");
		waves.add(pf + "wave6_part2");
		waves.add(pf + "wave6_part3");
		_firstRoomWaveNames.put(pf + "wave6", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "wave7_part1");
		waves.add(pf + "wave7_part2");
		waves.add(pf + "wave7_part3");
		_firstRoomWaveNames.put(pf + "wave7", waves);

		for(List spawns : _firstRoomWaveNames.values()) 
			_firstRoomSubwavesSize += spawns.size();
		_secondRoomWaveNames = new HashMap<String, List<String>>();

		waves.add(pf + "wave_room");
		_secondRoomWaveNames.put(pf + "wave_room", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "wave_ending");
		_secondRoomWaveNames.put(pf + "wave_ending", waves);
		_raidRoomWaveNames = new HashMap<String, List<String>>();
		waves.add(pf + "rb1");
		_raidRoomWaveNames.put(pf + "wave1", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "rb2");
		_raidRoomWaveNames.put(pf + "wave2", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "rb3");
		_raidRoomWaveNames.put(pf + "wave3", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "rb4");
		_raidRoomWaveNames.put(pf + "wave4", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "rb5");
		_raidRoomWaveNames.put(pf + "wave5", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "rb6");
		_raidRoomWaveNames.put(pf + "wave6", waves);
		waves = new ArrayList<String>();
		waves.add(pf + "rb7");
		_raidRoomWaveNames.put(pf + "wave7", waves);		
		switch(getInstancedZoneId())
		{
			case 205:
				_isPartyInstance = false;
				monsterSet = SOLO85_MONSTERS;
				break;
			case 206:
				_isPartyInstance = false;
				monsterSet = SOLO90_MONSTERS;
				break;			
			case 207:
				_isPartyInstance = false;
				monsterSet = SOLO95_MONSTERS;
				break;	
			case 208:
				_isPartyInstance = true;
				monsterSet = PARTY85_MONSTERS;
				break;				
			case 209:
				_isPartyInstance = true;
				monsterSet = PARTY90_MONSTERS;
				break;				
			case 210:
				_isPartyInstance = true;
				monsterSet = PARTY95_MONSTERS;
				break;	
		}
		
		if(!_isPartyInstance)
		{
			spawnByGroup(pf + "support");
		}
		else
			startChallenge();
		//getZone("[isthina_epic]").addListener(_epicZoneListener); xz poka
	}
	
	private String getPrefix()
	{
		switch(getInstancedZoneId())
		{
			case 205:
				return "K85S_";
			case 206:
				return "K90S_";		
			case 207:
				return "K95S_";
			case 208:
				return "K85P_";	
			case 209:
				return "K90P_";
			case 210:
				return "K95P_";
		}
		return "";
	}
	
	public void deselectSupport(String support)
	{
		if(excludedSupport == null || excludedSupport.equals(""))
		{
			excludedSupport = support;
			for(Player player : getPlayers())
				player.teleToLocation(SOLO_ENTRANCE);
		}
		startChallenge();
	}
  
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
	}

	private void cleanup()
	{
		ssqCameraZone.setNpcState(3);
		ssqCameraZone.setNpcState(0);
		ssqCameraZone.deleteMe();
		if(_aggroCheckTask != null)
			_aggroCheckTask.cancel(true);	
		if(_waveMovementTask != null)
			_waveMovementTask.cancel(true);	
		if(_altharCheckTask != null)
			_altharCheckTask.cancel(true);	
		if(_healTask != null)
			_healTask.cancel(true);	
		if(_supportTask != null)
			_supportTask.cancel(true);				
	}
  
	private long getReuseTimer()
	{
		Calendar _instanceTime = Calendar.getInstance();

		Calendar currentTime = Calendar.getInstance();
		_instanceTime.set(11, 6);
		_instanceTime.set(12, 30);
		_instanceTime.set(13, 0);

		if(_instanceTime.compareTo(currentTime) < 0)
			_instanceTime.add(5, 1);
		return _instanceTime.getTimeInMillis();	
	}	
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			//if(status == 0)
			//{
			//	if(_isPartyInstance && zone.getName().equalsIgnoreCase("[400062]") || !_isPartyInstance && zone.getName().equalsIgnoreCase("[400061]") && cha.isPlayer())
			//		if(_isPartyInstance)
			//			cha.teleToLocation(PARTY_UPSTAIR_TELEPORT, cha.getReflection());
			//		else
			//			cha.teleToLocation(SOLO_UPSTAIR_TELEPORT, cha.getReflection());
			//}
			if(cha.isPlayer() && (_isPartyInstance && zone.getName().equalsIgnoreCase("[4600071]") || (!_isPartyInstance && zone.getName().equalsIgnoreCase("[4600072]"))))
				cha.getPlayer().addListener(_deathListener);
			if(ssqCameraZone != null)
				ssqCameraZone.setNpcState(2);
		}		

		@Override
		public void onZoneLeave(Zone zone, Creature cha) 
		{
			if(cha.isPlayer() && ((_isPartyInstance && zone.getName().equalsIgnoreCase("[4600071]")) || (!_isPartyInstance && zone.getName().equalsIgnoreCase("[4600072]"))))
				cha.getPlayer().removeListener(_deathListener);
		}
	}

	public static enum KartiaType
	{
		SOLO85(1, 1, 85, 89), 
		SOLO90(1, 1, 90, 94), 
		SOLO95(1, 1, 95, 99), 
		PARTY85(2, 7, 85, 89), 
		PARTY90(2, 7, 90, 94), 
		PARTY95(2, 7, 95, 99);

		private int _minPlayers;
		private int _maxPlayers;
		private int _minLevel;
		private int _maxLevel;

		private KartiaType(int minPlayers, int maxPlayers, int minLevel, int maxLevel) 
		{ 
			_minPlayers = minPlayers;
			_maxPlayers = maxPlayers;
			_minLevel = minLevel;
			_maxLevel = maxLevel;
		}

		public int getMinPlayers()
		{
			return 	_minPlayers;
		}

		public int getMaxPlayers()
		{
			return _maxPlayers;
		}

		public int getMinLevel()
		{
			return _minLevel;
		}

		public int getMaxLevel()
		{
			return _maxLevel;
		}

		public static KartiaType getTypeByTemplateId(int templateId)
		{
			switch (templateId)
			{
				case 205:
					return SOLO85;
				case 206:
					return SOLO90;
				case 207:
					return SOLO95;
				case 208:
					return PARTY85;
				case 209:
					return PARTY90;
				case 210:
					return PARTY95;
			}
			return null;
		}
	}
	
	private void invokeDeathListener()
	{
		for(NpcInstance npc : getNpcs())
			npc.addListener(_deathListener);
	}	

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc())
			{
				NpcInstance npc = (NpcInstance) self;
				onNpcDie(npc, killer);
			}	
			if(!self.isPlayer())
				return;
			boolean exit = true;
			if(_isPartyInstance)
				for(Player member : getPlayers())
					if(!member.isDead())
					{
						exit = false;
						break;
					}

			if(exit)
			{
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						clearReflection(1, true);
					}
				}
				, 15000L);
			}		
		}
	}	

	private void startChallenge()
	{

		if(_isPartyInstance)
		{
			kartiaAlthar = addSpawnWithoutRespawn(monsterSet.get("altar").intValue(), new Location(-119684, -10453, -11307, 0), 0);
			ssqCameraLight = addSpawnWithoutRespawn(18830, new Location(-119684, -10453, -11307, 0), 0);
			ssqCameraZone = addSpawnWithoutRespawn(18830, new Location(-119907, -10443, -11924, 0), 0);
		}
		else
		{
			kartiaAlthar = addSpawnWithoutRespawn(monsterSet.get("altar").intValue(), new Location(-110116, -10453, -11307, 0), 0);
			ssqCameraLight = addSpawnWithoutRespawn(18830, new Location(-110116, -10453, -11307, 0), 0);
			ssqCameraZone = addSpawnWithoutRespawn(18830, new Location(-110339, -10443, -11924, 0), 0);
		}

		ssqCameraZone.setNpcState(3);
		ssqCameraZone.setNpcState(0);

		ssqCameraLight.setNpcState(3);
		ssqCameraLight.setNpcState(0);

		kartiaAlthar.setRandomWalk(false);
		kartiaAlthar.setIsInvul(true);
		ssqCameraLight.setRandomWalk(false);
		ssqCameraZone.setRandomWalk(false);

		if(!_isPartyInstance)
		{
			knight = addSpawnWithoutRespawn(monsterSet.get("support_adolph").intValue(), new Location(SOLO_ENTRANCE.getX(), SOLO_ENTRANCE.getY(), SOLO_ENTRANCE.getZ(), 0), 0);
			followers.add(knight);

			if(!excludedSupport.equals("WARRIOR"))
			{
				warrior = addSpawnWithoutRespawn(monsterSet.get("support_barton").intValue(), new Location(SOLO_ENTRANCE.getX(), SOLO_ENTRANCE.getY(), SOLO_ENTRANCE.getZ(), 0), 0);
				followers.add(warrior);
			}

			if(!excludedSupport.equals("ARCHER"))
			{
				archer = addSpawnWithoutRespawn(monsterSet.get("support_hayuk").intValue(), new Location(SOLO_ENTRANCE.getX(), SOLO_ENTRANCE.getY(), SOLO_ENTRANCE.getZ(), 0), 0);
				followers.add(archer);
			}

			if(!excludedSupport.equals("SUMMONER"))
			{
				summoner = addSpawnWithoutRespawn(monsterSet.get("support_eliyah").intValue(), new Location(SOLO_ENTRANCE.getX(), SOLO_ENTRANCE.getY(), SOLO_ENTRANCE.getZ(), 0), 0);
				followers.add(summoner);

				for(byte i = 0; i < 3; i = (byte)(i + 1))
				{
					NpcInstance light = addSpawnWithoutRespawn(monsterSet.get("support_eliyah_spirit").intValue(), new Location(summoner.getX(), summoner.getY(), summoner.getZ(), 0), 0);
					followers.add(light);
				}
			}

			if(!excludedSupport.equals("HEALER"))
			{
				healer = addSpawnWithoutRespawn(monsterSet.get("support_elise").intValue(), new Location(SOLO_ENTRANCE.getX(), SOLO_ENTRANCE.getY(), SOLO_ENTRANCE.getZ(), 0), 0);
				followers.add(healer);

				_healTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new HealTask(), 2000L, 7000L);
			}

			_supportTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new KartiaSupportTask(), 2000L, 2000L);
		}

		for(DoorInstance door : getDoors())
			door.closeMe();
		
		String pf = getPrefix();
		spawnByGroup(pf + "captivated");
		
		for(NpcInstance npc : getNpcs())
		{
			if(npc.getNpcId() == 33641 || npc.getNpcId() == 33643 || npc.getNpcId() == 33645)
			{
				npc.setRunning();
				captivateds.add(npc);			
			}
		}
		
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				nextWave();
			}
		}
		, 10000L);

		_aggroCheckTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new MonsterAggroTask(), 5000L, 3000L);
		_waveMovementTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new MonsterMovementTask(), 5000L, 3000L);
		_altharCheckTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new AltharTask(), 5000L, 3000L);
	}	

	private boolean nextWave()
	{
		if(status > 2)
			return false;

		Map<String, List<String>> waves = null;
		if(status == 0)
			waves = _firstRoomWaveNames;
		else if(status == 1)
			waves = _secondRoomWaveNames;
		else if(status == 2)
			waves = _raidRoomWaveNames;
		if(waves == null || currentWave >= waves.size())
			return false;
		List subwaves = null;
		int i = 0;
		for(List<String> wave : waves.values())
		{
			if(i == currentWave)
			{
				if(currentSubwave >= wave.size())
				{
					currentSubwave = 0;
					currentWave += 1;
					i++;
				}
				else 
				{
					subwaves = wave;
					currentWave = i;
				}
			} 
			else i++;
		}

		if((subwaves == null) || (currentSubwave >= subwaves.size()))
			return false;
		String waveName = (String)subwaves.get(currentSubwave++);

		List<Spawner> spawnList = spawnByGroupList(waveName);
		invokeDeathListener();
		for(NpcInstance npc : getNpcs())
		{
			//TODO[A]: При дефолтном АИ нельзя прокастовать его из дефолта
			//Exception: RunnableImpl.run(): l2next.gameserver.ai.CharacterAI cannot be cast to l2next.gameserver.ai.DefaultAI
			//DefaultAI ai = (DefaultAI) npc.getAI();
			//if(ai != null)
				//ai.setMaxPursueRange(Integer.MAX_VALUE);
			npc.setRandomWalk(false);
		}	
//getCurrentNpcId
		if((currentSubwave - 1 == 0) && ((status == 0) || (status == 2)))
		{
			for(Player player : getPlayers())
				player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_S1, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, String.valueOf(currentWave + 1) + "-й "));
		}

		waveSpawnTime = 0L;
		wave.clear();
		monstersToKill.clear();
		for(Spawner spawn : spawnList)
		{
			int npcId = spawn.getCurrentNpcId();
			if(!monstersToKill.containsKey(npcId))
				monstersToKill.put(npcId, 0);
			monstersToKill.put(spawn.getCurrentNpcId(), monstersToKill.get(npcId) + 1);
			wave.add(spawn.getLastSpawn());
		}
		waveSpawnTime = System.currentTimeMillis();

		return true;
	}


	public void onNpcDie(NpcInstance npc, Creature killer)
	{
		long instanceTime;
		if(status <= 2 && monstersToKill != null && !monstersToKill.isEmpty())
		{
			boolean neededToProgress = false;
			for(int npcId : monstersToKill.keys()) 
			{ 
				if(npc.getNpcId() == npcId)
				{
					int newValue = monstersToKill.get(npcId) - 1;
					if(newValue <= 0)
						monstersToKill.remove(npcId);
					else
						monstersToKill.put(npcId, newValue);
					neededToProgress = true;
					break;
				}
			}

			if(neededToProgress)
			{
				boolean needNextWave = true;
				for(int killCount : monstersToKill.values()) 
				{ 
					if(killCount > 0)
					{
						needNextWave = false;
						break;
					}
				}

				if(needNextWave)
				{
					if(currentSubwave - 1 == 0)
						killedWaves += 1;
					killedSubwaves += 1;

					if((status == 0) && (killedWaves == _firstRoomWaveNames.size()) && (killedSubwaves == _firstRoomSubwavesSize - 1))
					{
						ssqCameraLight.setNpcState(1);
						ssqCameraZone.setNpcState(2);
					}
					else if((status == 0) && (killedSubwaves >= _firstRoomSubwavesSize))
					{
						status = 1;
						killedWaves = 0;
						currentWave = 0;
						currentSubwave = 0;
						if(_isPartyInstance)
							getDoor(16170012).openMe();
						else
							getDoor(16170002).openMe();
						ssqCameraZone.setNpcState(3);
						ssqCameraZone.setNpcState(0);

						saveCaptivateds();
					}
					else if((status == 1) && (killedWaves >= _secondRoomWaveNames.size() - 1))
						openRaidDoor();
					else if((status == 2) && (killedWaves >= _raidRoomWaveNames.size() - 1))
					{
						status = 3;
						freeRuler();
					}

					if(status < 3)
					{
						ThreadPoolManager.getInstance().schedule(new RunnableImpl()
						{
							@Override
							public void runImpl()
							{
								nextWave();
							}
						}
						, 10000L);
					}

				}

			}

		}
		else if((status == 3) && (npc.getNpcId() == ((Integer)monsterSet.get("ruler")).intValue()))
		{
			cleanup();
			clearReflection(5, true);

			if(!_isPartyInstance)
			{
				for(Player player : getPlayers())
				{
					switch (getInstancedZoneId())
					{
						case 205:
							player.addExpAndSp(486407696L, 3800614);
							break;
						case 206:
							player.addExpAndSp(672353854L, 5644776);
							break;
						case 207:
							player.addExpAndSp(972042801L, 8502614);
					}
				}

			}

			instanceTime = getReuseTimer();
			for(Player player : getPlayers())
				player.setInstanceReuse(getInstancedZoneId(), instanceTime);
		}
	}

	public void openRaidDoor()
	{
		status = 2;
		killedWaves = 0;
		currentWave = 0;
		currentSubwave = 0;
		if(_isPartyInstance)
			getDoor(16170013).openMe();
		else
			getDoor(16170003).openMe();
		if(_isPartyInstance)
			ruler = addSpawnWithoutRespawn(monsterSet.get("ruler").intValue(), new Location(-120864, -15872, -11400, 15596), 0);
		else
			ruler = addSpawnWithoutRespawn(monsterSet.get("ruler").intValue(), new Location(-111296, -15872, -11400, 15596), 0);

		ruler.setIsInvul(true);
		ruler.startAbnormalEffect(AbnormalEffect.FLESH_STONE);
		ruler.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, ruler);
		ruler.startParalyzed();		
		
		if(savedCaptivateds > 0)
		{
			for(int i = 0; i < savedCaptivateds; i++)
			{
				NpcInstance support;

				if(_isPartyInstance)
					support = addSpawnWithoutRespawn(monsterSet.get("support_troop").intValue(), new Location(-120901, -14562, -11424, 47595), 0);
				else
					support = addSpawnWithoutRespawn(((Integer)monsterSet.get("support_troop")).intValue(), new Location(-111333, -14562, -11424, 47595), 0);
				supports.add(support);
			}

			ThreadPoolManager.getInstance().scheduleAtFixedRate(new RaidSupportTask(), 1000L, 5000L);
		}

		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if(_isPartyInstance)
					getDoor(16170003).closeMe();
				else
					getDoor(16170003).closeMe();
			}
		}
		, 60000L);
	}	
	
	public void spawnHealingTree()
	{

		Skill buff = SkillTable.getInstance().getInfo(15003, 1);
		Skill heal = SkillTable.getInstance().getInfo(15002, 1);

		if(getPlayers().isEmpty())
			return;

		final Player player = getPlayers().get(0);

		Location loc = player.getLoc();

		final Creature tree = addSpawnWithoutRespawn(19256, new Location(loc.getX(), loc.getY(), loc.getZ(), loc.h), 0);
		tree.setTarget(player);
		tree.doCast(buff, player, true);
		tree.doCast(heal, player, true);

		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if((tree != null) && (!player.isDead()))
				{
					tree.setTarget(player);

					tree.doCast(SkillTable.getInstance().getInfo(15002, 1), player, true);

					ThreadPoolManager.getInstance().schedule(this, 10000L);
				}
			}
		}
		, 10000L);

		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if((tree != null) && (!player.isDead()))
				{
					tree.setTarget(player);

					tree.doCast(SkillTable.getInstance().getInfo(15003, 1), player, true);

					ThreadPoolManager.getInstance().schedule(this, 20000L);
				}
			}
		}
		, 20000L);
	}	

	public static synchronized void saveCaptivateds()
	{
		int delay = 0;
		for(final NpcInstance captivated : captivateds)
		{
			savedCaptivateds += 1;

			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					if(captivated != null)
					{
						DefaultAI ai = (DefaultAI) captivated.getAI();
											
						if(_isPartyInstance)
							ai.addTaskMove(Location.findPointToStay(new Location(-118391, -10454, -11924), 250, 250, captivated.getGeoIndex()), true);	
						else
							ai.addTaskMove(Location.findPointToStay(new Location(-108823, -10454, -11924), 250, 250, captivated.getGeoIndex()), true);	
					}
				}
			}
			, delay);

			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					captivated.deleteMe();
				}
			}
			, 10000 + delay);

			delay += 1000;
		}
		captivateds.clear();
	}	

	public static void freeRuler()
	{
		if(ruler != null)
		{
			ruler.stopAbnormalEffect(AbnormalEffect.FLESH_STONE);
			ruler.setIsInvul(false);
			ruler.stopParalyzed();
			wave.add(ruler);
		}
	}	


	public class KartiaSupportTask extends RunnableImpl
	{
		private int _lastPlyaerHeading;

		public KartiaSupportTask()
		{
			_lastPlyaerHeading = -1;
		}

		@Override
		public void runImpl()
		{
			if(getPlayers().isEmpty())
				return;

			boolean refollowAll = false;
			for(NpcInstance follower : followers)
			{
				boolean needFollow = true;

				if(((follower.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK) || (!follower.isAttackingNow())) && ((follower.getAI().getIntention() != CtrlIntention.AI_INTENTION_CAST) || (!follower.isCastingNow())) && (!follower.isMoving))
				{
					for(AggroList.HateInfo aggro : follower.getAggroList().getPlayableMap().values())
						if((aggro.attacker.isNpc()) || (aggro.attacker.isPlayer()))
							follower.getAggroList().remove(aggro.attacker, true);

					if((getPlayers().size() > 0) && (PositionUtils.calculateDistance(getPlayers().get(0), follower, true) > 600.0D))
						needFollow = true;
					else
					{
						for(NpcInstance npc : follower.getAroundNpc(600, 600))
						{
							if((npc.isMonster()) && (npc.getNpcId() != ((Integer)monsterSet.get("captivated")).intValue()) && (npc.getNpcId() != ((Integer)monsterSet.get("altar")).intValue()) && (!npc.isInvul()))
							{
								follower.setRunning();
								follower.getAggroList().addDamageHate(npc, 999, 999);
								follower.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, npc, 1000);
								needFollow = false;
								break;
							}
	
						}
						
						if(needFollow)
						{
							if((_lastPlyaerHeading < 0) || ((getPlayers().size() > 0) && (_lastPlyaerHeading != ((Player)getPlayers().get(0)).getLoc().h)) || (refollowAll))
							{
								needFollow = true;
								refollowAll = true;
							}
							else
								needFollow = false;
							_lastPlyaerHeading = (getPlayers().size() > 0 ? ((Player)getPlayers().get(0)).getLoc().h : 0);
						}
					}
				}	
				else
					needFollow = false;

				if(needFollow)
				{
					double angle = PositionUtils.convertHeadingToDegree(((Player)getPlayers().get(0)).getLoc().h);
					double radians = Math.toRadians(angle);
					double radius = 100.0D;
					double course = 160.0D;

					int x = (int)(Math.cos(3.141592653589793D + radians + course) * radius);
					int y = (int)(Math.sin(3.141592653589793D + radians + course) * radius);

					follower.setRunning();
					Location loc = ((Player)getPlayers().get(0)).getLoc();
					loc.setX(loc.getX() + x + Rnd.get(-100, 100));
					loc.setY(loc.getY() + y + Rnd.get(-100, 100));
					DefaultAI ai = (DefaultAI) follower.getAI();
					ai.addTaskMove(Location.findPointToStay(loc, 250, 250, follower.getGeoIndex()), true);
				}
			}
		}
	}	

	public class HealTask extends RunnableImpl
	{
		public HealTask()
		{
		}

		@Override
		public void runImpl()
		{
			if(getPlayers().isEmpty())
				return;

			final Player player = getPlayers().get(0);

			if(healer != null)
			{
				double percentHp = player.getCurrentHp() / player.getMaxHp();

				if(percentHp <= 0.5D)
				{
					healer.setTarget(player);

					switch(getInstancedZoneId())
					{
						case 205:
							healer.doCast(SkillTable.getInstance().getInfo(14899, 1), player, true);
							break;
						case 206:
							healer.doCast(SkillTable.getInstance().getInfo(14900, 1), player, true);
							break;
						case 207:
							healer.doCast(SkillTable.getInstance().getInfo(14901, 1), player, true);
					}

					boolean needTree = true;
					for(NpcInstance npc : getNpcs())
					{
						if(npc.getNpcId() == 19256)
						{
							needTree = false;
							break;
						}
					}

					if(needTree)
					{
						ThreadPoolManager.getInstance().schedule(new RunnableImpl()
						{
							@Override
							public void runImpl()
							{
								switch(getInstancedZoneId())
								{
									case 205:
										healer.doCast(SkillTable.getInstance().getInfo(14903, 1), player, true);
										break;
									case 206:
										healer.doCast(SkillTable.getInstance().getInfo(14904, 1), player, true);
										break;
									case 207:
										healer.doCast(SkillTable.getInstance().getInfo(14905, 1), player, true);
								}		

								ThreadPoolManager.getInstance().schedule(new RunnableImpl()
								{
									@Override
									public void runImpl()
									{
										spawnHealingTree();
									}
								}
								, 2000L);
							}
						}
						, 2000L);
					}

				}
				else if(percentHp <= 0.85D)
				{
					healer.setTarget(player);
					healer.doCast(SkillTable.getInstance().getInfo(14899, 1), player, true);
				}
			}
		}
	}	


	public static class RaidSupportTask extends RunnableImpl
	{

		public RaidSupportTask()
		{
		}

		@Override
		public void runImpl()
		{
			for(NpcInstance support : supports) 
			{
				if((!support.isAttackingNow()) && (!support.isCastingNow()))
				{
					for(NpcInstance monster : support.getAroundNpc(1200, 1200))
					{
						if((monster.isMonster()) && (!monster.isInvul()))
						{
							support.setRunning();
							support.getAggroList().addDamageHate(monster, 999, 999);
							support.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, monster, 1000);
							break;
						}
					}
				}
			}
		}
	}

	public class AltharTask extends RunnableImpl
	{
		private final Skill HP_ABSORBTION85 = SkillTable.getInstance().getInfo(14984, 1);
		private final Skill HP_ABSORBTION90 = SkillTable.getInstance().getInfo(14985, 1);
		private final Skill HP_ABSORBTION95 = SkillTable.getInstance().getInfo(14986, 1);

		public AltharTask()
		{
		}

		@Override
		public void runImpl()
		{
			Skill castSkill = null;
			switch (getInstancedZoneId())
			{
				case 205:
				case 208:
					castSkill = HP_ABSORBTION85;
					break;
				case 206:
				case 209:
					castSkill = HP_ABSORBTION90;
					break;
				case 207:
				case 210:
					castSkill = HP_ABSORBTION95;
			}

			if(castSkill == null)
				return;
			if((captivateds != null) && (captivateds.isEmpty()))
			{
				for(NpcInstance npc : wave)
				{
					if(!npc.isDead())
					{
						double distance = PositionUtils.calculateDistance(npc, kartiaAlthar, true);
						if((distance < 500.0D) && (npc.getZ() - kartiaAlthar.getZ() < 150))
						{
							onNpcDie(npc, kartiaAlthar);
							if(npc != null)
								npc.deleteMe();
							kartiaAlthar.setNpcState(1);
							if(captivateds.size() > 0)
							{
								final NpcInstance captivated = (NpcInstance)captivateds.get(Rnd.get(captivateds.size()));
								if(captivated != null)
								{
									kartiaAlthar.setTarget(captivated);
									kartiaAlthar.doCast(castSkill, kartiaAlthar, true);
									kartiaAlthar.setHeading(0);

									ThreadPoolManager.getInstance().schedule(new RunnableImpl()
									{
										@Override
										public void runImpl()
										{
											captivated.deleteMe();
											if(captivateds != null)
												captivateds.remove(captivated);
										}
									}
									, 10000L);
								}
							}
						}
					}
				}
			}
		}
	}

	public static class MonsterAggroTask extends RunnableImpl
	{

		public MonsterAggroTask()
		{
		}

		private void aggroCheck(NpcInstance npc)
		{
			if((npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_CAST) && (npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK))
			{
				if(!World.getAroundPlayers(npc).isEmpty())
				{
					GArray<GameObject> objects = World.getAroundObjects(npc, 450, 200);
					for(GameObject object : objects)
					{
						if(((object instanceof Player)) || ((object instanceof GuardInstance)))
						{
							npc.getAggroList().addDamageHate((Creature)object, 999, 999);
							npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, object, 1000);							
							break;
						}
					}
				}
			}
		}

		@Override
		public void runImpl()
		{
			if(status <= 2)
			{
				for(NpcInstance npc : wave)
					aggroCheck(npc);

				if((status == 3) && (ruler != null))
					aggroCheck(ruler);
			}
		}
	}

	public static class MonsterMovementTask extends RunnableImpl
	{
		public static final List<Location> SOLO_LEFT_KILLER_ROUTES = new ArrayList<Location>();
		public static final List<Location> SOLO_RIGHT_KILLER_ROUTES;
		public static final List<Location> PARTY_LEFT_KILLER_ROUTES;
		public static final List<Location> PARTY_RIGHT_KILLER_ROUTES;
		static
		{
			SOLO_LEFT_KILLER_ROUTES.add(new Location(-110440, -10472, -11926));
			SOLO_LEFT_KILLER_ROUTES.add(new Location(-110085, -10876, -11920));
			SOLO_LEFT_KILLER_ROUTES.add(new Location(-109182, -10791, -11920));
			SOLO_LEFT_KILLER_ROUTES.add(new Location(-109162, -10453, -11926));
			SOLO_LEFT_KILLER_ROUTES.add(new Location(-109933, -10451, -11688));

			SOLO_RIGHT_KILLER_ROUTES = new ArrayList<Location>();

			SOLO_RIGHT_KILLER_ROUTES.add(new Location(-110440, -10472, -11926));
			SOLO_RIGHT_KILLER_ROUTES.add(new Location(-110020, -9980, -11920));
			SOLO_RIGHT_KILLER_ROUTES.add(new Location(-109157, -10009, -11920));
			SOLO_RIGHT_KILLER_ROUTES.add(new Location(-109162, -10453, -11926));
			SOLO_RIGHT_KILLER_ROUTES.add(new Location(-109933, -10451, -11688));

			PARTY_LEFT_KILLER_ROUTES = new ArrayList<Location>();

			PARTY_LEFT_KILLER_ROUTES.add(new Location(-120008, -10472, -11926));
			PARTY_LEFT_KILLER_ROUTES.add(new Location(-119653, -10876, -11920));
			PARTY_LEFT_KILLER_ROUTES.add(new Location(-118750, -10791, -11920));
			PARTY_LEFT_KILLER_ROUTES.add(new Location(-118730, -10453, -11926));
			PARTY_LEFT_KILLER_ROUTES.add(new Location(-119501, -10451, -11688));

			PARTY_RIGHT_KILLER_ROUTES = new ArrayList<Location>();

			PARTY_RIGHT_KILLER_ROUTES.add(new Location(-120008, -10472, -11926));
			PARTY_RIGHT_KILLER_ROUTES.add(new Location(-119588, -9980, -11920));
			PARTY_RIGHT_KILLER_ROUTES.add(new Location(-118725, -10009, -11920));
			PARTY_RIGHT_KILLER_ROUTES.add(new Location(-118730, -10453, -11926));
			PARTY_RIGHT_KILLER_ROUTES.add(new Location(-119501, -10451, -11688));
		}

		public MonsterMovementTask()
		{
		}

		@Override
		public void runImpl()
		{
			if((waveSpawnTime == 0L) || ((System.currentTimeMillis() - waveSpawnTime) / 1000L < 15L))
				return;
			int waveSize = wave.size();
			int counter = 0;
			for(NpcInstance npc : wave)
			{
				counter++;

				if(npc.getAggroList().isEmpty() && (!npc.isMoving))
				{
					List<Location> routes;
					if(_isPartyInstance)
					{
						if(counter <= waveSize / 2)
							routes = PARTY_LEFT_KILLER_ROUTES;
						else
							routes = PARTY_RIGHT_KILLER_ROUTES;
					}
					else
					{
						if(counter <= waveSize / 2)
							routes = SOLO_LEFT_KILLER_ROUTES;
						else
							routes = SOLO_RIGHT_KILLER_ROUTES;
					}
					boolean takeNextRoute = false;
					Location nearestLoc = null;
					Double nearestLocDistance = null;
					Location npcLoc = npc.getLoc();
					for(Location loc : routes)
					{
						if(takeNextRoute)
						{
							nearestLoc = loc;
							break;
						}

						double distance = PositionUtils.calculateDistance(npcLoc.getX(), npcLoc.getY(), npcLoc.getZ(), loc.getX(), loc.getY(), loc.getZ(), true);
						if(distance < 150.0D)
							takeNextRoute = true;
						else if(nearestLoc == null)
						{
							nearestLoc = loc;
							nearestLocDistance = Double.valueOf(distance);
						}
						else
						{
							double currentLocDistance = PositionUtils.calculateDistance(npcLoc.getX(), npcLoc.getY(), npcLoc.getZ(), loc.getX(), loc.getY(), loc.getZ(), true);

							if(currentLocDistance <= nearestLocDistance.doubleValue())
							{
								nearestLoc = loc;
								nearestLocDistance = Double.valueOf(currentLocDistance);
							}
						}

					}

					if(nearestLoc != null)
					{
						nearestLoc.setX(nearestLoc.getX() + Rnd.get(30));
						nearestLoc.setY(nearestLoc.getY() + Rnd.get(30));
						npc.setRunning();
						DefaultAI ai = (DefaultAI) npc.getAI();
						ai.addTaskMove(Location.findPointToStay(nearestLoc, 250, 250, npc.getGeoIndex()), true);						
					}
				}
			}
		}
	}	

	public int getStatus()
	{
		return status;
	}
}