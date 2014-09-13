package npc.model;

import instances.IsthinaExtreme;
import instances.IsthinaNormal;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.ItemFunctions;
import l2next.gameserver.utils.ReflectionUtils;

/**
 * @author KilRoy
 * Р”Р»СЏ СЂР°Р±РѕС‚С‹ СЃ РёРЅСЃС‚Р°РјРё - РёСЃС‚С…РёРЅС‹
 * РЈ РќРџРЎРѕРІ 33151, 33293 (Rumiese)
 * TODO[K] - Р Р°СЃРєРѕРјРµРЅС‚РёС‚СЊ РІСЃС‘ РµСЃР»Рё РґРѕРїРёС€Сѓ РёРЅСЃС‚+РђРё С…Р°СЂРґ РјРѕРґР° Р�СЃС‚С…РёРЅС‹ + РЎРѕРѕР±СЂР°Р·РёС‚СЊ РЅР°РіСЂР°РґСѓ РїСЂРё РµС‘ СЃРјРµСЂС‚Рё
 */
public final class RumieseNpcInstance extends NpcInstance
{
	private static final long serialVersionUID = 5984176213940365077L;
	private static final int normalIsthinaInstId = 169;
	private static final int hardIsthinaInstId = 170;

	public RumieseNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		if(command.equalsIgnoreCase("request_normalisthina"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(normalIsthinaInstId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(normalIsthinaInstId))
			{
				ReflectionUtils.enterReflection(player, new IsthinaNormal(), normalIsthinaInstId);
			}
		}
		else if(command.equalsIgnoreCase("request_hardisthina"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(player.canReenterInstance(170))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if(player.canEnterInstance(170))
			{
				ReflectionUtils.enterReflection(player, new IsthinaExtreme(), 170);
			}
		}
		else if(command.equalsIgnoreCase("request_takemyprize"))
		{
		}
		else if(command.equalsIgnoreCase("request_Device"))
		{
			if(ItemFunctions.getItemCount(player, 17608) > 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no.htm");
				return;
			}
			Functions.addItem(player, 17608, 1);
			showChatWindow(player, "default/" + getNpcId() + "-ok.htm");
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}