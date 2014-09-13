package l2next.gameserver.network.clientpackets.Beautyshop;

 import java.io.PrintStream;
 import l2next.gameserver.model.Player;
 import l2next.gameserver.network.GameClient;
 import l2next.gameserver.network.clientpackets.L2GameClientPacket;
 import l2next.gameserver.network.serverpackets.Beautyshop.ExResponseBeautyRegistResetPacket;

 public class RequestRegistBeauty   extends L2GameClientPacket
 {
   private int face;
   private int hairstyle;
  private int haircolor;
  private int var1;
  private int FHOld;
 
 protected void readImpl()
    throws Exception
   {
     this.hairstyle = readD();
    this.face = readD();
     this.haircolor = readD();
 }
  
 protected void runImpl()    throws Exception
  {
    Player activeChar = ((GameClient)getClient()).getActiveChar();
    if (activeChar == null) {
     return;
    }
     activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 0, this.face, this.hairstyle, this.haircolor));
     System.out.println(this.hairstyle);
     System.out.println(this.face);
     System.out.println(this.haircolor);
    if ((this.face == -1) || (this.hairstyle == -1) || (this.haircolor == -1))
   {
     if (this.face == -1)
       {
        activeChar.setHairStyle(this.hairstyle);
         activeChar.setHairColor(this.haircolor);
        activeChar.broadcastUserInfo(true);
       }
      if (this.hairstyle == -1)
      {
        activeChar.setFace(this.face);
        activeChar.setHairColor(this.haircolor);
        activeChar.broadcastUserInfo(true);
      }
      if (this.haircolor == -1)
      {
        activeChar.setFace(this.face);
        activeChar.broadcastUserInfo(true);
      }
    }
    else
     {
       activeChar.setFace(this.face);
      activeChar.setHairStyle(this.hairstyle);
       activeChar.setHairColor(this.haircolor);
      activeChar.broadcastUserInfo(true);
     }
   }
 }
