package services.petevolve;

import l2next.commons.dao.JdbcEntityState;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Summon;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.tables.PetDataTable;
import l2next.gameserver.tables.PetDataTable.L2Pet;

/**
 * User: darkevil
 * Date: 02.06.2008
 * Time: 12:19:36
 */
public class fenrir extends Functions
{
	private static final int GREAT_WOLF = PetDataTable.GREAT_WOLF_ID;
	private static final int GREAT_WOLF_NECKLACE = L2Pet.GREAT_WOLF.getControlItemId();
	private static final int FENRIR_NECKLACE = L2Pet.FENRIR_WOLF.getControlItemId();

	public void evolve()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if(player == null || npc == null)
		{
			return;
		}
		if(player.getInventory().getItemByItemId(GREAT_WOLF_NECKLACE) == null)
		{
			show("scripts/services/petevolve/no_item.htm", player, npc);
			return;
		}
		Summon pl_pet = player.getFirstPet();
		if(pl_pet == null || pl_pet.isDead())
		{
			show("scripts/services/petevolve/evolve_no.htm", player, npc);
			return;
		}
		if(pl_pet.getNpcId() != GREAT_WOLF)
		{
			show("scripts/services/petevolve/no_wolf.htm", player, npc);
			return;
		}
		if(pl_pet.getLevel() < 70)
		{
			show("scripts/services/petevolve/no_level_gw.htm", player, npc);
			return;
		}

		int controlItemId = player.getFirstPet().getControlItemObjId();
		player.getFirstPet().unSummon();

		ItemInstance control = player.getInventory().getItemByObjectId(controlItemId);
		control.setItemId(FENRIR_NECKLACE);
		control.setEnchantLevel(L2Pet.FENRIR_WOLF.getMinLevel());
		control.setJdbcState(JdbcEntityState.UPDATED);
		control.update();
		player.sendItemList(false);

		show("scripts/services/petevolve/yes_wolf.htm", player, npc);
	}
}