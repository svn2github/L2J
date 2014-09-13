package l2next.gameserver.model.party;

import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Request;
import l2next.gameserver.model.Request.L2RequestType;
import l2next.gameserver.network.serverpackets.ExRegistPartySubstitute;
import l2next.gameserver.network.serverpackets.ExRegistWaitingSubstituteOk;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class PartySubstituteTask extends RunnableImpl
{
	@Override
	public void runImpl() throws Exception
	{
		ConcurrentMap<Player, Integer> _wPlayers = PartySubstitute.getInstance().getWaitingPlayer();
		Set<Player> _wPartys = PartySubstitute.getInstance().getWaitingParty();

		Set<Entry<Player, Integer>> sets = _wPlayers.entrySet();

		for(Entry<Player, Integer> e : sets)
		{
			Player p = e.getKey();

			if(e.getValue() > 4)
			{
				PartySubstitute.getInstance().removePlayerReplace(p);
				p.getParty().getPartyLeader().sendPacket(new ExRegistPartySubstitute(p.getObjectId(), ExRegistPartySubstitute.REGISTER_TIMEOUT));
				continue;
			}

			for(Player pp : _wPartys)
			{
				if(PartySubstitute.getInstance().isGoodPlayer(p, pp))
				{
					if(pp.isProcessingRequest())
					{
						continue;
					}

					new Request(L2RequestType.SUBSTITUTE, p, pp).setTimeout(10000L);
					pp.sendPacket(new ExRegistWaitingSubstituteOk(p.getParty(), p));

					break;
				}
			}
			PartySubstitute.getInstance().updatePlayerToReplace(p, e.getValue() + 1);
		}
	}

}
