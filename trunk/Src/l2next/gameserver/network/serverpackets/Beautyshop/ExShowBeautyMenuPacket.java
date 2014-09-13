package l2next.gameserver.network.serverpackets.Beautyshop;

import l2next.gameserver.network.serverpackets.L2GameServerPacket;
/*    */ 
/*    */ public class ExShowBeautyMenuPacket
/*    */   extends L2GameServerPacket
/*    */ {
/*    */   private final int _result;
/*    */   
/*    */   public ExShowBeautyMenuPacket(int result)
/*    */   {
/* 12 */     this._result = result;
/*    */   }
/*    */   
/*    */   protected void writeImpl()
/*    */   {
/* 18 */     writeD(this._result);
/*    */   }
/*    */ }