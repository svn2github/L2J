package l2god.scripts.instances;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import javolution.util.FastList;
import javolution.util.FastMap;
import l2god.gameserver.ThreadPoolManager;
import l2god.gameserver.instancemanager.InstanceManager;
import l2god.gameserver.instancemanager.InstanceManager.InstanceWorld;
import l2god.gameserver.model.actor.L2Character;
import l2god.gameserver.model.actor.L2Npc;
import l2god.gameserver.model.actor.controller.character.LocationController;
import l2god.gameserver.model.actor.instance.L2DoorInstance;
import l2god.gameserver.model.actor.instance.L2GuardInstance;
import l2god.gameserver.model.actor.instance.L2PcInstance;
import l2god.gameserver.model.items.base.proptypes.ProcessType;
import l2god.gameserver.model.player.formation.group.L2CommandChannel;
import l2god.gameserver.model.player.formation.group.L2Party;
import l2god.gameserver.model.world.Instance;
import l2god.gameserver.model.world.InstanceZoneId;
import l2god.gameserver.model.world.npc.spawn.L2Spawn;
import l2god.gameserver.model.world.quest.Quest;
import l2god.gameserver.model.world.zone.L2ZoneType;
import l2god.gameserver.model.world.zone.Location;
import l2god.gameserver.network.game.components.NpcStringId;
import l2god.gameserver.network.game.components.SystemMessageId;
import l2god.gameserver.network.game.serverpackets.EarthQuake;
import l2god.gameserver.network.game.serverpackets.SystemMessage;
import l2god.gameserver.network.game.serverpackets.packet.ex.ExSendUIEvent;
import l2god.gameserver.network.game.serverpackets.packet.show.ExShowScreenMessage;
import l2god.gameserver.util.Util;
import l2god.gameserver.util.arrays.L2FastList;

public class ShillienAltar extends Quest
{
  private static ShillienAltar _instance;
  private static final String qn = "ShillienAltar";
  private static final int INSTANCE_ID = InstanceZoneId.ALTAR_OF_SHILEN_2.getId();

  private static final Location FIRST_FLOOR = new Location(179400, 13683, -7396);
  private static final Location SECOND_FLOOR = new Location(179357, 13664, -9828);
  private static final Location THIRD_FLOOR = new Location(179354, 12922, -12776);
  private static final Location MELISSA_SPAWN0 = new Location(178432, 14848, -13688);
  private static final Location ISADORA_SPAWN = new Location(177833, 14852, -13688);
  private static final Location MELISSA_SPAWN = new Location(178146, 14356, -13688);
  private static final int ELCARDIA0 = 33474;
  private static final int ELCARDIA1 = 33475;
  private static final int ELCARDIA2 = 33476;
  private static final int FRIKIOS = 33299;
  private static final int EXECUTOR_CAPTAIN = 23131;
  private static final int CORRUPTED_CAPTAIN = 25857;
  private static final int SHILLIEN_BLADER = 23138;
  private static final int CORRUPTED_HIGH_PRIEST = 25858;
  private static final int SHILLIEN_ENCHANTER = 23132;
  private static final int SHILLIEN_PROTECTOR = 23134;
  private static final int SHILLIEN_WARRIOR = 23135;
  private static final int ISADORA = 25856;
  private static final int MELISSA0 = 25855;
  private static final int MELISSA = 25876;
  private static final int RITUAL_ALTAR0 = 19121;
  private static final int RITUAL_ALTAR1 = 19122;
  private static final int SHILLIEN_ALTAR = 19123;
  private static final int SEAL_OF_SENCERITY = 17745;
  private static final int VICTIMS_DEFEATED = 2518001;
  private static final int ALL_VICTIMS_DEFEATED = 2518002;
  private static final int VICTIMS_REMAINING = 2518003;
  private static final int NEED_CLOSE_ALTAR = 2518004;
  private static final int ALTAR_CLOSED = 2518005;
  private static final int ALTAR_ACTIVATED = 2518006;
  private static final int ATTACK_ALTAR = 2518007;
  private static final int ALTAR_RUNNING = 2518008;
  private static final int FIRST_FLOOR_TIME = 150;
  private static final int SECOND_FLOOR_TIME = 120;
  private static final int ALTAR_TIME = 60;
  private static final int[] DOORS = { 25180001, 25180002, 25180003, 25180004, 25180005, 25180006, 25180007 };

