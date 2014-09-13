package l2god.scripts.ai.zone;

import gnu.trove.map.hash.TIntIntHashMap;
import l2god.gameserver.ThreadPoolManager;
import l2god.gameserver.instancemanager.ZoneManager;
import l2god.gameserver.model.actor.L2Attackable;
import l2god.gameserver.model.actor.L2Character;
import l2god.gameserver.model.actor.L2Npc;
import l2god.gameserver.model.actor.controller.character.LocationController;
import l2god.gameserver.model.actor.instance.L2PcInstance;
import l2god.gameserver.model.holders.SkillHolder;
import l2god.gameserver.model.world.quest.Quest;
import l2god.gameserver.model.world.zone.L2ZoneType;
import l2god.gameserver.network.game.components.NpcStringId;
import l2god.gameserver.network.game.serverpackets.FlyToLocation;
import l2god.gameserver.network.game.serverpackets.FlyToLocation.FlyType;
import l2god.gameserver.network.game.serverpackets.packet.show.ExShowScreenMessage;
import l2god.gameserver.network.game.serverpackets.packet.vehicle.boat.ValidateLocation;
import l2god.gameserver.util.Rnd;

public class LandOfChaos extends Quest
{
  private L2Attackable _mobs;
  private static TIntIntHashMap _mob = new TIntIntHashMap();
  private static final int Дух_Зеленый = 19460;
  private static final int Дух_Синий = 19461;
  private static final int Дух_Красный = 19462;
  private static final int Кровь_с_душой = 19463;
  private static final int Рега = 19475;
  private static final int Червяк_Смерти = 23336;
  private static final int Скалейбл = 23330;
  private static final int Исчезающий_Зомби = 23333;
  private static final int Селроп = 23334;
  private static final int Порус = 23335;
  private static final int Рог_Хаоса = 23348;
  private static final int[][] SPAWNLIST = { { 23330, 1, 2, 40 }, { 23333, 1, 2, 40 }, { 23334, 1, 2, 40 }, { 23335, 1, 2, 40 }, { 23336, 1, 2, 40 } };

  private static final SkillHolder Кровавая_возможность = new SkillHolder(15537, 2);
  private static final SkillHolder Дух_Зел = new SkillHolder(15569, 1);

  private static int craterZone1 = 4600090;
  private static int craterZone2 = 4600091;
  private static int craterZone3 = 4600092;
  private static int craterZone4 = 4600093;
  private static int craterZone5 = 4600094;
  private static int craterZone6 = 4600095;
  private static int craterZone7 = 4600096;
  private static int craterZone8 = 4600097;
  private static int craterZone9 = 4600098;
  private static int craterZone10 = 4600099;

