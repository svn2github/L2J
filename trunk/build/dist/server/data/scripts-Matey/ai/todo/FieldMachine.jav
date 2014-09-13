package ai;

import spirth5oh.commons.collections.GArray;
import spirth5oh.gameserver.ai.CtrlEvent;
import spirth5oh.gameserver.ai.DefaultAI;
import spirth5oh.gameserver.model.Creature;
import spirth5oh.gameserver.model.instances.NpcInstance;
import spirth5oh.gameserver.scripts.Functions;

public class FieldMachine extends DefaultAI {
    private long _lastAction;

    public FieldMachine(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        NpcInstance actor = getActor();
        if (attacker == null || attacker.getPlayer() == null)
            return;

        // Ругаемся не чаще, чем раз в 15 секунд
        if (System.currentTimeMillis() - _lastAction > 15000) {
            _lastAction = System.currentTimeMillis();
            Functions.npcSayCustomMessage(actor, "scripts.ai.FieldMachine." + actor.getNpcId());
            GArray<NpcInstance> around = actor.getAroundNpc(1500, 300);
            if (around != null && !around.isEmpty())
                for (NpcInstance npc : around)
                    if (npc.isMonster() && npc.getNpcId() >= 22656 && npc.getNpcId() <= 22659)
                        npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 5000);
        }
    }
}