  private long getReuseTime()
  {
    Calendar monday = Calendar.getInstance();
    Calendar wednesday = Calendar.getInstance();
    Calendar friday = Calendar.getInstance();

    Calendar currentTime = Calendar.getInstance();

    monday.set(7, 2);
    monday.set(11, 6);
    monday.set(12, 30);
    monday.set(13, 0);

    if (monday.compareTo(currentTime) < 0)
    {
      monday.add(5, 7);
    }

    wednesday.set(7, 4);
    wednesday.set(11, 6);
    wednesday.set(12, 30);
    wednesday.set(13, 0);

    if (wednesday.compareTo(currentTime) < 0)
    {
      wednesday.add(5, 7);
    }

    friday.set(7, 6);
    friday.set(11, 6);
    friday.set(12, 30);
    friday.set(13, 0);

    if (friday.compareTo(currentTime) < 0)
    {
      friday.add(5, 7);
    }

    return wednesday.compareTo(friday) < 0 ? wednesday.getTimeInMillis() : monday.compareTo(wednesday) < 0 ? monday.getTimeInMillis() : friday.getTimeInMillis();
  }

  public boolean enterInstance(L2PcInstance player)
  {
    InstanceManager.InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

    if (!player.isGM()) {
      return false;
    }
    world = null;

    if (world != null)
    {
      if (!(world instanceof ShillienAltarWorld))
      {
        player.sendPacket(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER);
        return false;
      }

      if (!((ShillienAltarWorld)world).playersInside.contains(player))
      {
        ((ShillienAltarWorld)world).playersInside.add(player);
        world.allowed.add(Integer.valueOf(player.getObjectId()));
      }

      if (world.status < ShillienAltarState.FIRST_VICTIMS_SAVED.ordinal())
        player.teleToInstance(FIRST_FLOOR, world.instanceId);
      else if (world.status < ShillienAltarState.SECOND_VICTIMS_SAVED.ordinal())
        player.teleToInstance(SECOND_FLOOR, world.instanceId);
      else {
        player.teleToInstance(THIRD_FLOOR, world.instanceId);
      }
      return true;
    }

    world = new ShillienAltarWorld();
    int instanceTemplateId = INSTANCE_ID;
    if (!checkConditions(player, instanceTemplateId))
    {
      return false;
    }

    int instanceId = InstanceManager.getInstance().createDynamicInstance("ShillienAltar.xml");

    world.instanceId = instanceId;
    world.templateId = instanceTemplateId;
    world.status = 0;

    InstanceManager.getInstance().addWorld(world);

    if ((player.isGM()) && (player.getParty() == null))
    {
      player.teleToInstance(FIRST_FLOOR, instanceId);
      world.allowed.add(Integer.valueOf(player.getObjectId()));
      ((ShillienAltarWorld)world).playersInside.add(player);
      init((ShillienAltarWorld)world);
      return true;
    }

    if (player.getParty() != null)
    {
      for (L2PcInstance partyMember : player.getParty().getMembers())
      {
        partyMember.teleToInstance(FIRST_FLOOR, instanceId);
        world.allowed.add(Integer.valueOf(partyMember.getObjectId()));
        ((ShillienAltarWorld)world).playersInside.add(partyMember);
      }
      init((ShillienAltarWorld)world);
      return true;
    }
    return false;
  }

