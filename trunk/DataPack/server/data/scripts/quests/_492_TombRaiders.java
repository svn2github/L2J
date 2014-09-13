/**package quests; TODO: st.isNowAvaileble нету метода в QuestState вфнек поправь

 import org.apache.commons.lang3.ArrayUtils;

 import l2next.commons.util.Rnd;

 import l2next.gameserver.model.instances.NpcInstance;
 import l2next.gameserver.model.quest.Quest;
 import l2next.gameserver.model.quest.QuestState;
 import l2next.gameserver.scripts.ScriptFile;

 /*
 **@author JustForFun
 * @date 13/04/13


public class _492_TombRaiders extends Quest implements ScriptFile
{
private static final int NPC_ZENYA = 32140;
private static final int[] MOBS =
{
23193, 23194, 23195, 23196
};

public _492_TombRaiders()
{
super(false);

addStartNpc(NPC_ZENYA);
addKillId(MOBS);
addLevelCheck(80, 99);
}

 @Override public String onEvent(String event, QuestState st, NpcInstance npc)
 {
 if (event.equalsIgnoreCase("32140-06.htm"))
 {
 st.setState(STARTED);
 st.setCond(1);
 st.playSound(SOUND_ACCEPT);
 }

 return event;
 }

 @Override public String onTalk(NpcInstance npc, QuestState st)
 {
 int npcId = npc.getNpcId();
 String htmltext = "noquest";

 if (npcId == NPC_ZENYA)
 {
 switch (st.getState())
 {
 case DELAYED:
 htmltext = "32140-05.htm";
 break;
 case CREATED:
 if (st.isNowAvailable())
 {
 if (st.getPlayer().getLevel() >= 80)
 {
 // РљРІРµСЃС‚ РґРѕСЃС‚СѓРїРµРЅ РґР»СЏ 3РµР№ РїСЂРѕС„С‹
 if (st.getPlayer().getClassId().getLevel() >= 4)
 {
 htmltext = "32140-01.htm";
 }
 else
 {
 htmltext = "32140-02.htm";

 st.exitCurrentQuest(true);
 }
 }
 else
 {
 htmltext = "32140-03.htm";
 }
 }
 else
 {
 htmltext = "32140-04";
 }

 break;
 case STARTED:
 if (st.getCond() == 1)
 {
 htmltext = "32140-07.htm";
 }
 else
 {
 if ((st.getCond() == 2) && (st.getQuestItemsCount(34769) >= 50))
 {
 htmltext = "32140-08.htm";

 st.addExpAndSp(25000000, 28500000);
 st.playSound(SOUND_FINISH);
 st.exitCurrentQuest(this);
 }
 }
 }
 }

 return htmltext;
 }

 @Override public String onKill(NpcInstance npc, QuestState st)
 {
 if (st.getCond() == 1)
 {
 if (Rnd.chance(50) && ArrayUtils.contains(MOBS, npc.getNpcId()))
 {
 if (st.getQuestItemsCount(34769) < 50)
 {
 st.giveItems(34769, 1);

 if (st.getQuestItemsCount(34769) >= 50)
 {
 st.setCond(2);
 st.playSound(SOUND_MIDDLE);
 }
 }
 }
 }

 return null;
 }

 @Override public void onLoad()
 {
 }

 @Override public void onReload()
 {
 }

 @Override public void onShutdown()
 {
 }
 }
 */