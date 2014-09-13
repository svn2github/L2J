package l2next.gameserver.network.clientpackets;

import l2next.commons.util.Rnd;
import l2next.gameserver.cache.Msg;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.actor.instances.player.ShortCut;
import l2next.gameserver.model.base.EnchantSkillLearn;
import l2next.gameserver.network.serverpackets.ExEnchantSkillInfo;
import l2next.gameserver.network.serverpackets.ExEnchantSkillResult;
import l2next.gameserver.network.serverpackets.ShortCutRegister;
import l2next.gameserver.network.serverpackets.SkillList;
import l2next.gameserver.network.serverpackets.SystemMessage;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.tables.SkillTreeTable;
import l2next.gameserver.utils.Log;

/**
 * Format chdd c: (id) 0xD0 h: (subid) 0x0F d: skill id d: skill lvl
 */
public class RequestExEnchantSkill extends L2GameClientPacket
{
	private int _skillId;
	private int _skillLvl;
	private int _type;

	@Override
	protected void readImpl()
	{
		_type = readD(); // Awakeninger: по формату, тип
		_skillId = readD();
		_skillLvl = readD();
	}

	@Override
	protected void runImpl()
	{
		switch(_type)
		{
			case 0: //Обычная
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

				int slevel = activeChar.getSkillLevel(_skillId);
				if(slevel == -1)
				{
					return;
				}

				int enchantLevel = SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel());

				// already knows the skill with this level
				if(slevel >= enchantLevel)
				{
					return;
				}

				// Можем ли мы перейти с текущего уровня скилла на данную заточку
				if(slevel == sl.getBaseLevel() ? _skillLvl % 100 != 1 : slevel != enchantLevel - 1)
				{
					activeChar.sendMessage("Incorrect enchant level.");
					return;
				}

				Skill skill = SkillTable.getInstance().getInfo(_skillId, enchantLevel);
				if(skill == null)
				{
					activeChar.sendMessage("Internal error: not found skill level");
					return;
				}

				int[] cost = sl.getCost();
				int requiredSp = cost[1] * SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
				int requiredAdena = cost[0] * SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
				int rate = sl.getRate(activeChar);

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

				if(_skillId < 10000 && _skillLvl % 100 == 1) // only first lvl requires book (101, 201, 301...)
				{
					if(Functions.getItemCount(activeChar, SkillTreeTable.NORMAL_ENCHANT_BOOK) == 0)
					{
						activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
						return;
					}
					Functions.removeItem(activeChar, SkillTreeTable.NORMAL_ENCHANT_BOOK, 1);
				}
				else if(_skillId >= 10000 && _skillLvl % 100 == 1)
				{
					if(Functions.getItemCount(activeChar, SkillTreeTable.NEW_ENCHANT_BOOK) == 0)
					{
						activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
						return;
					}
					Functions.removeItem(activeChar, SkillTreeTable.NEW_ENCHANT_BOOK, 1);
				}

				if(Rnd.chance(rate))
				{
					activeChar.addExpAndSp(0, -1 * requiredSp);
					Functions.removeItem(activeChar, 57, requiredAdena);
					activeChar.sendPacket(new SystemMessage(SystemMessage.SP_HAS_DECREASED_BY_S1).addNumber(requiredSp), new SystemMessage(SystemMessage.SUCCEEDED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new SkillList(activeChar), new ExEnchantSkillResult(1));
					Log.add(activeChar.getName() + "|Successfully enchanted|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
				}
				else
				{
					skill = SkillTable.getInstance().getInfo(_skillId, sl.getBaseLevel());
					activeChar.sendPacket(new SystemMessage(SystemMessage.FAILED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(0));
					Log.add(activeChar.getName() + "|Failed to enchant|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
				}
				activeChar.addSkill(skill, true);
				updateSkillShortcuts(activeChar, _skillId, _skillLvl);
				activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, activeChar.getSkillDisplayLevel(_skillId)));
				break;
			}
			case 1: //Блесс
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

				int slevel = activeChar.getSkillLevel(_skillId);
				if(slevel == -1)
				{
					return;
				}

				int enchantLevel = SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel());

				// already knows the skill with this level
				if(slevel >= enchantLevel)
				{
					return;
				}

				// Можем ли мы перейти с текущего уровня скилла на данную заточку
				if(slevel == sl.getBaseLevel() ? _skillLvl % 100 != 1 : slevel != enchantLevel - 1)
				{
					activeChar.sendMessage("Incorrect enchant level.");
					return;
				}

				Skill skill = SkillTable.getInstance().getInfo(_skillId, enchantLevel);
				if(skill == null)
				{
					return;
				}

				int[] cost = sl.getCost();
				int requiredSp = cost[1] * SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
				int requiredAdena = cost[0] * SkillTreeTable.SAFE_ENCHANT_COST_MULTIPLIER * sl.getCostMult();

				int rate = sl.getRate(activeChar);

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

				if(_skillId < 10000)
				{
					if(Functions.getItemCount(activeChar, SkillTreeTable.SAFE_ENCHANT_BOOK) == 0)
					{
						activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
						return;
					}
					Functions.removeItem(activeChar, SkillTreeTable.SAFE_ENCHANT_BOOK, 1);
				}
				else if(_skillId >= 10000)
				{
					if(Functions.getItemCount(activeChar, SkillTreeTable.NEW_SAFE_ENCHANT_BOOK) == 0)
					{
						activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
						return;
					}
					Functions.removeItem(activeChar, SkillTreeTable.NEW_SAFE_ENCHANT_BOOK, 1);
				}

				if(Rnd.chance(rate))
				{
					activeChar.addSkill(skill, true);
					activeChar.addExpAndSp(0, -1 * requiredSp);
					Functions.removeItem(activeChar, 57, requiredAdena);
					activeChar.sendPacket(new SystemMessage(SystemMessage.SP_HAS_DECREASED_BY_S1).addNumber(requiredSp), new SystemMessage(SystemMessage.SUCCEEDED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(1));
					activeChar.sendSkillList();
					RequestExEnchantSkill.updateSkillShortcuts(activeChar, _skillId, _skillLvl);
					Log.add(activeChar.getName() + "|Successfully safe enchanted|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.Skill_enchant_failed_Current_level_of_enchant_skill_S1_will_remain_unchanged).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(0));
					Log.add(activeChar.getName() + "|Failed to safe enchant|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
				}

				activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, activeChar.getSkillDisplayLevel(_skillId)));
				break;
			}
			case 2: //Забвение(-1)
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

				int oldSkillLevel = activeChar.getSkillDisplayLevel(_skillId);
				if(oldSkillLevel == -1)
				{
					return;
				}

				if(_skillLvl != oldSkillLevel - 1 || _skillLvl / 100 != oldSkillLevel / 100)
				{
					return;
				}

				EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(_skillId, oldSkillLevel);
				if(sl == null)
				{
					return;
				}

				Skill newSkill;

				if(_skillLvl % 100 == 0)
				{
					_skillLvl = sl.getBaseLevel();
					newSkill = SkillTable.getInstance().getInfo(_skillId, _skillLvl);
				}
				else
				{
					newSkill = SkillTable.getInstance().getInfo(_skillId, SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel()));
				}

