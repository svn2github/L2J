package l2god.scripts.instances;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import javolution.util.FastList;
import javolution.util.FastMap;
import l2god.config.main.ConfigGrandBoss;
import l2god.gameserver.ThreadPoolManager;
import l2god.gameserver.instancemanager.InstanceManager;
import l2god.gameserver.instancemanager.InstanceManager.InstanceWorld;
import l2god.gameserver.model.actor.L2Character;
import l2god.gameserver.model.actor.L2Npc;
import l2god.gameserver.model.actor.instance.L2DoorInstance;
import l2god.gameserver.model.actor.instance.L2PcInstance;
import l2god.gameserver.model.player.ChatType;
import l2god.gameserver.model.player.formation.group.L2CommandChannel;
import l2god.gameserver.model.player.formation.group.L2Party;
import l2god.gameserver.model.world.Instance;
import l2god.gameserver.model.world.InstanceZoneId;
import l2god.gameserver.model.world.npc.spawn.L2Spawn;
import l2god.gameserver.model.world.quest.Quest;
import l2god.gameserver.model.world.zone.L2ZoneType;
import l2god.gameserver.model.world.zone.Location;
import l2god.gameserver.network.game.components.SystemMessageId;
import l2god.gameserver.network.game.serverpackets.NS;
import l2god.gameserver.network.game.serverpackets.SystemMessage;
import l2god.gameserver.util.Rnd;
import l2god.gameserver.util.Util;
import org.apache.log4j.Logger;

public class Fortuna extends Quest
{
  private static Fortuna _fortunaInstance;
  private static final String qn = "Fortuna";
  private static final int SELFINA = 33588;
  private static final Location ENTRANCE = new Location(42108, -172709, -7955);
  private static final Location LAIR_ENTRANCE = new Location(42096, -174439, -7950);
  private static final Location SELFINA_SPAWN = new Location(42101, -175276, -7944, 16200);
  private static final int FOR_ANCIENT_HEROES = 8888011;
  private static final int DROP_SPHERE_AND_SHE_WILL_NOT_SHINE = 8888012;
  private static final int THOSE_WHO_COMES_FOR_CURSED_BODIES = 8888003;
  private static final int BASE_CHALLENGE_DELAY = 120000;
  private static final Map<String, Integer> _scheduledSpawns = new FastMap();
  private static final int ENTRANCE_DOOR = 21120001;

  private long getReuseTime()
  {
    Calendar instanceTime = Calendar.getInstance();

    instanceTime.add(5, 1);
    instanceTime.set(11, 6);
    instanceTime.set(12, 30);
    instanceTime.set(13, 0);

    return instanceTime.getTimeInMillis();
  }

  public EnterInstanceResult reEnterInstance(L2PcInstance player)
  {
    InstanceManager.InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

    if (world != null)
    {
      if (!(world instanceof FortunaWorld))
      {
        player.sendPacket(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER);
        return EnterInstanceResult.ANOTHER_INSTANCE;
      }

      if (!((FortunaWorld)world).playersInside.contains(player))
      {
        ((FortunaWorld)world).playersInside.add(player);
        world.allowed.add(Integer.valueOf(player.getObjectId()));
      }

      player.teleToInstance(LAIR_ENTRANCE, world.instanceId);
      return EnterInstanceResult.OK;
    }
    return EnterInstanceResult.CANNOT_REENTER;
  }

  protected EnterInstanceResult enterInstance(L2PcInstance player, String template)
  {
    InstanceManager.InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

    if (world != null)
    {
      if (!(world instanceof FortunaWorld))
      {
        player.sendPacket(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER);
        return EnterInstanceResult.ANOTHER_INSTANCE;
      }
    }

    world = new FortunaWorld();

    int instanceTemplateId = InstanceZoneId.FORTUNA.getId();
    EnterInstanceResult reason = checkConditions(player, instanceTemplateId);
    if (reason != EnterInstanceResult.OK) {
      return reason;
    }
    int instanceId = InstanceManager.getInstance().createDynamicInstance(template);

    world.instanceId = instanceId;
    world.templateId = instanceTemplateId;
    world.status = 0;

    InstanceManager.getInstance().addWorld(world);
    init((FortunaWorld)world);

    if ((player.isGM()) && (player.getParty() == null))
    {
      player.teleToInstance(ENTRANCE, instanceId);
      world.allowed.add(Integer.valueOf(player.getObjectId()));
      ((FortunaWorld)world).playersInside.add(player);
      return EnterInstanceResult.OK;
    }

    if (player.getParty() != null)
    {
      for (L2PcInstance member : player.getParty().getMembers())
      {
        member.teleToInstance(ENTRANCE, instanceId);
        world.allowed.add(Integer.valueOf(member.getObjectId()));
        ((FortunaWorld)world).playersInside.add(member);
      }
      return EnterInstanceResult.OK;
    }
    return EnterInstanceResult.NO_PARTY;
  }

