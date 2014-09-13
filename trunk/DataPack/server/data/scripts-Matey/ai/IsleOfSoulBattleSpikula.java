package ai;

import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;

/**
* @Author Awakeninger
**/

public class IsleOfSoulBattleSpikula extends Fighter
{
	private int Spik1 = 23246;
	private int Spik2 = 23247;
	private int Spik3 = 23248;
	private int Spik4 = 23249;
	private int Spik5 = 23250;
	private int Spik6 = 23251;
	private int InSpik1 = 23252;
	private int InSpik2 = 23253;
	private int InSpik3 = 23254;
	private int InSpikulaAdutant = 19306;
	private int InSpikulaLeader = 19307;
	private int InClone = 19320;
	private int EliteSpik = 23262;
	private int Pathroll = 23270;
	private int Archer = 23271;
	private int Warrior = 23272;
	private int SpikAr = 23273;
	private int MainScout = 23276;
	//One Side
	private int GolemTech = 19309;
	private int GolemSver = 19311;
	private int GolemKop = 19313;
	private int GolemDav = 19315;
	private int GolemSamorez = 19317;
	private int GolemSnaryad = 19318;
	private int GolemLight = 23255;
	private int GolemTyag = 23256;
	private int GolemDual = 23257;
	private int GolemGruz = 23258;
	private int GolemBroken = 23259;
	private int GolemSummon = 23260;
	private int GolemSuperBreak = 23261;
	private int BrokenGolemTech = 23263;
	private int BrokenGolemSver = 23264;
	private int BrokenGolemKop = 23265;
	private int BrokenGolemDav = 23266;
	private int GolemScout = 23268;
	private int GolemFighter = 23269;
	
	

	public IsleOfSoulBattleSpikula(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 5000;
	}

	@Override
	public boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();

		if(getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			actor.getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}

		return super.checkAggression(target);
	}

	@Override
	protected boolean thinkActive()
	{
        NpcInstance actor = getActor();
        doAttack(actor);

		return false;
	}

    private void doAttack(NpcInstance actor)
    {
        for(NpcInstance npc : actor.getAroundNpc(300,750))
        {
			switch(npc.getNpcId())
			{
				case 19309:
				case 19311:
				case 19313:
				case 19315:
				case 19317:
				case 19318:
				case 23255:
				case 23256:
				case 23257:
				case 23258:
				case 23259:
				case 23260:
				case 23261:
				case 23263:
				case 23264:
				case 23265:
				case 23266:
				case 23268:
				case 23269:
					actor.doAttack(npc);
			}
        }
    }
    
    @Override
    protected boolean randomWalk()
    {
        return false;
    }
}