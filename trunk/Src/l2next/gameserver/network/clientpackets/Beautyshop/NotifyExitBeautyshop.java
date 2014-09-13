
package l2next.gameserver.network.clientpackets.Beautyshop;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;

 public class NotifyExitBeautyshop   extends L2GameClientPacket
 {
   protected void readImpl()
     throws Exception
  {}
  
   protected void runImpl()
     throws Exception
   {
   Player activeChar = ((GameClient)getClient()).getActiveChar();
    if (activeChar == null) {}
  }
 }
