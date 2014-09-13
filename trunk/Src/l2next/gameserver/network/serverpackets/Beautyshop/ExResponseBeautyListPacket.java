package l2next.gameserver.network.serverpackets.Beautyshop;

/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import l2next.gameserver.model.Player;
/*     */ import l2next.gameserver.model.items.PcInventory;
/*     */ import l2next.gameserver.network.serverpackets.L2GameServerPacket;
/*     */ 
/*     */ public class ExResponseBeautyListPacket
/*     */   extends L2GameServerPacket
/*     */ {
/*     */   private long _adena_count;
/*     */   private long _item_count;
/*     */   private int _check;
/*     */   private int id1;
/*     */   private int id2;
/*  19 */   private int[] face = { 10001, 10002, 10003, 10004, 10005, 10006, 10007, 10008, 10009, 10010 };
/*  31 */   private int[] face2 = { 99999959, 99999982, 99999948, 99999936, 99999992, 99999987, 99999962, 99999972, 99999936, 99999927 };
/*  43 */   private List<int[]> _reqs = Collections.emptyList();
/*     */   
/*     */   public ExResponseBeautyListPacket(Player pl, int check)
/*     */   {
/*  47 */     this._adena_count = pl.getInventory().getAdena();
/*  48 */     this._item_count = pl.getInventory().getAdena2();
/*  49 */     this._check = check;
/*     */   }
/*     */   
/*     */   protected void writeImpl()
/*     */   {
/*  64 */     writeQ(this._adena_count);
/*  65 */     writeQ(this._item_count);
/*  66 */     writeD(this._check);
/*  67 */     if (this._check == 0)
/*     */     {
/*  69 */       writeD(10);
/*     */       
/*  71 */       writeD(10001);
/*  72 */       writeD(99999959);
/*     */       
/*  74 */       writeD(10002);
/*  75 */       writeD(99999982);
/*     */       
/*  77 */       writeD(10003);
/*  78 */       writeD(99999948);
/*     */       
/*  80 */       writeD(10004);
/*  81 */       writeD(99999936);
/*     */       
/*  83 */       writeD(10005);
/*  84 */       writeD(99999992);
/*     */       
/*  86 */       writeD(10006);
/*  87 */       writeD(99999987);
/*     */       
/*  89 */       writeD(10007);
/*  90 */       writeD(99999962);
/*     */       
/*  92 */       writeD(10008);
/*  93 */       writeD(99999972);
/*     */       
/*  95 */       writeD(10009);
/*  96 */       writeD(99999936);
/*     */       
/*  98 */       writeD(10010);
/*  99 */       writeD(99999927);
/*     */     }
/* 101 */     if (this._check == 1)
/*     */     {
/* 103 */       writeD(5);
/*     */       
/* 105 */       writeD(20001);
/* 106 */       writeD(99999982);
/*     */       
/* 108 */       writeD(20002);
/* 109 */       writeD(99999970);
/*     */       
/* 111 */       writeD(20003);
/* 112 */       writeD(99999981);
/*     */       
/* 114 */       writeD(20004);
/* 115 */       writeD(99999937);
/*     */       
/* 117 */       writeD(20005);
/* 118 */       writeD(99999956);
/*     */     }
/*     */   }
/*     */ }