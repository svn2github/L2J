package l2next.gameserver.model.instances;

import l2next.commons.lang.reference.HardReference;
import l2next.gameserver.idfactory.IdFactory;
import l2next.gameserver.model.GameObject;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.reference.L2Reference;
import l2next.gameserver.network.serverpackets.MyTargetSelected;

/**
 * @author VISTALL
 * @date 20:20/03.01.2011
 */
public class ControlKeyInstance extends GameObject
{
	/**
	 *
	 */
	private static final long serialVersionUID = -5006105090095518082L;
	protected HardReference<ControlKeyInstance> reference;

	public ControlKeyInstance()
	{
		super(IdFactory.getInstance().getNextId());
		reference = new L2Reference<ControlKeyInstance>(this);
	}

	@Override
	public HardReference<ControlKeyInstance> getRef()
	{
		return reference;
	}

	@Override
	public void onAction(Player player, boolean shift)
	{
		if(player.getTarget() != this)
		{
			player.setTarget(this);
			player.sendPacket(new MyTargetSelected(getObjectId(), 0));
			return;
		}

		player.sendActionFailed();
	}
}