  private void init(ShillienAltarWorld world)
  {
    Instance instance = InstanceManager.getInstance().getInstance(world.instanceId);

    if (instance != null)
    {
      for (L2DoorInstance door : instance.getDoors())
      {
        door.closeMe();
      }

      for (L2Spawn spawn : instance.getGroupSpawn("monsters"))
      {
        L2Npc npc = spawn.spawnOne(false);
        if (npc.getNpcId() == 19123)
        {
          world.altar = npc;
          world.altarMaxHp = npc.getMaxHp();
          npc.setIsMortal(false);
        }
      }

      for (L2Spawn spawn : instance.getGroupSpawn("first_floor_victims"))
      {
        world.firstFloorVictims.add(spawn.spawnOne(false));
      }

      for (L2Spawn spawn : instance.getGroupSpawn("second_floor_victims"))
      {
        world.secondFloorVictims.add(spawn.spawnOne(false));
      }

      world.status = ShillienAltarState.TELEPORTED.ordinal();
    }
  }

  private boolean checkConditions(L2PcInstance player, int instanceTemplateId)
  {
    L2Party party = player.getParty();

    if (player.isGM())
    {
      Long reEnterTime = Long.valueOf(InstanceManager.getInstance().getInstanceTime(player.getObjectId(), instanceTemplateId));
      if (System.currentTimeMillis() < reEnterTime.longValue())
      {
        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_MAY_NOT_REENTER_YET).addPcName(player));
        return false;
      }
      return true;
    }

    if (player.getParty() == null)
    {
      player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
      return false;
    }

    int minPlayers = 7;
    int maxPlayers = 7;
    int minLevel = 95;

    if (party.getLeader() != player)
    {
      party.getCommandChannel().broadcastMessage(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
      return false;
    }
    if (party.getMemberCount() > maxPlayers)
    {
      player.sendPacket(SystemMessageId.PARTY_EXCEEDED_THE_LIMIT_CANT_ENTER);
      return false;
    }
    if (party.getMemberCount() < minPlayers)
    {
      party.getCommandChannel().broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_PEOPLE_TO_ENTER_INSTANCE_ZONE_NEED_S1).addNumber(minPlayers));
      return false;
    }

    for (L2PcInstance member : party.getMembers())
    {
      if ((member == null) || (member.getLevel() < minLevel) || (!member.isAwakened()))
      {
        party.getCommandChannel().broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_LEVEL_REQUIREMENT_NOT_SUFFICIENT).addPcName(member));
        return false;
      }
      if (!Util.checkIfInRange(1000, player, member, true))
      {
        party.getCommandChannel().broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_IN_LOCATION_THAT_CANNOT_BE_ENTERED).addPcName(member));
        return false;
      }
      Long reEnterTime = Long.valueOf(InstanceManager.getInstance().getInstanceTime(member.getObjectId(), instanceTemplateId));
      if (System.currentTimeMillis() < reEnterTime.longValue())
      {
        party.getCommandChannel().broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_MAY_NOT_REENTER_YET).addPcName(member));
        return false;
      }
    }
    return true;
  }

  public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
  {
    ShillienAltarWorld world = (ShillienAltarWorld)InstanceManager.getInstance().getInstanceWorld(npc, ShillienAltarWorld.class);

    if (world == null) {
      return null;
    }
    world.altarCurrentDamage += damage;

    return null;
  }

  public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
  {
    ShillienAltarWorld world = (ShillienAltarWorld)InstanceManager.getInstance().getInstanceWorld(npc, ShillienAltarWorld.class);

    if (world != null)
    {
      final Instance instance = InstanceManager.getInstance().getInstance(world.instanceId);

      if (instance == null) {
        return null;
      }
      int npcId = npc.getNpcId();
      if (!world.killedMonsters.containsKey(Integer.valueOf(npcId)))
        world.killedMonsters.put(Integer.valueOf(npcId), Integer.valueOf(1));
      else {
        world.killedMonsters.put(Integer.valueOf(npcId), Integer.valueOf(((Integer)world.killedMonsters.get(Integer.valueOf(npcId))).intValue() + 1));
      }

      if (world.status == ShillienAltarState.FIRST_FLOOR_ACTIVE.ordinal())
      {
        if ((world.killedMonsters.containsKey(Integer.valueOf(23131))) && (((Integer)world.killedMonsters.get(Integer.valueOf(23131))).intValue() >= 3) && (world.killedMonsters.containsKey(Integer.valueOf(25857))) && (((Integer)world.killedMonsters.get(Integer.valueOf(25857))).intValue() >= 1))
        {
          world.status = ShillienAltarState.FIRST_VICTIMS_SAVED.ordinal();
          onStatusChanged(world);
        }

      }
      else if (world.status == ShillienAltarState.SECOND_FLOOR_ACTIVE.ordinal())
      {
        if ((world.killedMonsters.containsKey(Integer.valueOf(23138))) && (((Integer)world.killedMonsters.get(Integer.valueOf(23138))).intValue() >= 3) && (world.killedMonsters.containsKey(Integer.valueOf(25858))) && (((Integer)world.killedMonsters.get(Integer.valueOf(25858))).intValue() >= 1))
        {
          world.status = ShillienAltarState.SECOND_VICTIMS_SAVED.ordinal();
          onStatusChanged(world);
        }

      }
      else if (world.status == ShillienAltarState.THIRD_FLOOR_ACTIVE.ordinal())
      {
        switch (npc.getNpcId())
        {
        case 23132:
          instance.getDoor(DOORS[3]).openMe();
          ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
          {
            public void run()
            {
              instance.getDoor(ShillienAltar.DOORS[3]).closeMe();
            }
          }
          , 5000L);

          break;
        case 23134:
          instance.getDoor(DOORS[4]).openMe();
          ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
          {
            public void run()
            {
              instance.getDoor(ShillienAltar.DOORS[4]).closeMe();
            }
          }
          , 5000L);

          break;
        case 23135:
          instance.getDoor(DOORS[5]).openMe();
          ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
          {
            public void run()
            {
              instance.getDoor(ShillienAltar.DOORS[5]).closeMe();
            }
          }
          , 5000L);
        case 23133:
        }

      }
      else if (world.status == ShillienAltarState.SHILLIEN_ALTAR_ACTIVE.ordinal())
      {
        if ((world.killedMonsters.containsKey(Integer.valueOf(25855))) && (((Integer)world.killedMonsters.get(Integer.valueOf(25855))).intValue() >= world.melissaCount) && (world.killedMonsters.containsKey(Integer.valueOf(25856))) && (((Integer)world.killedMonsters.get(Integer.valueOf(25856))).intValue() >= world.isadoraCount))
        {
          if ((world.killedMonsters.containsKey(Integer.valueOf(25876))) && (((Integer)world.killedMonsters.get(Integer.valueOf(25876))).intValue() >= 1))
          {
            world.status = ShillienAltarState.ALTAR_DESTROYED.ordinal();
            onStatusChanged(world);
          }
          else if (world.melissa == null)
          {
            world.melissa = addSpawn(25876, MELISSA_SPAWN.getX(), MELISSA_SPAWN.getY(), MELISSA_SPAWN.getZ(), 0, false, 0L, false, world.instanceId);
          }
        }
      }
    }

    return null;
  }

  private void onStatusChanged(final ShillienAltarWorld world)
  {
    final Instance instance = InstanceManager.getInstance().getInstance(world.instanceId);

    ShillienAltarState state = ShillienAltarState.values()[world.status];
    switch (5.$SwitchMap$l2god$scripts$instances$ShillienAltar$ShillienAltarState[state.ordinal()])
    {
    case 1:
      break;
    case 2:
      if (world.timer != null)
      {
        world.timer.cancel(true);
      }

      for (L2PcInstance player : world.playersInside)
      {
        player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(2518004), 5, 3000, new String[0]));
      }

      world.timer = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new VictimDefeatTask(5, 150, world), 0L, 1000L);
      break;
    case 3:
      if (world.timer != null)
      {
        world.timer.cancel(true);
        world.timer = null; } break;
    case 4:
      if (world.timer != null)
      {
        world.timer.cancel(true);
      }

      for (L2PcInstance player : world.playersInside)
      {
        player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(2518004), 5, 3000, new String[0]));
      }

      world.timer = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new VictimDefeatTask(5, 120, world), 0L, 1000L);
      break;
    case 5:
      if (world.timer != null)
      {
        world.timer.cancel(true);
        world.timer = null; } break;
    case 6:
      break;
    case 7:
      ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
      {
        public void run()
        {
          world.timer = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable()
          {
            private int _time = 60;

            public void run()
            {
              if (_time <= 0) {
                return;
              }
              double percent = val$world.altarCurrentDamage / val$world.altarMaxHp;
              int progress = (int)Math.min(6000.0D, percent * 6000.0D);
              progress -= progress % 60;

              boolean defeated = false;
              if (percent >= 0.95D)
              {
                defeated = true;
                for (L2PcInstance player : val$world.playersInside)
                {
                  player.sendPacket(new ExSendUIEvent(player, 5, _time--, progress, 122520, 2518008, null));
                  player.sendPacket(new ExSendUIEvent(player, 1, 0, 0, 0, 2518008, null));
                  player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(2518005), 5, 3000, new String[0]));
                }
              }
              else
              {
                for (L2PcInstance player : val$world.playersInside)
                {
                  if (_time == 60)
                  {
                    player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(2518006), 5, 3000, new String[0]));
                  }

                  player.sendPacket(new ExSendUIEvent(player, 5, _time--, progress, 122520, 2518008, null));
                }

              }

              if ((!defeated) && (_time <= 0))
              {
                for (L2PcInstance player : val$world.playersInside)
                {
                  player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(2518007), 5, 3000, new String[0]));
                }

                if (val$instance != null)
                {
                  boolean isMelissaAlive = false;
                  boolean isIsadoraAlive = false;
                  for (L2Npc npc : val$instance.getNpcs())
                  {
                    if ((npc.getNpcId() == 25855) && (!npc.isDead()))
                    {
                      isMelissaAlive = true;
                    }
                    else if ((npc.getNpcId() == 25856) && (!npc.isDead()))
                    {
                      isIsadoraAlive = true;
                    }
                  }

                  if (!isMelissaAlive)
                  {
                    addSpawn(25855, ShillienAltar.MELISSA_SPAWN0.getX(), ShillienAltar.MELISSA_SPAWN0.getY(), ShillienAltar.MELISSA_SPAWN0.getZ(), 0, true, 0L, false, val$world.instanceId);
                    val$world.melissaCount += 1;
                  }
                  if (!isIsadoraAlive)
                  {
                    addSpawn(25856, ShillienAltar.ISADORA_SPAWN.getX(), ShillienAltar.ISADORA_SPAWN.getY(), ShillienAltar.ISADORA_SPAWN.getZ(), 0, true, 0L, false, val$world.instanceId);
                    val$world.isadoraCount += 1;
                  }
                  val$world.altarMisses += 1;
                }
              }

              if (defeated)
              {
                _time = -1;
              }

              if (_time <= 0)
              {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                  public void run()
                  {
                    val$world.altarCurrentDamage = 0;
                    if (val$world.altar != null)
                      val$world.altar.setCurrentHp(val$world.altar.getMaxHp());
                    _time = 60;
                  }
                }
                , 15000L);
              }
            }
          }
          , 0L, 1000L);
        }
      }
      , 5000L);

      break;
    case 8:
      for (L2Spawn spawn : instance.getGroupSpawn("adventure_guildsman"))
      {
        spawn.spawnOne(false);
      }

      if (world.timer != null)
      {
        world.timer.cancel(true);
        world.timer = null;
      }

      for (L2PcInstance player : world.playersInside)
      {
        player.addItem(ProcessType.QUEST, 17745, 20L, null, true);
      }

      for (Iterator i$ = instance.getPlayers().iterator(); i$.hasNext(); ) { int playerId = ((Integer)i$.next()).intValue();
        InstanceManager.getInstance().setInstanceTime(playerId, world.templateId, getReuseTime());
      }
      instance.setDuration(300000);
    }
  }

  public String onSpawn(L2Npc npc)
  {
    if ((npc.getNpcId() == 19123) && ((npc instanceof L2GuardInstance)))
    {
      ((L2GuardInstance)npc).setCanAttackPlayer(false);
    }

    return null;
  }

  public synchronized String onEnterZone(L2Character character, L2ZoneType zone)
  {
    if ((character instanceof L2PcInstance));
    return super.onEnterZone(character, zone);
  }

  public String onExitZone(L2Character character, L2ZoneType zone)
  {
    if ((character instanceof L2PcInstance));
    return super.onExitZone(character, zone);
  }

  public String onFirstTalk(L2Npc npc, L2PcInstance player)
  {
    ShillienAltarWorld world = (ShillienAltarWorld)InstanceManager.getInstance().getInstanceWorld(npc, ShillienAltarWorld.class);

    if (world == null) {
      return null;
    }
    switch (npc.getNpcId())
    {
    case 19121:
      if (world.status >= ShillienAltarState.FIRST_VICTIMS_SAVED.ordinal())
      {
        return "embryo_altar_dummy01002.htm";
      }
      return "embryo_altar_dummy01001.htm";
    case 19122:
      if (world.status >= ShillienAltarState.SECOND_VICTIMS_SAVED.ordinal())
      {
        return "embryo_altar_dummy02002.htm";
      }
      return "embryo_altar_dummy02001.htm";
    case 19123:
      if (world.status >= ShillienAltarState.ALTAR_DESTROYED.ordinal())
      {
        return "embryo_altar_dummy03002.htm";
      }
      return "embryo_altar_dummy03001.htm";
    }
    return null;
  }

  public String onAsk(L2PcInstance player, L2Npc npc, int ask, int reply)
  {
    ShillienAltarWorld world = (ShillienAltarWorld)InstanceManager.getInstance().getInstanceWorld(npc, ShillienAltarWorld.class);

    if (world == null) {
      return null;
    }
    Instance instance = InstanceManager.getInstance().getInstance(world.instanceId);

    if (instance == null) {
      return null;
    }
    int npcId = npc.getNpcId();

    switch (npcId)
    {
    case 33474:
      if ((world.status == ShillienAltarState.TELEPORTED.ordinal()) && (reply == 1))
      {
        world.status = ShillienAltarState.FIRST_FLOOR_ACTIVE.ordinal();

        if (instance.getDoor(DOORS[0]) != null) {
          instance.getDoor(DOORS[0]).openMe();
        }
        onStatusChanged(world);
        return "seal_of_silen_elcardia1002.htm";
      }

      return "seal_of_silen_elcardia1003.htm";
    case 33475:
      if ((world.status == ShillienAltarState.FIRST_VICTIMS_SAVED.ordinal()) && (reply == 1))
      {
        world.status = ShillienAltarState.SECOND_FLOOR_ACTIVE.ordinal();

        if (instance.getDoor(DOORS[1]) != null) {
          instance.getDoor(DOORS[1]).openMe();
        }
        onStatusChanged(world);

        return "seal_of_silen_elcardia1002.htm";
      }

      return "seal_of_silen_elcardia1003.htm";
    case 33476:
      if ((world.status == ShillienAltarState.SECOND_VICTIMS_SAVED.ordinal()) && (reply == 1))
      {
        world.status = ShillienAltarState.THIRD_FLOOR_ACTIVE.ordinal();

        if (instance.getDoor(DOORS[2]) != null) {
          instance.getDoor(DOORS[2]).openMe();
        }
        onStatusChanged(world);

        return "seal_of_silen_elcardia1002.htm";
      }

      return "seal_of_silen_elcardia1003.htm";
    case 19121:
      if ((world.status == ShillienAltarState.FIRST_VICTIMS_SAVED.ordinal()) && (reply == 1))
      {
        player.teleToInstance(SECOND_FLOOR, world.instanceId); } break;
    case 19122:
      if ((world.status == ShillienAltarState.SECOND_VICTIMS_SAVED.ordinal()) && (reply == 1))
      {
        player.teleToInstance(THIRD_FLOOR, world.instanceId); } break;
    case 33299:
      if ((world.status == ShillienAltarState.THIRD_FLOOR_ACTIVE.ordinal()) && (reply == 5))
      {
        if (!instance.getDoor(DOORS[6]).isOpened()) {
          instance.getDoor(DOORS[6]).openMe();
        }
        world.status = ShillienAltarState.SHILLIEN_ALTAR_ACTIVE.ordinal();
        onStatusChanged(world);
        npc.getLocationController().delete();
      }

      break;
    }

    return null;
  }

  public ShillienAltar()
  {
    addSpawnId(19123);
    addKillId(new int[] { 23131, 25857 });
    addKillId(new int[] { 23138, 25858 });
    addKillId(new int[] { 23132, 23134, 23135 });
    addKillId(new int[] { 25855, 25876, 25856 });
    addAskId(33474, 10347);
    addAskId(33475, 10347);
    addAskId(33476, 10347);
    addFirstTalkId(new int[] { 19121, 19122 });
    addAskId(19121, 11);
    addAskId(19122, 11);
    addAskId(33299, 10349);
    addAttackId(19123);
  }

  public static void main(String[] args)
  {
    _instance = new ShillienAltar();
  }

  public static ShillienAltar getInstance()
  {
    return _instance;
  }

  public static class VictimDefeatTask
    implements Runnable
  {
    private ShillienAltar.ShillienAltarWorld _world;
    private int _victims;
    private int _initialTime;
    private int _time;

    VictimDefeatTask(int victims, int time, ShillienAltar.ShillienAltarWorld world)
    {
      _world = world;
      _victims = victims;
      _initialTime = time;
      _time = time;
    }

    public void run()
    {
      for (L2PcInstance player : _world.playersInside)
      {
        player.sendPacket(new ExSendUIEvent(player, 4, 0, _time-- * 60, 0, 2518003, new String[] { String.valueOf(_victims) }));
      }

      if (_time <= 0)
      {
        _time = _initialTime;
        _victims -= 1;

        for (L2PcInstance player : _world.playersInside)
        {
          player.sendPacket(new EarthQuake(player.getX(), player.getY(), player.getZ(), 5, 5));
          player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(2518001), 5, 3000, new String[] { String.valueOf(_victims) }));
        }

        if ((_world.status == ShillienAltar.ShillienAltarState.FIRST_FLOOR_ACTIVE.ordinal()) && (!_world.firstFloorVictims.isEmpty()))
        {
          ((L2Npc)_world.firstFloorVictims.get(0)).getLocationController().delete();
          _world.firstFloorVictims.remove(0);
        }
        else if ((_world.status == ShillienAltar.ShillienAltarState.SECOND_FLOOR_ACTIVE.ordinal()) && (!_world.secondFloorVictims.isEmpty()))
        {
          ((L2Npc)_world.firstFloorVictims.get(0)).getLocationController().delete();
          _world.secondFloorVictims.remove(0);
        }

        if (_victims <= 0)
        {
          ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
          {
            public void run()
            {
              for (L2PcInstance player : _world.playersInside)
              {
                player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(2518002), 5, 3000, new String[0]));
              }

              if (_world.timer != null)
              {
                _world.timer.cancel(true);
                _world.timer = null;
              }

              InstanceManager.getInstance().destroyInstance(_world.instanceId);
            }
          }
          , 3000L);
        }
      }
    }
  }

  public class ShillienAltarWorld extends InstanceManager.InstanceWorld
  {
    public List<L2PcInstance> playersInside = new FastList();
    public Map<Integer, Integer> killedMonsters = new FastMap();
    public List<L2Npc> firstFloorVictims = new FastList();
    public List<L2Npc> secondFloorVictims = new FastList();
    public Future<?> timer;
    public int altarMisses = 0;
    public int isadoraCount = 1;
    public int melissaCount = 1;
    public L2Npc melissa;
    public L2Npc altar;
    public int altarMaxHp;
    public int altarCurrentDamage;

    public ShillienAltarWorld()
    {
    }
  }

  private static enum ShillienAltarState
  {
    TELEPORTED, 
    FIRST_FLOOR_ACTIVE, 
    FIRST_VICTIMS_SAVED, 
    SECOND_FLOOR_ACTIVE, 
    SECOND_VICTIMS_SAVED, 
    THIRD_FLOOR_ACTIVE, 
    SHILLIEN_ALTAR_ACTIVE, 
    ALTAR_DESTROYED;
  }
}