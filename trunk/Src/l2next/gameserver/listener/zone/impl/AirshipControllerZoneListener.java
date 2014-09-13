package l2next.gameserver.listener.zone.impl;

import l2next.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Zone;
import l2next.gameserver.model.entity.boat.ClanAirShip;
import l2next.gameserver.model.instances.ClanAirShipControllerInstance;

public class AirshipControllerZoneListener implements OnZoneEnterLeaveListener
{
	private ClanAirShipControllerInstance _controllerInstance;

	@Override
	public void onZoneEnter(Zone zone, Creature actor)
	{
		if(_controllerInstance == null && actor instanceof ClanAirShipControllerInstance)
		{
			_controllerInstance = (ClanAirShipControllerInstance) actor;
		}
		else if(actor.isClanAirShip())
		{
			_controllerInstance.setDockedShip((ClanAirShip) actor);
		}
	}

	@Override
	public void onZoneLeave(Zone zone, Creature actor)
	{
		if(actor.isClanAirShip())
		{
			_controllerInstance.setDockedShip(null);
		}
	}
}
