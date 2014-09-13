/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2next.gameserver.skills.effects;

import gnu.trove.map.hash.TIntIntHashMap;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.network.serverpackets.ExAlterSkillRequest;
import l2next.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectHellBinding extends Effect
{
	/**
	 * Constructor for EffectParalyze.
	 * @param env Env
	 * @param template EffectTemplate
	 */

	private static TIntIntHashMap _ChainedAirSkills = new TIntIntHashMap(8);

	private static TIntIntHashMap _ChainedTemporalReplace = new TIntIntHashMap(8);

	public EffectHellBinding(Env env, EffectTemplate template)
	{
		super(env, template);
		_ChainedAirSkills.clear();
		_ChainedTemporalReplace.clear();
		_ChainedAirSkills.put(139, 10249);
		_ChainedAirSkills.put(140, 10262);
		_ChainedAirSkills.put(140, 10499);
		_ChainedAirSkills.put(141, 10749);
		_ChainedAirSkills.put(142, 10999);
		_ChainedAirSkills.put(143, 11247);
		_ChainedAirSkills.put(144, 11749);
		_ChainedAirSkills.put(145, 11499);
		_ChainedAirSkills.put(146, 11999);
		_ChainedTemporalReplace.put(10249, 10008);
		_ChainedTemporalReplace.put(10262, 10258);
		_ChainedTemporalReplace.put(10499, 10258);
		_ChainedTemporalReplace.put(10749, 10508);
		_ChainedTemporalReplace.put(10999, 10760); //Confirmed by lineage forum
		_ChainedTemporalReplace.put(11247, 11017);
		_ChainedTemporalReplace.put(11749, 11509);
		_ChainedTemporalReplace.put(11499, 11263);
		_ChainedTemporalReplace.put(11999, 11814);
	}

	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if(_effected.isParalyzeImmune() || _effected.isAirBinded() || _effected.isKnockedDown())
		{
			return false;
		}
		return super.checkCondition();
	}

	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		for(Player playerNearEffected : World.getAroundPlayers(_effected, 1200, 400))
		{
			if(playerNearEffected.getTarget() == _effected && playerNearEffected.isAwaking())
			{
				int chainSkill = _ChainedAirSkills.get(playerNearEffected.getClassId().getId());
				int temporalReplaceSkill = _ChainedTemporalReplace.get(chainSkill);
				playerNearEffected.sendPacket(new ExAlterSkillRequest(chainSkill, temporalReplaceSkill, 5));
			}
		}
		if(!_effected.isAirBinded())
		{
			_effected.startAirbinding();
		}
		_effected.abortAttack(true, true);
		_effected.abortCast(true, true);
		_effected.startParalyzed();
		_effected.stopMove();
	}

	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		if(_effected.isAirBinded())
		{
			_effected.stopAirbinding();
		}
		_effected.stopParalyzed();
	}

	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
