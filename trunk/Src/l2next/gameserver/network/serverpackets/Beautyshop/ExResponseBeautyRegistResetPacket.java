package l2next.gameserver.network.serverpackets.Beautyshop;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.utils.ItemFunctions;

public class ExResponseBeautyRegistResetPacket
  extends L2GameServerPacket
{
  private long _adena_count;
  private long _item_count;
  private int _check;
  private final int _hairStyle;
  private final int _faceStyle;
  private final int _hairColor;
  
  public ExResponseBeautyRegistResetPacket(Player pl, int check, int hairStyle, int faceStyle, int hairColor)
  {
    this._adena_count = pl.getInventory().getAdena();
    this._item_count = pl.getInventory().getAdena2();
    this._check = check;
    this._hairStyle = hairStyle;
    this._faceStyle = faceStyle;
    this._hairColor = hairColor;
  }
  
  protected void writeImpl()
  {
    writeQ(this._adena_count);
    writeQ(this._item_count);
    writeD(this._check);
    writeD(1);
    
    writeD(this._hairStyle);
    writeD(this._faceStyle);
    writeD(this._hairColor);
    

    Player activeChar = ((GameClient)getClient()).getActiveChar();
    if (activeChar == null) {
      return;
    }
    if (this._adena_count > 0L) {
      ItemFunctions.removeItem(activeChar, 57, 15000000L, true);
    }
    if (this._item_count > 0L) {
      ItemFunctions.removeItem(activeChar, 36308, 3L, true);
    }
  }
}