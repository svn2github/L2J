package l2next.gameserver.listener.actor.player.impl;

import l2next.commons.lang.reference.HardReference;
import l2next.gameserver.listener.actor.player.OnAnswerListener;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.instances.PetInstance;

/**
 * @author VISTALL
 * @date 11:35/15.04.2011
 */
public class ReviveAnswerListener implements OnAnswerListener
{
	private HardReference<Player> _playerRef;
	private double _power;
	private boolean _forPet;

	public ReviveAnswerListener(Player player, double power, boolean forPet)
	{
		_playerRef = player.getRef();
		_forPet = forPet;
		_power = power;
	}

	@Override
	public void sayYes()
	{
		Player player = _playerRef.get();
		if(player == null)
		{
			return;
		}
		if(!player.isDead() && !_forPet || _forPet && player.getFirstPet() != null && !player.getFirstPet().isDead())
		{
			return;
		}
		if(!_forPet)
		{
			player.doRevive(_power);
		}
		else if(player.getFirstPet() != null)
		{
			((PetInstance) player.getFirstPet()).doRevive(_power);
		}
	}

	@Override
	public void sayNo()
	{

	}

	public double getPower()
	{
		return _power;
	}

	public boolean isForPet()
	{
		return _forPet;
	}
}
