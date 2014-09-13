package l2next.gameserver.utils;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.database.mysql;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.MenteeInfo;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.World;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.mail.Mail;
import l2next.gameserver.network.serverpackets.ExNoticePostArrived;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.skills.effects.EffectTemplate;
import l2next.gameserver.stats.Env;
import l2next.gameserver.tables.SkillTable;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Andrey A.
 * <p/>
 * Date: 02.11.12
 * <p/>
 * Time: 12:35
 */
public class MentorUtil
{
	public static final Map<Integer, Integer> SIGN_OF_TUTOR = new HashMap<Integer, Integer>()
	{
		{
			put(10, 1);
			put(20, 25);
			put(30, 30);
			put(40, 63);
			put(50, 68);
			put(51, 16);
			put(52, 7);
			put(53, 9);
			put(54, 11);
			put(55, 13);
			put(56, 16);
			put(57, 19);
			put(58, 23);
			put(59, 29);
			put(60, 37);
			put(61, 51);
			put(62, 20);
			put(63, 24);
			put(64, 30);
			put(65, 36);
			put(66, 44);
			put(67, 55);
			put(68, 67);
			put(69, 84);
			put(70, 107);
			put(71, 120);
			put(72, 92);
			put(73, 114);
			put(74, 139);
			put(75, 172);
			put(76, 213);
			put(77, 629);
			put(78, 322);
			put(79, 413);
			put(80, 491);
			put(81, 663);
			put(82, 746);
			put(83, 850);
			put(84, 987);
			put(85, 1149);
			put(86, 2015);
		}
	};

	public static int[] effectsForMentee = {
		9233,
		9227,
		9228,
		9229,
		9230,
		9231,
		9232
	}; // Buffs and skill SP+EXP(9233)
	public static int skillForMentee = 9379; // Summon mentor to Mentee
	public static int[] skillsForMentor = {
		9376,
		9377,
		9378
	}; // Active skills for mentor
	public static int effectForMentor = 9256; // Skill SP+EXP 10% for mentor
	public static int[] effectsRemove = {
		9233,
		9227,
		9228,
		9229,
		9230,
		9231,
		9232,
		9256
	}; // Removed effects from all
	// characters after end of
	// mentoring
	public static int[] skillRemove = {
		9376,
		9377,
		9378,
		9379
	};

	public static void applyMentoringConditions(Player activeChar)
	{
		if(activeChar.isMentor())
		{
			addEffectToPlayer(SkillTable.getInstance().getInfo(effectForMentor, 1), activeChar);
			/**
			 * При заходе ментора, бафаем всех учеников которые онлайн
			 */
			for(MenteeInfo menteeInfo : activeChar.getMentorSystem().getMenteeInfo())
			{
				if(menteeInfo.isOnline())
				{
					Player mentee = World.getPlayer(menteeInfo.getObjectId());
					for(int effect : effectsForMentee)
					{
						addEffectToPlayer(SkillTable.getInstance().getInfo(effect, 1), mentee);
					}
				}
			}
		}
		else
		{
			for(int effect : effectsForMentee)
			{
				addEffectToPlayer(SkillTable.getInstance().getInfo(effect, 1), activeChar);
			}
			if(World.getPlayer(activeChar.getMentorSystem().getMentor()) != null)
			{
				addEffectToPlayer(SkillTable.getInstance().getInfo(effectForMentor, 1), World.getPlayer(activeChar.getMentorSystem().getMentor()));
			}
		}
	}

	public static void addEffectToPlayer(Skill skill, Player target)
	{
		for(EffectTemplate et : skill.getEffectTemplates())
		{
			Env env = new Env(target, target, skill);
			Effect effect = et.getEffect(env);
			target.getEffectList().addEffect(effect);
		}
	}

	public static void addSkillsToMentor(Player mentor)
	{
		// Checking exist other mentors
		if(mentor.getMentorSystem().getMentor() == 0)
		{
			for(int skillId : skillsForMentor)
			{
				Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
				mentor.addSkill(skill, true);
				mentor.sendSkillList();
			}
		}
	}

	public static void addSkillsToMentee(Player mentee)
	{
		if(mentee.getMentorSystem().getMentor() != 0)
		{
			Skill skill = SkillTable.getInstance().getInfo(skillForMentee, 1);
			mentee.addSkill(skill, true);
			mentee.sendSkillList();
		}
	}

	public static void removeConditions(Player player)
	{
		if(player.isMentor())
		{
			removeConditionsFromMentee(player);
			removeEffectsFromPlayer(player);
		}
		else if(!player.isMentor() && World.getPlayer(player.getMentorSystem().getMentor()) != null)
		{
			removeEffectsFromPlayer(player);
			Player mentor = World.getPlayer(player.getMentorSystem().getMentor());
			int menteeOnline = 0;
			for(MenteeInfo mentee : mentor.getMentorSystem().getMenteeInfo())
			{
				if(mentee.isOnline())
				{
					menteeOnline++;
				}
			}
			if(menteeOnline < 1)
			{
				removeEffectsFromPlayer(mentor);
			}
		}

	}

	public static void removeConditionsFromMentee(Player player)
	{
		for(MenteeInfo mentee : player.getMentorSystem().getMenteeInfo())
		{
			Player activeChar = World.getPlayer(mentee.getObjectId());
			if(activeChar != null)
			{
				removeEffectsFromPlayer(activeChar);
			}
		}
	}

	public static void removeEffectsFromPlayer(Player activeChar)
	{
		for(int buff : effectsRemove)
		{
			activeChar.getEffectList().stopEffect(buff);
		}
	}

	public static void removeSkills(Player activeChar)
	{
		for(int skillToRemove : skillRemove)
		{
			Skill skill = SkillTable.getInstance().getInfo(skillToRemove, 1);
			activeChar.removeSkill(skill);
		}
	}

	public static void setTimePenalty(int mentorId, long timeTo, long expirationTime)
	{
		Player mentor = World.getPlayer(mentorId);
		if(mentor != null && mentor.isOnline())
		{
			mentor.setVar("mentorPenalty", timeTo, -1);
		}
		else
		{
			mysql.set("REPLACE INTO character_variables (obj_id, type, name, value, expire_time) VALUES (?,'user-var','mentorPenalty',?,?)", mentorId, timeTo, expirationTime);
		}
	}

	public static void sendMentorMail(Player receiver, Map<Integer, Long> items)
	{
		if(receiver == null || !receiver.isOnline())
		{
			return;
		}
		if(items.keySet().size() > 8)
		{
			return;
		}

		Mail mail = new Mail();
		mail.setSenderId(1);
		mail.setSenderName(new CustomMessage("Mentor.npc", receiver).toString());
		mail.setReceiverId(receiver.getObjectId());
		mail.setReceiverName(receiver.getName());
		mail.setTopic(new CustomMessage("Mentor.title", receiver).toString());
		mail.setBody(new CustomMessage("Mentor.text", receiver).addNumber(receiver.getLevel()).addString(receiver.getName()).toString());
		for(Map.Entry<Integer, Long> itm : items.entrySet())
		{
			ItemInstance item = ItemFunctions.createItem(itm.getKey());
			item.setLocation(ItemInstance.ItemLocation.MAIL);
			item.setCount(itm.getValue());
			item.save();
			mail.addAttachment(item);
		}
		mail.setType(Mail.SenderType.MENTOR);
		mail.setUnread(true);
		mail.setExpireTime(720 * 3600 + (int) (System.currentTimeMillis() / 1000L));
		mail.save();

		receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
		receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
	}
}
