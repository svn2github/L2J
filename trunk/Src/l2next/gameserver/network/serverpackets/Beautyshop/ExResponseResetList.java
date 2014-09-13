package l2next.gameserver.network.serverpackets.Beautyshop;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;

public class ExResponseResetList
  extends L2GameServerPacket
{
  private final long _ownAdena;
  private final int _hairStyle;
  private final int _faceStyle;
  private final int _hairColor;
  private final long _item_count;
  
  public ExResponseResetList(Player pl, int hairStyle, int faceStyle, int hairColor)
  {
    this._ownAdena = pl.getInventory().getAdena();
    this._hairStyle = hairStyle;
    this._faceStyle = faceStyle;
    this._hairColor = hairColor;
    this._item_count = pl.getInventory().getAdena2();
  }
  
  protected void writeImpl()
  {
    writeQ(this._ownAdena);
    writeQ(this._item_count);
    
    writeD(this._hairStyle);
    writeD(this._faceStyle);
    writeD(this._hairColor);
  }
}