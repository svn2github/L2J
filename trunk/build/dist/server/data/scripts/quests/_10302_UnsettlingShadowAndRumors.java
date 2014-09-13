package quests;

import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.model.quest.QuestState;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.components.NpcString;

/**
 * @author coldy
 * @date 27.07.2012 TODO: offlike EN HTMLs
 */
public class _10302_UnsettlingShadowAndRumors extends Quest implements ScriptFile
{
    private static final int NPC_KANTARUBIS = 32898;
    private static final int NPC_IZSHAEL = 32894;
    private static final int NPC_KES = 32901;
    private static final int NPC_MASTER_KEI = 32903;
    private static final int NPC_KITT = 32902;

    public _10302_UnsettlingShadowAndRumors()
    {
        super(false);

        addStartNpc(NPC_KANTARUBIS);
        addTalkId(NPC_IZSHAEL, NPC_KES, NPC_MASTER_KEI, NPC_KITT);
        addLevelCheck(90, 99);
        addQuestCompletedCheck(_10301_ShadowOfTerrorBlackishRedFog.class);
    }

    public String onEvent(String event, QuestState st, NpcInstance npc)
    {
        if (st == null)
        {
            return event;
        }

        if (event.equals("32898-02.htm"))
        {
            st.setState(STARTED);
            st.setCond(1);
            st.playSound(SOUND_ACCEPT);
        }
        else if (event.equals("32894-01.htm"))
        {
            st.setCond(2);
            st.playSound(SOUND_MIDDLE);
        }
        else if (event.equals("32901-01.htm"))
        {
            st.setCond(3);
            st.playSound(SOUND_MIDDLE);
        }
        else if (event.equals("32903-01.htm"))
        {
            st.setCond(4);
            st.playSound(SOUND_MIDDLE);
        }
        else if (event.equals("32902-01.htm"))
        {
            st.setCond(5);
            st.playSound(SOUND_MIDDLE);
        }
        else if (event.equals("32894-05.htm"))
        {
            st.setCond(6);
            st.playSound(SOUND_MIDDLE);
        }
        else if (event.equals("32898-06.htm"))
        {
            st.showQuestionMark(10304);
            st.playSound("ItemSound.quest_tutorial");
            st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.valueOf(530400), 600, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false));
            st.addExpAndSp(6728850, 755280);
            st.giveItems(57, 2177190);
            st.giveItems(34033, 1);
            st.playSound("ItemSound.quest_finish");
            st.exitCurrentQuest(false);
        }

        return event;
    }

    public String onTalk(NpcInstance npc, QuestState st)
    {
        String htmltext = "noquest";
        QuestState prevst = st.getPlayer().getQuestState(_10301_ShadowOfTerrorBlackishRedFog.class);

        if (npc.getNpcId() == NPC_KANTARUBIS)
        {
            switch (st.getState())
            {
                case COMPLETED:
                    htmltext = "32898-completed.htm";
                    break;
                case CREATED:
                    if (st.getPlayer().getLevel() >= 90)
                    {
                        if ((prevst != null) && (prevst.isCompleted()))
                        {
                            htmltext = "32898-00.htm";
                        }
                        else
                        {
                            htmltext = "32898-nolvl.htm";

                            st.exitCurrentQuest(true);
                        }
                    }
                    else
                    {
                        htmltext = "32898-nolvl.htm";

                        st.exitCurrentQuest(true);
                    }

                    break;
                case STARTED:
                    if (st.getCond() == 1)
                    {
                        htmltext = "32898-03.htm";
                    }
                    else
                    {
                        if (st.getCond() != 6)
                        {
                            break;
                        }

                        htmltext = "32898-04.htm";
                    }
            }
        }
        else if (npc.getNpcId() == NPC_IZSHAEL)
        {
            switch (st.getState())
            {
                case COMPLETED:
                    htmltext = "completed";
                    break;
                case STARTED:
                    switch (st.getCond())
                    {
                        case 1:
                            htmltext = "32894-00.htm";
                            break;
                        case 2:
                            htmltext = "32894-02.htm";
                            break;
                        case 5:
                            htmltext = "32894-03.htm";
                            break;
                        case 6:
                            htmltext = "32894-06.htm";
                        case 3:
                        case 4:
                    }
            }
        }
        else if (npc.getNpcId() == NPC_KES)
        {
            switch (st.getState())
            {
                case COMPLETED:
                    htmltext = "completed";
                    break;
                case STARTED:
                    if (st.getCond() != 2)
                    {
                        break;
                    }

                    htmltext = "32901-00.htm";
            }
        }
        else if (npc.getNpcId() == NPC_MASTER_KEI)
        {
            switch (st.getState())
            {
                case COMPLETED:
                    htmltext = "completed";
                    break;
                case STARTED:
                    if (st.getCond() != 3)
                    {
                        break;
                    }

                    htmltext = "32903-00.htm";
            }
        }
        else if (npc.getNpcId() == NPC_KITT)
        {
            switch (st.getState())
            {
                case COMPLETED:
                    htmltext = "completed";
                    break;
                case STARTED:
                    if (st.getCond() != 4)
                    {
                        break;
                    }

                    htmltext = "32902-00.htm";
            }
        }

        return htmltext;
    }

    @Override
    public void onLoad()
    {
    }

    @Override
    public void onReload()
    {
    }

    @Override
    public void onShutdown()
    {
    }
}