  public final EnterInstanceResult enterInstance(L2PcInstance player)
  {
    return enterInstance(player, "Fortuna.xml");
  }

  private void init(final FortunaWorld world)
  {
    ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
    {
      public void run()
      {
        Instance instance = InstanceManager.getInstance().getInstance(world.instanceId);
        if (instance != null)
        {
          final L2DoorInstance door = instance.getDoor(21120001);
          door.openMe();

          ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
          {
            public void run()
            {
              door.closeMe();

              final L2Npc selfina = addSpawn(33588, Fortuna.SELFINA_SPAWN.getX(), Fortuna.SELFINA_SPAWN.getY(), Fortuna.SELFINA_SPAWN.getZ(), Fortuna.SELFINA_SPAWN.getHeading(), false, 10000L, false, val$world.instanceId);
              selfina.broadcastPacket(new NS(selfina.getObjectId(), ChatType.NPC_ALL, selfina.getNpcId(), 8888011));

              ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
              {
                public void run()
                {
                  selfina.broadcastPacket(new NS(selfina.getObjectId(), ChatType.NPC_ALL, selfina.getNpcId(), 8888012));
                  Fortuna.this.spawnLightnings(val$world);

                  ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                  {
                    public void run()
                    {
                      Fortuna.this.startChallenge(val$world);
                    }
                  }
                  , 2000L);
                }
              }
              , 5000L);
            }
          }
          , 30000L);
        }
      }
    }
    , 10000L);
  }

  private void spawnLightnings(FortunaWorld world)
  {
    Instance instance = InstanceManager.getInstance().getInstance(world.instanceId);
    if (instance != null)
    {
      for (L2Spawn spawn : instance.getGroupSpawn("lightning_sphere_1"))
      {
        L2Npc sphere = spawn.spawnOne(false);
        sphere.setIsNoRndWalk(true);
      }
    }
  }

  private void startChallenge(final FortunaWorld world)
  {
    int delay = (world.playersInside.size() > 0) && (((L2PcInstance)world.playersInside.get(0)).isGM()) ? 10000 : 110000;
    ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
    {
      public void run()
      {
        final Instance instance = InstanceManager.getInstance().getInstance(world.instanceId);

        if (instance == null) {
          return;
        }
        for (final String groupName : Fortuna._scheduledSpawns.keySet())
        {
          world.spawnTasks.add(ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
          {
            public void run()
            {
              List spawns = instance.getGroupSpawn(groupName);

              if (spawns == null)
              {
                Fortuna._log.warn("Fortuna instance: cannot spawn monsters from group spawn [" + groupName + "]");
                return;
              }

              int counter = 0;
              for (final L2Spawn spawn : spawns)
              {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                  public void run()
                  {
                    L2Npc npc = spawn.spawnOne(false);
                    npc.setIsNoRndWalk(true);
                    npc.setIsRunning(true);
                  }
                }
                , counter);

                counter += Rnd.get(650, 1500);
              }
            }
          }
          , ((Integer)Fortuna._scheduledSpawns.get(groupName)).intValue()));
        }
      }
    }
    , delay);
  }

  public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
  {
    return null;
  }

  private EnterInstanceResult checkConditions(L2PcInstance player, int instanceTemplateId)
  {
    L2Party party = player.getParty();

    if (player.isGM())
    {
      Long reEnterTime = Long.valueOf(InstanceManager.getInstance().getInstanceTime(player.getObjectId(), instanceTemplateId));
      if (System.currentTimeMillis() < reEnterTime.longValue())
      {
        player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_MAY_NOT_REENTER_YET).addPcName(player));
        return EnterInstanceResult.LOCKED_DUE_TO_ENTER_TIME;
      }
      return EnterInstanceResult.OK;
    }

    if (player.getParty() == null)
    {
      player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
      return EnterInstanceResult.NO_PARTY;
    }

    int minPlayers = ConfigGrandBoss.MIN_FORTUNA_PLAYERS;
    int maxPlayers = ConfigGrandBoss.MAX_FORTUNA_PLAYERS;
    int minLevel = ConfigGrandBoss.MIN_LEVEL_FORTUNA_PLAYERS;

    if (party.getLeader() != player)
    {
      party.getCommandChannel().broadcastMessage(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
      return EnterInstanceResult.NOT_LEADER;
    }
    if (party.getMemberCount() > maxPlayers)
    {
      party.broadcastMessage(SystemMessageId.PARTY_EXCEEDED_THE_LIMIT_CANT_ENTER);
      return EnterInstanceResult.TOO_MANY_MEMBERS;
    }
    if (party.getMemberCount() < minPlayers)
    {
      party.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_PEOPLE_TO_ENTER_INSTANCE_ZONE_NEED_S1).addNumber(minPlayers));
      return EnterInstanceResult.TOO_LESS_MEMBERS;
    }

    for (L2PcInstance member : party.getMembers())
    {
      if ((member == null) || (member.getLevel() < minLevel) || (!member.isAwakened()))
      {
        party.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_LEVEL_REQUIREMENT_NOT_SUFFICIENT).addPcName(member));
        return EnterInstanceResult.TOO_LOW_LEVEL;
      }
      if (!Util.checkIfInRange(1000, player, member, true))
      {
        party.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_IN_LOCATION_THAT_CANNOT_BE_ENTERED).addPcName(member));
        return EnterInstanceResult.MEMBER_OUT_OF_RANGE;
      }
      Long reEnterTime = Long.valueOf(InstanceManager.getInstance().getInstanceTime(member.getObjectId(), instanceTemplateId));
      if (System.currentTimeMillis() < reEnterTime.longValue())
      {
        party.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_MAY_NOT_REENTER_YET).addPcName(member));
        return EnterInstanceResult.LOCKED_DUE_TO_ENTER_TIME;
      }
    }
    return EnterInstanceResult.OK;
  }

  public synchronized String onEnterZone(L2Character character, L2ZoneType zone)
  {
    if ((character instanceof L2PcInstance))
    {
      FortunaWorld world = (FortunaWorld)InstanceManager.getInstance().getInstanceWorld(character, FortunaWorld.class);

      if (world != null)
      {
        if (!world.playersInside.contains(character))
          world.playersInside.add((L2PcInstance)character);
      }
    }
    return super.onEnterZone(character, zone);
  }

  public String onExitZone(L2Character character, L2ZoneType zone)
  {
    if ((character instanceof L2PcInstance))
    {
      FortunaWorld world = (FortunaWorld)InstanceManager.getInstance().getInstanceWorld(character, FortunaWorld.class);

      if (world != null)
      {
        if (world.playersInside.contains(character))
          world.playersInside.remove(character);
      }
    }
    return super.onExitZone(character, zone);
  }

  public static void main(String[] args)
  {
    _fortunaInstance = new Fortuna();
  }

  public static Fortuna getInstance()
  {
    return _fortunaInstance;
  }

  static
  {
    _scheduledSpawns.put("monsters_1", Integer.valueOf(2000));
    _scheduledSpawns.put("monsters_2", Integer.valueOf(7000));
    _scheduledSpawns.put("monsters_3", Integer.valueOf(12000));
    _scheduledSpawns.put("monsters_4", Integer.valueOf(15000));
    _scheduledSpawns.put("monsters_5", Integer.valueOf(20000));
    _scheduledSpawns.put("monsters_6", Integer.valueOf(25000));
    _scheduledSpawns.put("monsters_7", Integer.valueOf(30000));
    _scheduledSpawns.put("monsters_8", Integer.valueOf(37000));
    _scheduledSpawns.put("monsters_9", Integer.valueOf(40000));
    _scheduledSpawns.put("monsters_10", Integer.valueOf(45000));
    _scheduledSpawns.put("monsters_11", Integer.valueOf(48000));
    _scheduledSpawns.put("yui", Integer.valueOf(108000));
    _scheduledSpawns.put("monsters_13", Integer.valueOf(118000));
    _scheduledSpawns.put("monsters_14", Integer.valueOf(128000));
    _scheduledSpawns.put("monsters_15", Integer.valueOf(137000));
    _scheduledSpawns.put("monsters_16", Integer.valueOf(119200));
    _scheduledSpawns.put("monsters_17", Integer.valueOf(148300));
    _scheduledSpawns.put("kinen", Integer.valueOf(200000));
    _scheduledSpawns.put("kinen_transformed", Integer.valueOf(230000));
    _scheduledSpawns.put("monsters_18", Integer.valueOf(340000));
    _scheduledSpawns.put("monsters_19", Integer.valueOf(343000));
    _scheduledSpawns.put("monsters_20", Integer.valueOf(359000));
    _scheduledSpawns.put("monsters_21", Integer.valueOf(343000));
    _scheduledSpawns.put("monsters_22", Integer.valueOf(346000));
    _scheduledSpawns.put("monsters_23", Integer.valueOf(348000));
    _scheduledSpawns.put("monsters_24", Integer.valueOf(350000));
    _scheduledSpawns.put("monsters_25", Integer.valueOf(356000));
    _scheduledSpawns.put("monsters_26", Integer.valueOf(378000));
    _scheduledSpawns.put("monsters_27", Integer.valueOf(408000));
    _scheduledSpawns.put("konyar", Integer.valueOf(409000));
    _scheduledSpawns.put("konyar_tranformed", Integer.valueOf(449000));
    _scheduledSpawns.put("konyar_tranformed_2", Integer.valueOf(487000));
    _scheduledSpawns.put("monsters_28", Integer.valueOf(547000));
    _scheduledSpawns.put("monsters_29", Integer.valueOf(553000));
    _scheduledSpawns.put("monsters_30", Integer.valueOf(568000));
    _scheduledSpawns.put("monsters_31", Integer.valueOf(578000));
    _scheduledSpawns.put("monsters_32", Integer.valueOf(588000));
    _scheduledSpawns.put("rakiello", Integer.valueOf(628000));
    _scheduledSpawns.put("monsters_33", Integer.valueOf(638000));
    _scheduledSpawns.put("monsters_34", Integer.valueOf(643000));
    _scheduledSpawns.put("monsters_35", Integer.valueOf(665000));
    _scheduledSpawns.put("monsters_36", Integer.valueOf(668000));
    _scheduledSpawns.put("monsters_37", Integer.valueOf(672000));
    _scheduledSpawns.put("monsters_38", Integer.valueOf(674000));
    _scheduledSpawns.put("monsters_39", Integer.valueOf(690000));
    _scheduledSpawns.put("monsters_40", Integer.valueOf(740000));
    _scheduledSpawns.put("monsters_41", Integer.valueOf(750000));
    _scheduledSpawns.put("monsters_42", Integer.valueOf(765000));
    _scheduledSpawns.put("monsters_43", Integer.valueOf(767000));
    _scheduledSpawns.put("monsters_44", Integer.valueOf(770000));
    _scheduledSpawns.put("monsters_45", Integer.valueOf(777000));
    _scheduledSpawns.put("monsters_46", Integer.valueOf(781000));
    _scheduledSpawns.put("monsters_47", Integer.valueOf(791000));
    _scheduledSpawns.put("monsters_48", Integer.valueOf(796000));
    _scheduledSpawns.put("monsters_49", Integer.valueOf(803000));
    _scheduledSpawns.put("monsters_50", Integer.valueOf(828000));
    _scheduledSpawns.put("monsters_51", Integer.valueOf(858000));
    _scheduledSpawns.put("monsters_52", Integer.valueOf(883000));
    _scheduledSpawns.put("monsters_53", Integer.valueOf(983000));
    _scheduledSpawns.put("monsters_54", Integer.valueOf(993000));
    _scheduledSpawns.put("monsters_55", Integer.valueOf(998000));
    _scheduledSpawns.put("monsters_56", Integer.valueOf(1023000));
    _scheduledSpawns.put("monsters_57", Integer.valueOf(1034000));
    _scheduledSpawns.put("monsters_58", Integer.valueOf(1023000));
    _scheduledSpawns.put("monsters_59", Integer.valueOf(1026000));
    _scheduledSpawns.put("monsters_60", Integer.valueOf(1031000));
    _scheduledSpawns.put("monsters_61", Integer.valueOf(1034000));
    _scheduledSpawns.put("monsters_62", Integer.valueOf(1038000));
    _scheduledSpawns.put("monsters_63", Integer.valueOf(1037000));
    _scheduledSpawns.put("monsters_64", Integer.valueOf(1077000));
    _scheduledSpawns.put("monsters_65", Integer.valueOf(1082000));
    _scheduledSpawns.put("thron", Integer.valueOf(1097000));
    _scheduledSpawns.put("monsters_66", Integer.valueOf(1100000));
    _scheduledSpawns.put("monsters_67", Integer.valueOf(1107000));
    _scheduledSpawns.put("monsters_68", Integer.valueOf(1112000));
  }

  public class FortunaWorld extends InstanceManager.InstanceWorld
  {
    public List<L2PcInstance> playersInside = new FastList();
    public List<Future<?>> spawnTasks = new FastList();

    public FortunaWorld()
    {
    }
  }

  public static enum EnterInstanceResult
  {
    OK, 
    ANOTHER_INSTANCE, 
    NO_PARTY, 
    NOT_LEADER, 
    TOO_MANY_MEMBERS, 
    TOO_LESS_MEMBERS, 
    TOO_LOW_LEVEL, 
    MEMBER_OUT_OF_RANGE, 
    LOCKED_DUE_TO_ENTER_TIME, 
    CANNOT_REENTER;
  }
}