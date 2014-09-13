package l2next.gameserver.network.clientpackets;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.base.EnchantSkillLearn;
import l2next.gameserver.network.serverpackets.ExEnchantSkillInfoDetail;
import l2next.gameserver.tables.SkillTreeTable;

public final class RequestExEnchantSkillInfoDetail extends L2GameClientPacket
{
	private static final int TYPE_NORMAL_ENCHANT = 0;
	private static final int TYPE_SAFE_ENCHANT = 1;
	private static final int TYPE_UNTRAIN_ENCHANT = 2;
	private static final int TYPE_CHANGE_ENCHANT = 3;
	private static final int TYPE_100_ENCHANT = 4;

	private int _unk1;
	private int _type;
	private int _skillId;
	private int _skillLvl;

	@Override
	protected void readImpl()
	{
		_type = readD();
		_skillId = readD();
		_skillLvl = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();

		if(activeChar == null)
		{
			return;
		}

		if(activeChar.getTransformation() != 0)
		{
			activeChar.sendMessage("You must leave transformation mode first.");
			return;
		}

		if(activeChar.getLevel() < 76 || activeChar.getClassLevel() < 4)
		{
			activeChar.sendMessage("You must have 3rd class change quest completed.");
			return;
		}

		int bookId = 0;
		int sp = 0;
		int adenaCount = 0;
		double spMult = SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER;

		EnchantSkillLearn esd = null;

		switch(_type)
		{
			case TYPE_NORMAL_ENCHANT:
				if(_skillId < 10000 & _skillLvl % 100 == 1)
				{
					bookId = SkillTreeTable.NORMAL_ENCHANT_BOOK;
				}
				else if(_skillId >= 10000)
				{
					bookId = SkillTreeTable.NEW_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
				break;
			case TYPE_SAFE_ENCHANT:
				if(_skillId < 10000)
				{
					bookId = SkillTreeTable.SAFE_ENCHANT_BOOK;
				}
				else
				{
					bookId = SkillTreeTable.NEW_SAFE_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
				spMult = SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
				break;
			case TYPE_UNTRAIN_ENCHANT:
				if(_skillId < 10000)
				{
					bookId = SkillTreeTable.UNTRAIN_ENCHANT_BOOK;
				}
				else
				{
					bookId = SkillTreeTable.UNTRAIN_NEW_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl + 1);
				break;
			case TYPE_CHANGE_ENCHANT:
				if(_skillId < 10000)
				{
					bookId = SkillTreeTable.CHANGE_ENCHANT_BOOK;
				}
				else
				{
					bookId = SkillTreeTable.NEW_CHANGE_ENCHANT_BOOK;
				}
				esd = SkillTreeTable.getEnchantsForChange(_skillId, _skillLvl).get(0);
				spMult = 1f / SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
				break;
			case TYPE_100_ENCHANT:
				if(_skillId < 10000)
				{
					bookId = SkillTreeTable.ENCHANT_BOOK_LINDVIOR;
				}
				else
				{
					bookId = SkillTreeTable.ENCHANT_BOOK_LINDVIOR;
				}
				esd = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
				spMult = SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
				break;
		}

		if(esd == null)
		{
			return;
		}

		spMult *= esd.getCostMult();
		int[] cost = esd.getCost();

		sp = (int) (cost[1] * spMult);

		if(_type != TYPE_UNTRAIN_ENCHANT)
		{
			adenaCount = (int) (cost[0] * spMult);
		}

		if(_type == TYPE_100_ENCHANT)
		{
			// send skill enchantment detail
			activeChar.sendPacket(new ExEnchantSkillInfoDetail(_type, _skillId, _skillLvl, sp, 100, bookId, adenaCount));
		}
		else
		{
			activeChar.sendPacket(new ExEnchantSkillInfoDetail(_type, _skillId, _skillLvl, sp, esd.getRate(activeChar), bookId, adenaCount));
		}
	}
}