  public LandOfChaos()
  {
    addAttackId(new int[] { 19460, 19461, 19462 });
    addSpawnId(new int[] { 19463, 19460, 19461, 19462, 19475, 23336 });
    onSpawnRerun(new int[] { 19463, 19460, 19461, 19462, 19475, 23336 });
    addFirstTalkId(19463);
    addKillId(23348);

    ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new clearMobs(), 600000L, 600000L);
  }

  public String onSpawn(L2Npc npc)
  {
    if (npc.getNpcId() == 19475)
    {
      npc.setIsNoAnimation(true);
    }
    else if (npc.getNpcId() == 23336)
    {
      npc.setIsNoRndWalk(true);
    }
    else if ((npc.getNpcId() == 19460) || (npc.getNpcId() == 19462) || (npc.getNpcId() == 19461))
    {
      npc.setIsInvul(true);
      npc.setIsNoRndWalk(true);
      npc.setIsCanMove(false);
      npc.setIsNoAttackingBack(true);
    }

    return super.onSpawn(npc);
  }

  public final String onFirstTalk(L2Npc npc, L2PcInstance player)
  {
    if (npc == null)
    {
      return null;
    }

    int npcId = npc.getNpcId();

    if (npcId == 19463)
    {
      npc.setTarget(player);
      player.sendPacket(new ExShowScreenMessage(NpcStringId.getNpcStringId(1802306), 2, 5000, new String[0]));
      npc.doCast(Кровавая_возможность.getSkill());
      npc.getLocationController().delete();
    }

    return null;
  }

  public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
  {
    if (npc == null)
    {
      return super.onAttack(npc, player, damage, isPet);
    }

    int npcId = npc.getNpcId();

    if (npcId == 19462)
    {
      if (Rnd.getChance(30))
      {
        for (int[] spawns : SPAWNLIST)
        {
          if (Rnd.getChance(spawns[3]))
          {
            for (int i = 0; i < Rnd.get(spawns[1], spawns[2]); i++)
            {
              L2Attackable npcs = (L2Attackable)addSpawn(spawns[0], npc.getX() + Rnd.get(100), npc.getY() + Rnd.get(100), npc.getZ(), 0, false, 0L, false, player.getInstanceId());
              npcs.attackCharacter(player);
            }
          }
        }
        npc.getLocationController().delete();
      }
    }
    else if (npcId == 19461)
    {
      if (Rnd.getChance(30))
      {
        if (!_mob.containsValue(player.getObjectId()))
        {
          for (int i = 0; i < Rnd.get(2, 4); i++)
          {
            _mobs = ((L2Attackable)addSpawn(23348, npc.getX() + Rnd.get(100), npc.getY() + Rnd.get(100), npc.getZ(), 0, false, 0L, false, player.getInstanceId()));
            _mob.put(_mobs.getObjectId(), player.getObjectId());
          }

        }

        npc.getLocationController().delete();
      }
    }
    else if (npcId == 19460)
    {
      if ((ZoneManager.getInstance().getZoneById(craterZone1).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone1).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone2).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone2).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone3).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone3).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone4).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone4).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone5).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone5).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone6).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone6).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone7).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone7).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone8).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone8).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone9).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone9).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
      else if ((ZoneManager.getInstance().getZoneById(craterZone10).isInsideZone(npc)) && (Rnd.getChance(30)))
      {
        for (L2Character mobs : ZoneManager.getInstance().getZoneById(craterZone10).getCharactersInside())
        {
          if ((mobs.isMonster()) && (mobs.getAttackable().getNpcId() != 19462) && (mobs.getAttackable().getNpcId() != 19461) && (mobs.getAttackable().getNpcId() != 19460))
          {
            if (Rnd.getChance(10))
            {
              npc.doCast(Дух_Зел.getSkill());
              moveToPoint(npc, player, mobs);
            }
          }
        }
        npc.getLocationController().delete();
      }
    }

    return super.onAttack(npc, player, damage, isPet);
  }

  public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
  {
    if (npc.getNpcId() == 23348)
    {
      if (_mob.containsKey(npc.getObjectId()))
      {
        _mob.remove(npc.getObjectId());
      }
    }
    return super.onKill(npc, player, isPet);
  }

  protected void moveToPoint(L2Npc npc, L2PcInstance player, L2Character cha)
  {
    int x = npc.getX() + Rnd.get(100);
    int y = npc.getY() + Rnd.get(100);
    int z = npc.getZ();

    player.broadcastPacket(new FlyToLocation(cha, x, y, z, FlyToLocation.FlyType.THROW_HORIZONTAL, 800, 0, 0));
    cha.setXYZ(x, y, z);
    cha.broadcastPacket(new ValidateLocation(npc));
  }

  public static void main(String[] args)
  {
    new LandOfChaos();
  }

  private class clearMobs
    implements Runnable
  {
    public clearMobs()
    {
    }

    public void run()
    {
      for (int npcObjectId : LandOfChaos._mob.keys())
      {
        LandOfChaos._mob.remove(npcObjectId);
      }
    }
  }
}