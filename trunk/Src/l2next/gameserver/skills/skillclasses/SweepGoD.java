package l2next.gameserver.skills.skillclasses;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.instances.MonsterInstance;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.reward.RewardItem;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.templates.StatsSet;
import l2next.gameserver.utils.ItemFunctions;

import java.util.List;

public class SweepGoD extends Skill
{
	public SweepGoD(StatsSet set)
	{
		super(set);
	}

	@Override
	public void useSkill(Creature activeChar, GArray<Creature> targets)
	{
		if(!activeChar.isPlayer())
		{
			return;
		}

		Player player = (Player) activeChar;

		for(Creature targ : targets)
		{

			MonsterInstance target = (MonsterInstance) targ;

			List<RewardItem> items = target.takeSweep();

			if(items == null)
			{
				activeChar.getAI().setAttackTarget(null);
				//target.endDecayTask();
				continue;
			}

			for(RewardItem item : items)
			{
				ItemInstance sweep = ItemFunctions.createItem(item.itemId);
				sweep.setCount(item.count);

				if(player.isInParty() && player.getParty().isDistributeSpoilLoot())
				{
					player.getParty().distributeItem(player, sweep, null);
					continue;
				}

				if(!player.getInventory().validateCapacity(sweep) || !player.getInventory().validateWeight(sweep))
				{
					sweep.dropToTheGround(player, target);
					continue;
				}

				player.getInventory().addItem(sweep);

				SystemMessage smsg;
				if(item.count == 1)
				{
					smsg = new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S1);
					smsg.addItemName(item.itemId);
					player.sendPacket(smsg);
				}
				else
				{
					smsg = new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S2_S1);
					smsg.addItemName(item.itemId);
					smsg.addNumber(item.count);
					player.sendPacket(smsg);
				}
				if(player.isInParty())
				{
					if(item.count == 1)
					{
						smsg = new SystemMessage(SystemMessage.S1_HAS_OBTAINED_S2_BY_USING_SWEEPER);
						smsg.addName(player);
						smsg.addItemName(item.itemId);
						player.getParty().broadcastToPartyMembers(player, smsg);
					}
					else
					{
						smsg = new SystemMessage(SystemMessage.S1_HAS_OBTAINED_3_S2_S_BY_USING_SWEEPER);
						smsg.addName(player);
						smsg.addItemName(item.itemId);
						smsg.addNumber(item.count);
						player.getParty().broadcastToPartyMembers(player, smsg);
					}
				}
			}

			activeChar.getAI().setAttackTarget(null);
			//target.endDecayTask();
		}
	}
}