package ai;

import l2next.gameserver.ai.CtrlIntention;
import l2next.gameserver.ai.Fighter;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.utils.Location;

/**
* @Author Awakeninger
**/

public class IsleOfSoulBattleGolem extends Fighter
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
	
	

	public IsleOfSoulBattleGolem(NpcInstance actor)
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
				case 23246:
				case 23247:
				case 23248:
				case 23249:
				case 23250:
				case 23251:
				case 23252:
				case 23253:
				case 23254:
				case 19306:
				case 19307:
				case 19320:
				case 23262:
				case 23270:
				case 23271:
				case 23272:
				case 23273:
				case 23276:
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