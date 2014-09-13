package l2next.gameserver.templates.npc;

import gnu.trove.set.hash.TIntHashSet;

/**
 * @author VISTALL
 * @date 23:48/15.12.2010
 */
public class AbsorbInfo
{
	public static enum AbsorbType
	{
		LAST_HIT, PARTY_ONE, PARTY_ALL, PARTY_RANDOM
	}

	private final boolean _skill;
	private final AbsorbType _absorbType;
	private final int _chance;
	private final int _cursedChance;
	private final TIntHashSet _levels;

	public AbsorbInfo(boolean skill, AbsorbType absorbType, int chance, int cursedChance, int min, int max)
	{
		_skill = skill;
		_absorbType = absorbType;
		_chance = chance;
		_cursedChance = cursedChance;
		_levels = new TIntHashSet(max - min);
		for(int i = min; i <= max; i++)
		{
			_levels.add(i);
		}
	}

	public boolean isSkill()
	{
		return _skill;
	}

	public AbsorbType getAbsorbType()
	{
		return _absorbType;
	}

	public int getChance()
	{
		return _chance;
	}

	public int getCursedChance()
	{
		return _cursedChance;
	}

	public boolean canAbsorb(int le)
	{
		return _levels.contains(le);
	}
}
