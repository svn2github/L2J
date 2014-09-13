package l2next.gameserver.listener.actor.player.impl;

import l2next.commons.lang.reference.HardReference;
import l2next.gameserver.listener.actor.player.OnAnswerListener;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.model.base.TeamType;

public class MentorAnswerListener implements OnAnswerListener
{
	private HardReference<Player> _playerRef;
	private String _mentee;

	public MentorAnswerListener(Player mentor, String mentee)
	{
		_playerRef = mentor.getRef();
		_mentee = mentee;
	}

	@Override
	public void sayYes()
	{
		Player player = _playerRef.get();
		if(player == null)
		{
			return;
		}

		if(player.isDead() || !player.getReflection().isDefault() || player.isInOlympiadMode() || player.isInObserverMode() || player.isTeleporting() || player.getTeam() != TeamType.NONE)
		{
			return;
		}

		player.teleToLocation(World.getPlayer(_mentee).getLoc());
	}

	@Override
	public void sayNo()
	{

	}
}
