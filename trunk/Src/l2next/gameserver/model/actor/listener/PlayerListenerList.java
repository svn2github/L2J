package l2next.gameserver.model.actor.listener;

import l2next.commons.listener.Listener;
import l2next.gameserver.listener.actor.player.*;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.*;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.network.clientpackets.RequestActionUse.Action;

/**
 * @author G1ta0
 */
public class PlayerListenerList extends CharListenerList
{
	public PlayerListenerList(Player actor)
	{
		super(actor);
	}

	@Override
	public Player getActor()
	{
		return (Player) actor;
	}

	public void onEnter()
	{
		if(!global.getListeners().isEmpty())
		{
			for(Listener<Creature> listener : global.getListeners())
			{
				if(OnPlayerEnterListener.class.isInstance(listener))
				{
					((OnPlayerEnterListener) listener).onPlayerEnter(getActor());
				}
			}
		}

		if(!getListeners().isEmpty())
		{
			for(Listener<Creature> listener : getListeners())
			{
				if(OnPlayerEnterListener.class.isInstance(listener))
				{
					((OnPlayerEnterListener) listener).onPlayerEnter(getActor());
				}
			}
		}
	}

	public void onExit()
	{
		if(!global.getListeners().isEmpty())
		{
			for(Listener<Creature> listener : global.getListeners())
			{
				if(OnPlayerExitListener.class.isInstance(listener))
				{
					((OnPlayerExitListener) listener).onPlayerExit(getActor());
				}
			}
		}

		if(!getListeners().isEmpty())
		{
			for(Listener<Creature> listener : getListeners())
			{
				if(OnPlayerExitListener.class.isInstance(listener))
				{
					((OnPlayerExitListener) listener).onPlayerExit(getActor());
				}
			}
		}
	}

	public void onTeleport(int x, int y, int z, Reflection reflection)
	{
		if(!global.getListeners().isEmpty())
		{
			for(Listener<Creature> listener : global.getListeners())
			{
				if(OnTeleportListener.class.isInstance(listener))
				{
					((OnTeleportListener) listener).onTeleport(getActor(), x, y, z, reflection);
				}
			}
		}

		if(!getListeners().isEmpty())
		{
			for(Listener<Creature> listener : getListeners())
			{
				if(OnTeleportListener.class.isInstance(listener))
				{
					((OnTeleportListener) listener).onTeleport(getActor(), x, y, z, reflection);
				}
			}
		}
	}

	public void onPartyInvite()
	{
		if(!global.getListeners().isEmpty())
		{
			for(Listener<Creature> listener : global.getListeners())
			{
				if(OnPlayerPartyInviteListener.class.isInstance(listener))
				{
					((OnPlayerPartyInviteListener) listener).onPartyInvite(getActor());
				}
			}
		}

		if(!getListeners().isEmpty())
		{
			for(Listener<Creature> listener : getListeners())
			{
				if(OnPlayerPartyInviteListener.class.isInstance(listener))
				{
					((OnPlayerPartyInviteListener) listener).onPartyInvite(getActor());
				}
			}
		}
	}

	public void onPartyLeave()
	{
		if(!global.getListeners().isEmpty())
		{
			for(Listener<Creature> listener : global.getListeners())
			{
				if(OnPlayerPartyLeaveListener.class.isInstance(listener))
				{
					((OnPlayerPartyLeaveListener) listener).onPartyLeave(getActor());
				}
			}
		}

		if(!getListeners().isEmpty())
		{
			for(Listener<Creature> listener : getListeners())
			{
				if(OnPlayerPartyLeaveListener.class.isInstance(listener))
				{
					((OnPlayerPartyLeaveListener) listener).onPartyLeave(getActor());
				}
			}
		}
	}

	public void onLevelChange(int oldLvl, int newLvl)
	{
		if(!global.getListeners().isEmpty())
		{
			for(Listener<Creature> listener : global.getListeners())
			{
				if(OnLevelChangeListener.class.isInstance(listener))
				{
					((OnLevelChangeListener) listener).onLevelChange(getActor(), oldLvl, newLvl);
				}
			}
		}

		if(!getListeners().isEmpty())
		{
			for(Listener<Creature> listener : getListeners())
			{
				if(OnLevelChangeListener.class.isInstance(listener))
				{
					((OnLevelChangeListener) listener).onLevelChange(getActor(), oldLvl, newLvl);
				}
			}
		}
	}

	public void onSocialAction(Action action)
	{
		if(!global.getListeners().isEmpty())
		{
			for(Listener<Creature> listener : global.getListeners())
			{
				if(OnSocialActionListener.class.isInstance(listener))
				{
					((OnSocialActionListener) listener).onSocialAction(getActor(), getActor().getTarget(), action);
				}
			}
		}

		if(!getListeners().isEmpty())
		{
			for(Listener<Creature> listener : getListeners())
			{
				if(OnSocialActionListener.class.isInstance(listener))
				{
					((OnSocialActionListener) listener).onSocialAction(getActor(), getActor().getTarget(), action);
				}
			}
		}
	}
	
	public void onUseItem(ItemInstance item)
	{
		if (!global.getListeners().isEmpty())
		{	for (Listener<Creature> listener : global.getListeners())
			{
				if (OnUseItemListener.class.isInstance(listener))
				{
					((OnUseItemListener) listener).onUseItem(getActor(), getActor().getTarget(), item);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnUseItemListener.class.isInstance(listener))
				{
					((OnUseItemListener) listener).onUseItem(getActor(), getActor().getTarget(), item);
				}
			}
		}
	}
}
