package l2god.scripts.npc.town;

import l2god.gameserver.datatables.xml.MultiSellData;
import l2god.gameserver.instancemanager.InstanceManager;
import l2god.gameserver.model.actor.L2Npc;
import l2god.gameserver.model.actor.instance.L2PcInstance;
import l2god.gameserver.model.items.base.proptypes.ProcessType;
import l2god.gameserver.model.items.itemcontainer.PcInventory;
import l2god.gameserver.model.player.base.ClassId;
import l2god.gameserver.model.world.Instance;
import l2god.gameserver.model.world.quest.Quest;
import org.apache.commons.lang3.ArrayUtils;

public class VoucherTrader extends Quest
{
  private static final int[] VoucherTraders = { 33385, 33386, 33387, 33388 };
  private static final int SignOfAllegiance = 17739;
  private static final int SignOfPledge = 17740;
  private static final int SignOfSincerity = 17741;
  private static final int SignOfWill = 17742;
  private static final int SealOfAllegiance = 17743;
  private static final int SealOfPledge = 17744;
  private static final int SealOfSincerity = 17745;
  private static final int SealOfWill = 17746;

  public VoucherTrader()
  {
    addAskId(VoucherTraders, -928);
  }

  public String onAsk(L2PcInstance player, L2Npc npc, int ask, int reply)
  {
    if (ArrayUtils.contains(VoucherTraders, npc.getNpcId()))
    {
      switch (reply)
      {
      case 0:
        int sign = -1; int seal = -1; int exp = -1;
        switch (npc.getNpcId())
        {
        case 33385:
          sign = 17739;
          seal = 17743;
          exp = 60000000;
          break;
        case 33386:
          sign = 17740;
          seal = 17744;
          exp = 66000000;
          break;
        case 33387:
          sign = 17741;
          seal = 17745;
          exp = 68000000;
          break;
        case 33388:
          sign = 17742;
          seal = 17746;
          exp = 76000000;
        }

        if (player.getInventory().getCountOf(sign) == 0L)
        {
          return npc.getServerName() + "000b.htm";
        }
        if ((player.getVar(npc.getServerName() + "_trade") != null) && (Long.parseLong(player.getVar(npc.getServerName() + "_trade")) > System.currentTimeMillis()))
        {
          return npc.getServerName() + "002.htm";
        }

        player.setVar(npc.getServerName() + "_trade", String.valueOf(System.currentTimeMillis() + 86400000L));
        player.exchangeItemsById(ProcessType.NPC, npc, sign, 1L, seal, 20L, true);
        player.addExpAndSp(exp, 0);
        return npc.getServerName() + "003a.htm";
      case 1:
        switch (npc.getNpcId())
        {
        case 33385:
          MultiSellData.getInstance().separateAndSend(720, player, npc, false);
          break;
        case 33386:
          MultiSellData.getInstance().separateAndSend(721, player, npc, false);
          break;
        case 33387:
          MultiSellData.getInstance().separateAndSend(722, player, npc, false);
          break;
        case 33388:
          MultiSellData.getInstance().separateAndSend(723, player, npc, false);
        }

        return null;
      case 2:
        return "voucher_trader001.htm";
      case 3:
        Instance instance = InstanceManager.getInstance().getInstance(player.getInstanceId());
        if (instance != null)
        {
          instance.ejectPlayer(player.getObjectId());
        }
        return null;
      case 4:
        return npc.getServerName() + "004.htm";
      case 11:
        return npc.getServerName() + "005.htm";
      case 12:
        if (!player.isAwakened())
        {
          return "voucher_trader1007.htm";
        }
        int npcOffset = (npc.getNpcId() - 33385) * 8;
        switch (player.getClassId().getGeneralIdForAwaken())
        {
        case 139:
          MultiSellData.getInstance().separateAndSend(735 + npcOffset, player, npc, false);
          break;
        case 140:
          MultiSellData.getInstance().separateAndSend(736 + npcOffset, player, npc, false);
          break;
        case 141:
          MultiSellData.getInstance().separateAndSend(737 + npcOffset, player, npc, false);
          break;
        case 142:
          MultiSellData.getInstance().separateAndSend(738 + npcOffset, player, npc, false);
          break;
        case 143:
          MultiSellData.getInstance().separateAndSend(739 + npcOffset, player, npc, false);
          break;
        case 144:
          MultiSellData.getInstance().separateAndSend(740 + npcOffset, player, npc, false);
          break;
        case 145:
          MultiSellData.getInstance().separateAndSend(741 + npcOffset, player, npc, false);
          break;
        case 146:
          MultiSellData.getInstance().separateAndSend(742 + npcOffset, player, npc, false);
        }

        return null;
      case 729:
      case 733:
        if (!player.isAwakened())
        {
          return "voucher_trader1007.htm";
        }
        int npcOffset = (npc.getNpcId() - 33385) * 8;
        MultiSellData.getInstance().separateAndSend(reply + npcOffset, player, npc, false);
        return null;
      }
    }

    return null;
  }

  public static void main(String[] args)
  {
    new VoucherTrader();
  }
}