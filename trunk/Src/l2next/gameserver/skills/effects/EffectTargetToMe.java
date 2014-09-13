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

import l2next.gameserver.model.Effect;
import l2next.gameserver.network.serverpackets.FlyToLocation;
import l2next.gameserver.network.serverpackets.ValidateLocation;
import l2next.gameserver.stats.Env;
import l2next.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectTargetToMe extends Effect
{
	/**
	 * Constructor for EffectTargetToMe.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectTargetToMe(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		Location flyLoc = _effected.getFlyLocation(getEffector(), getSkill());
		_effected.setLoc(flyLoc);
		_effected.broadcastPacket(new FlyToLocation(_effected, flyLoc, getSkill().getFlyType(), 0));
		_effected.broadcastPacket(new ValidateLocation(_effected));
	}

	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
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
