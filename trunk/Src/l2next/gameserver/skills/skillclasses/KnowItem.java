package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.commons.util.Rnd;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.templates.StatsSet;

import java.util.StringTokenizer;

/**
 * @author KilRoy Used for knowing unidentified item(Skill+itemType)
 * @param _bound_item
 *            - Bounded items(and random special skill)
 * @param _unidentified_item
 *            - Removed items
 * @param _blessed_item
 *            - proffit
 * @param _normal_item
 *            - proffit
 */
public class KnowItem extends Skill
{

	public KnowItem(StatsSet set)
	{
		super(set);

		StringTokenizer items = new StringTokenizer((set.getString("bound_item", "")), ";");
		if(items.hasMoreTokens())
		{
			try
			{
				int first = Integer.parseInt(items.nextToken());
				int second = Integer.parseInt(items.nextToken());
				int third = Integer.parseInt(items.nextToken());
				if(Rnd.chance(50))
				{
					_bound_item = first;
				}
				else if(Rnd.chance(50) & second != 0)
				{
					_bound_item = second;
				}
				else if(Rnd.chance(50) & third != 0)
				{
					_bound_item = third;
				}
				else
				{
					_bound_item = first;
				}
			}
			catch(Exception e)
			{
				_bound_item = set.getInteger("bound_item", 0);
			}
		}

		_blessed_item = set.getInteger("blessed_item", 0);
		_normal_item = set.getInteger("normal_item", 0);
		_unidentified_item = set.getInteger("unidentified_item", 0);
	}

	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if(target == null)
		{
			return false;
		}

		Player player = activeChar.getPlayer();

		if(player.getInventory().getItemByItemId(_unidentified_item) == null)
		{
			return false;
		}

		if(player.getWeightPenalty() >= 3 || player.getInventory().getSize() > player.getInventoryLimit() - 10)
		{
			player.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
			return false;
		}

		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		Player player = activeChar.getPlayer();

		if(player == null)
		{
			return;
		}

		player.getInventory().destroyItemByItemId(_unidentified_item, 1);
		player.sendPacket(new SystemMessage(SystemMessage.S2_S1_HAS_DISAPPEARED).addItemName(_unidentified_item));

		if(_bound_item != 0 && _blessed_item != 0 && _normal_item != 0)
		{ // For R R95 R99 Grade
			if(Rnd.chance(90))
			{
				player.getInventory().addItem(_bound_item, 1);
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_bound_item));
			}
			else if(Rnd.chance(3))
			{
				player.getInventory().addItem(_blessed_item, 1);
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_blessed_item));
			}
			else
			{
				player.getInventory().addItem(_normal_item, 1);
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_normal_item));
			}
		}
		else if(_bound_item != 0 && _normal_item != 0 && _blessed_item == 0)
		{ // For D C B A S Grade
			if(Rnd.chance(90))
			{
				player.getInventory().addItem(_bound_item, 1);
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_bound_item));
			}
			else
			{
				player.getInventory().addItem(_normal_item, 1);
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1).addItemName(_normal_item));
			}
		}
		else
		{
			player.sendMessage("This item is not working now, please talk administrator item ID: .");
		}
	}
}
