package l2god.scripts.ai.group_template;

import java.util.List;
import l2god.config.scripts.ConfigChaosFestival;
import l2god.gameserver.ThreadPoolManager;
import l2god.gameserver.instancemanager.InstanceManager;
import l2god.gameserver.model.actor.L2Npc;
import l2god.gameserver.model.actor.instance.L2MonsterInstance;
import l2god.gameserver.model.actor.instance.L2PcInstance;
import l2god.gameserver.model.holders.ItemHolder;
import l2god.gameserver.model.world.quest.Quest;
import l2god.gameserver.util.Rnd;
import l2god.scripts.instances.ChaosFestival.ChaosFestivalWorld;

public class ChaosFestivalMobs extends Quest
{
  public static final int HERB_BOX = 19292;
  public static final int MYST_BOX = 19267;
  public static final int HERB_OF_HEALTH = 35983;
  public static final int HERB_OF_POWER = 35984;
  public static final int MYST_SIGN = 34900;

  public String onAttack(final L2Npc npc, final L2PcInstance attacker, int damage, boolean isPet)
  {
    ChaosFestival.ChaosFestivalWorld world = (ChaosFestival.ChaosFestivalWorld)InstanceManager.getInstance().getInstanceWorld(npc, ChaosFestival.ChaosFestivalWorld.class);

    if (world == null) {
      return null;
    }
    switch (npc.getNpcId())
    {
    case 19292:
      npc.doDie(attacker);

      ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
      {
        public void run()
        {
          ((L2MonsterInstance)npc).dropItem(attacker, new ItemHolder(35983, 1L));

          if ((Rnd.get() <= 0.05D) || (attacker.isGM()))
          {
            ((L2MonsterInstance)npc).dropItem(attacker, new ItemHolder(35984, 1L));
          }
        }
      }
      , 3000L);

      break;
    case 19267:
      npc.doDie(attacker);

      ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
      {
        public void run()
        {
          int signsCount = Rnd.get(ConfigChaosFestival.CHAOS_FESTIVAL_MYST_BOX_SIGNS_MIN_COUNT, ConfigChaosFestival.CHAOS_FESTIVAL_MYST_BOX_SIGNS_MAX_COUNT);
          ((L2MonsterInstance)npc).dropItem(attacker, new ItemHolder(34900, signsCount));

          if ((ConfigChaosFestival.CHAOS_FESTIVAL_MYST_BOX_RANDOM_REWARDS != null) && (ConfigChaosFestival.CHAOS_FESTIVAL_MYST_BOX_RANDOM_REWARDS.size() > 0))
          {
            double chance = Rnd.get();
            int selectedItemId = 0;
            double max = 1.0D;
            for (int[] itemInfo : ConfigChaosFestival.CHAOS_FESTIVAL_MYST_BOX_RANDOM_REWARDS)
            {
              int itemId = itemInfo[0];
              double itemChance = itemInfo[1] / 100000.0D;
              if (max - itemChance <= chance)
              {
                selectedItemId = itemId;
                break;
              }
              max -= itemChance;
            }

            ((L2MonsterInstance)npc).dropItem(attacker, new ItemHolder(selectedItemId, 1L));
          }
        }
      }
      , 3000L);
    }

    return null;
  }

  public String onSpawn(L2Npc npc)
  {
    npc.setIsNoRndWalk(true);
    return null;
  }

  public ChaosFestivalMobs()
  {
    addAttackId(new int[] { 19292, 19267 });
    addSpawnId(new int[] { 19292, 19267 });
  }

  public static void main(String[] args)
  {
    new ChaosFestivalMobs();
  }
}