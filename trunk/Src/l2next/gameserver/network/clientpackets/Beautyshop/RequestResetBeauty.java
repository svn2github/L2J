package l2next.gameserver.network.clientpackets.Beautyshop;

import l2next.gameserver.model.Player;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.clientpackets.L2GameClientPacket;
import l2next.gameserver.network.serverpackets.Beautyshop.ExResponseBeautyRegistResetPacket;
import l2next.gameserver.network.serverpackets.Beautyshop.ExResponseResetList;

public class RequestResetBeauty
  extends L2GameClientPacket
{
  private int face;
  private int hairstyle;
  private int haircolor;
  
  protected void readImpl()
    throws Exception
  {
    this.hairstyle = readD();
    this.face = readD();
    this.haircolor = readD();
  }
  
  protected void runImpl()
    throws Exception
  {
    Player activeChar = ((GameClient)getClient()).getActiveChar();
    if (activeChar == null) {
      return;
    }
    activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, this.face, this.hairstyle, this.haircolor));
    activeChar.sendPacket(new ExResponseResetList(activeChar, this.hairstyle, this.face, this.haircolor));
  }
}
