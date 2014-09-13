package l2next.gameserver.network.clientpackets;

import l2next.commons.util.Rnd;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.base.EnchantSkillLearn;
import l2next.gameserver.network.serverpackets.ExEnchantSkillInfo;
import l2next.gameserver.network.serverpackets.ExEnchantSkillResult;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.tables.SkillTreeTable;
import l2next.gameserver.utils.Log;

public final class RequestExEnchantSkillRouteChange extends L2GameClientPacket
{
	private int _skillId;
	private int _skillLvl;

	@Override
	protected void readImpl()
	{
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

		EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(_skillId, _skillLvl);
		if(sl == null)
		{
			return;
		}

		int slevel = activeChar.getSkillDisplayLevel(_skillId);
		if(slevel == -1)
		{
			return;
		}

		if(slevel <= sl.getBaseLevel() || slevel % 100 != _skillLvl % 100)
		{
			return;
		}

		int[] cost = sl.getCost();
		int requiredSp = cost[1] * sl.getCostMult() / SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;
		int requiredAdena = cost[0] * sl.getCostMult() / SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER;

		if(activeChar.getSp() < requiredSp)
		{
			sendPacket(Msg.SP_REQUIRED_FOR_SKILL_ENCHANT_IS_INSUFFICIENT);
			return;
		}

		if(activeChar.getAdena() < requiredAdena)
		{
			sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		if(Functions.getItemCount(activeChar, SkillTreeTable.CHANGE_ENCHANT_BOOK) == 0)
		{
			activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
			return;
		}

		Functions.removeItem(activeChar, SkillTreeTable.CHANGE_ENCHANT_BOOK, 1);
		Functions.removeItem(activeChar, 57, requiredAdena);
		activeChar.addExpAndSp(0, -1 * requiredSp);

		int levelPenalty = Rnd.get(Math.min(4, _skillLvl % 100));

		_skillLvl -= levelPenalty;
		if(_skillLvl % 100 == 0)
		{
			_skillLvl = sl.getBaseLevel();
		}

		Skill skill = SkillTable.getInstance().getInfo(_skillId, SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel()));

		if(skill != null)
		{
			activeChar.addSkill(skill, true);
		}

		if(levelPenalty == 0)
		{
			SystemMessage sm = new SystemMessage(SystemMessage.Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_will_remain);
			sm.addSkillName(_skillId, _skillLvl);
			activeChar.sendPacket(sm);
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.Enchant_skill_route_change_was_successful_Lv_of_enchant_skill_S1_has_been_decreased_by_S2);
			sm.addSkillName(_skillId, _skillLvl);
			sm.addNumber(levelPenalty);
			activeChar.sendPacket(sm);
		}

		Log.add(activeChar.getName() + "|Successfully changed route|" + _skillId + "|" + slevel + "|to+" + _skillLvl + "|" + levelPenalty, "enchant_skills");

		activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, activeChar.getSkillDisplayLevel(_skillId)), new ExEnchantSkillResult(1));
		RequestExEnchantSkill.updateSkillShortcuts(activeChar, _skillId, _skillLvl);
	}
}