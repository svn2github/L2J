
package l2next.gameserver.network.clientpackets.Beautyshop;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.Beautyshop.ExResponseBeautyListPacket;
 
public class RequestShowBeautyList   extends L2GameClientPacket
 {
   private int check;
   
  protected void readImpl()    throws Exception
   {
    this.check = readD();
   }
  
  protected void runImpl()    throws Exception
  {
    Player activeChar = ((GameClient)getClient()).getActiveChar();
    if (activeChar == null) {
      return;
     }
    activeChar.sendPacket(new ExResponseBeautyListPacket(activeChar, this.check));
   }
 }