				if(newSkill == null)
				{
					return;
				}

				if(Functions.getItemCount(activeChar, SkillTreeTable.UNTRAIN_ENCHANT_BOOK) == 0)
				{
					activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
					return;
				}

				Functions.removeItem(activeChar, SkillTreeTable.UNTRAIN_ENCHANT_BOOK, 1);

				activeChar.addExpAndSp(0, sl.getCost()[1] * sl.getCostMult());
				activeChar.addSkill(newSkill, true);

				if(_skillLvl > 100)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.Untrain_of_enchant_skill_was_successful_Current_level_of_enchant_skill_S1_has_been_decreased_by_1);
					sm.addSkillName(_skillId, _skillLvl);
					activeChar.sendPacket(sm);
				}
				else
				{
					SystemMessage sm = new SystemMessage(SystemMessage.Untrain_of_enchant_skill_was_successful_Current_level_of_enchant_skill_S1_became_0_and_enchant_skill_will_be_initialized);
					sm.addSkillName(_skillId, _skillLvl);
					activeChar.sendPacket(sm);
				}

				Log.add(activeChar.getName() + "|Successfully untranes|" + _skillId + "|to+" + _skillLvl + "|---", "enchant_skills");

				activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, newSkill.getDisplayLevel()), ExEnchantSkillResult.SUCCESS, new SkillList(activeChar));
				RequestExEnchantSkill.updateSkillShortcuts(activeChar, _skillId, _skillLvl);
				break;
			}
			case 3: //Переьочка на другую ветку
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
				break;
			}
			case 4: //Новая точка линдвиора 100% TODO
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

				int slevel = activeChar.getSkillLevel(_skillId);
				if(slevel == -1)
				{
					return;
				}

				int enchantLevel = SkillTreeTable.convertEnchantLevel(sl.getBaseLevel(), _skillLvl, sl.getMaxLevel());

				// already knows the skill with this level
				if(slevel >= enchantLevel)
				{
					return;
				}

				// Можем ли мы перейти с текущего уровня скилла на данную заточку
				if(slevel == sl.getBaseLevel() ? _skillLvl % 100 != 1 : slevel != enchantLevel - 1)
				{
					activeChar.sendMessage("Incorrect enchant level.");
					return;
				}

				Skill skill = SkillTable.getInstance().getInfo(_skillId, enchantLevel);
				if(skill == null)
				{
					activeChar.sendMessage("Internal error: not found skill level");
					return;
				}

				int[] cost = sl.getCost();
				int requiredSp = cost[1] * SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
				int requiredAdena = cost[0] * SkillTreeTable.NORMAL_ENCHANT_COST_MULTIPLIER * sl.getCostMult();
				int rate = 100;

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

				if(_skillId < 10000 && _skillLvl % 100 == 1) // only first lvl requires book (101, 201, 301...)
				{
					if(Functions.getItemCount(activeChar, SkillTreeTable.ENCHANT_BOOK_LINDVIOR) == 0)
					{
						activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
						return;
					}
					Functions.removeItem(activeChar, SkillTreeTable.ENCHANT_BOOK_LINDVIOR, 1);
				}
				else if(_skillId >= 10000 && _skillLvl % 100 == 1)
				{
					if(Functions.getItemCount(activeChar, SkillTreeTable.ENCHANT_BOOK_LINDVIOR) == 0)
					{
						activeChar.sendPacket(Msg.ITEMS_REQUIRED_FOR_SKILL_ENCHANT_ARE_INSUFFICIENT);
						return;
					}
					Functions.removeItem(activeChar, SkillTreeTable.ENCHANT_BOOK_LINDVIOR, 1);
				}

				if(Rnd.chance(rate))
				{
					activeChar.addExpAndSp(0, -1 * requiredSp);
					Functions.removeItem(activeChar, 57, requiredAdena);
					activeChar.sendPacket(new SystemMessage(SystemMessage.SP_HAS_DECREASED_BY_S1).addNumber(requiredSp), new SystemMessage(SystemMessage.SUCCEEDED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new SkillList(activeChar), new ExEnchantSkillResult(1));
					Log.add(activeChar.getName() + "|Successfully enchanted|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
				}
				else
				{
					skill = SkillTable.getInstance().getInfo(_skillId, sl.getBaseLevel());
					activeChar.sendPacket(new SystemMessage(SystemMessage.FAILED_IN_ENCHANTING_SKILL_S1).addSkillName(_skillId, _skillLvl), new ExEnchantSkillResult(0));
					Log.add(activeChar.getName() + "|Failed to enchant|" + _skillId + "|to+" + _skillLvl + "|" + rate, "enchant_skills");
				}
				activeChar.addSkill(skill, true);
				updateSkillShortcuts(activeChar, _skillId, _skillLvl);
				activeChar.sendPacket(new ExEnchantSkillInfo(_skillId, activeChar.getSkillDisplayLevel(_skillId)));
				break;
			}
		}
	}

	protected static void updateSkillShortcuts(Player player, int skillId, int skillLevel)
	{
		for(ShortCut sc : player.getAllShortCuts())
		{
			if(sc.getId() == skillId && sc.getType() == ShortCut.TYPE_SKILL)
			{
				ShortCut newsc = new ShortCut(sc.getSlot(), sc.getPage(), sc.getType(), sc.getId(), skillLevel, 1);
				player.sendPacket(new ShortCutRegister(player, newsc));
				player.registerShortCut(newsc);
			}
		}
	}
}