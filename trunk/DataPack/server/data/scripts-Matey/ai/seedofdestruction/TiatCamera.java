package ai.seedofdestruction;

import l2next.gameserver.ai.DefaultAI;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.World;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.ExStartScenePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pchayka
 */
public class TiatCamera extends DefaultAI
{
	private List<Player> _players = new ArrayList<Player>();

	public TiatCamera(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
		actor.startDamageBlocked();
	}

	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		for(Player p : World.getAroundPlayers(actor, 300, 300))
		{
			if(!_players.contains(p))
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_TIAT_OPENING);
				_players.add(p);
			}
		}
		return true;
	}
}