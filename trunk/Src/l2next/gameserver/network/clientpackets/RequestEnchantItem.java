package l2next.gameserver.network.clientpackets;

import l2next.commons.dao.JdbcEntityState;
import l2next.commons.util.Rnd;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.EnchantItemHolder;
import l2next.gameserver.instancemanager.WorldStatisticsManager;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.items.PcInventory;
import l2next.gameserver.model.items.etcitems.EnchantScroll;
import l2next.gameserver.model.worldstatistics.CategoryType;
import l2next.gameserver.network.serverpackets.EnchantResult;
import l2next.gameserver.network.serverpackets.InventoryUpdate;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.ItemFunctions;

//import l2next.gameserver.model.items.etcitems.EnchantScrollManager;

public class RequestEnchantItem extends AbstractEnchantPacket
{
	private int _objectId;
	private int _catalystObjId;

	@Override
	public void readImpl()
	{
		this._objectId = readD();
		this._catalystObjId = readD();
	}

	@Override
	public void runImpl()
	{
		Player player = getClient().getActiveChar();

		if(player == null)
		{
			return;
		}

		if(!isValidPlayer(player))
		{
			player.setEnchantScroll(null);
			player.sendPacket(EnchantResult.CANCEL);
			player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
			player.sendActionFailed();
		}

		PcInventory inventory = player.getInventory();
		inventory.writeLock();
		try
		{
			ItemInstance item = inventory.getItemByObjectId(this._objectId);
			ItemInstance scroll = player.getEnchantScroll();
			ItemInstance catalyst = this._catalystObjId > 0 ? inventory.getItemByObjectId(this._catalystObjId) : null;

			if(!ItemFunctions.checkCatalyst(item, catalyst))
			{
				catalyst = null;
			}

			if((item == null) || (scroll == null))
			{
				player.sendActionFailed();
				return;
			}

			EnchantScroll esi = EnchantItemHolder.getInstance().getEnchantScroll(scroll.getItemId());
			if(esi == null)
			{
				player.sendActionFailed();
				return;
			}

			if(item.getEnchantLevel() >= esi.getMax() || item.getEnchantLevel() < esi.getMin())
			{
				player.sendPacket(EnchantResult.CANCEL);
				player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
				player.sendActionFailed();
				return;
			}

			if(!checkItem(item, esi))
			{
				player.sendPacket(EnchantResult.CANCEL);
				player.sendPacket(SystemMsg.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
				player.sendActionFailed();
				return;
			}

			if((!inventory.destroyItem(scroll, 1L)) || ((catalyst != null) && (!inventory.destroyItem(catalyst, 1L))))
			{
				player.sendPacket(EnchantResult.CANCEL);
				player.sendActionFailed();
				return;
			}

			if(item.getVisualId() > 0)
			{
				item.setVisualId(0);
			}

			boolean equipped = false;
			if((equipped = item.isEquipped()))
			{
				inventory.isRefresh = true;
				inventory.unEquipItem(item);
			}

			int safeEnchantLevel = item.getTemplate().getBodyPart() == 32768 ? (esi.getSafe() + 1) : esi.getSafe();

			int chance = esi.getChance();

			if(catalyst != null)
			{
				chance += ItemFunctions.getCatalystPower(catalyst.getItemId());
			}

			if(esi.getType() == EnchantScroll.ScrollType.ANCIENT || esi.getType() == EnchantScroll.ScrollType.ITEM_MALL)
			{
				chance += 10;
			}

			if(esi.getType() == EnchantScroll.ScrollType.DIVINE)
			{
				chance = 100;
			}

			if(item.getEnchantLevel() <= safeEnchantLevel)
			{
				chance = 100;
			}

			chance = Math.min(chance, 100);

			if(item.isArmor())
			{
				WorldStatisticsManager.getInstance().updateStat(player, CategoryType.ARMOR_ENCHANT_TRY, item.getCrystalType().ordinal(), item.getEnchantLevel() + 1);
			}
			else if(item.isWeapon())
			{
				WorldStatisticsManager.getInstance().updateStat(player, CategoryType.WEAPON_ENCHANT_TRY, item.getCrystalType().ordinal(), item.getEnchantLevel() + 1);
			}

			if(Rnd.chance(chance))
			{
				item.setEnchantLevel(item.getEnchantLevel() + 1);
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();

				if(equipped)
				{
					inventory.equipItem(item);
					inventory.isRefresh = false;
				}

				player.sendPacket(new InventoryUpdate().addModifiedItem(item));

				if(item.isArmor())
				{
					WorldStatisticsManager.getInstance().updateStat(player, CategoryType.ARMOR_ENCHANT_MAX, item.getCrystalType().ordinal(), item.getEnchantLevel());
				}

				if(item.isWeapon())

				{
					WorldStatisticsManager.getInstance().updateStat(player, CategoryType.WEAPON_ENCHANT_MAX, item.getCrystalType().ordinal(), item.getEnchantLevel());
				}

				player.sendPacket(new EnchantResult(0, 0, 0L, item.getEnchantLevel()));

				if(Config.SHOW_ENCHANT_EFFECT_RESULT)
				{
					player.broadcastPacket(new L2GameServerPacket[]{new SystemMessage(3013).addName(player).addNumber(item.getEnchantLevel()).addItemName(item.getItemId())});
					player.broadcastPacket(new L2GameServerPacket[]{new MagicSkillUse(player, player, 5965, 1, 500, 1500L)});
				}
			}
			else
			{
				switch(esi.getType())
				{
					case NORMAL:
						if(item.isEquipped())
						{
							player.sendDisarmMessage(item);
						}
						if(!inventory.destroyItem(item, 1L))
						{
							player.sendActionFailed();
							return;
						}
						int crystalId = item.getCrystalType().cry;
						if((crystalId > 0) && (item.getTemplate().getCrystalCount() > 0))
						{
							int crystalAmount = (int) (item.getTemplate().getCrystalCount() * 0.87D);
							if(item.getEnchantLevel() > 3)
							{
								crystalAmount = (int) (crystalAmount + item.getTemplate().getCrystalCount() * 0.25D * (item.getEnchantLevel() - 3));
							}
							if(crystalAmount < 1)
							{
								crystalAmount = 1;
							}
							player.sendPacket(new EnchantResult(1, crystalId, crystalAmount));
							ItemFunctions.addItem(player, crystalId, crystalAmount, true);
						}
						else
						{
							player.sendPacket(EnchantResult.FAILED_NO_CRYSTALS);
						}
						break;
					case DESTRUCTION:
						item.setEnchantLevel(item.getEnchantLevel());
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();

						if(equipped)
						{
							inventory.equipItem(item);
							inventory.isRefresh = false;
						}

						player.sendPacket(new InventoryUpdate().addModifiedItem(item));
						player.sendPacket(SystemMsg.THE_BLESSED_ENCHANT_FAILED);
						player.sendPacket(EnchantResult.ANCIENT_FAILED);
						break;
					case BLESSED:
					case ITEM_MALL:
					case CRYSTAL:

						item.setEnchantLevel(0);
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();

						if(equipped)
						{
							inventory.equipItem(item);
							inventory.isRefresh = false;
						}

						player.sendPacket(new InventoryUpdate().addModifiedItem(item));
						player.sendPacket(SystemMsg.THE_BLESSED_ENCHANT_FAILED);
						player.sendPacket(EnchantResult.BLESSED_FAILED);
						break;
					case ANCIENT:
						player.sendPacket(EnchantResult.ANCIENT_FAILED);
				}
			}

		}
		finally
		{
			inventory.writeUnlock();

			player.setEnchantScroll(null);
			player.updateStats();
		}
	}
}