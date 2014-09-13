package l2next.gameserver.utils;

import l2next.gameserver.cache.Msg;
import l2next.gameserver.data.xml.holder.SkillAcquireHolder;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.SkillLearn;
import l2next.gameserver.model.SubClass;
import l2next.gameserver.model.base.AcquireType;
import l2next.gameserver.model.base.ClassId;
import l2next.gameserver.model.base.ClassType2;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.CustomMessage;
import l2next.gameserver.scripts.Functions;

import java.util.Collection;

/**
 * @author VISTALL
 * @date 17:49/08.12.2010
 */
public class CertificationFunctions
{
	public static final String PATH = "villagemaster/certification/";

	public static void showCertificationList(NpcInstance npc, Player player)
	{
		if(!checkConditions(65, npc, player, true))
		{
			return;
		}

		Functions.show(PATH + "certificatelist.htm", player, npc);
	}

	public static void showDualCertificationList(NpcInstance npc, Player player)
	{
		if(!checkConditions(85, npc, player, true))
		{
			return;
		}

		Functions.show(PATH + "dualcertificatelist.htm", player, npc);
	}

	public static void getCertification65(NpcInstance npc, Player player)
	{
		if(!checkConditions(65, npc, player, false))
		{
			return;
		}

		SubClass clzz = player.getActiveSubClass();
		if(clzz.isCertificationGet(SubClass.CERTIFICATION_65))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		Functions.addItem(player, 10280, 1);
		clzz.addCertification(SubClass.CERTIFICATION_65);
		player.store(true);
	}

	public static void getCertification70(NpcInstance npc, Player player)
	{
		if(!checkConditions(70, npc, player, false))
		{
			return;
		}

		SubClass clzz = player.getActiveSubClass();

		// если не взят преведущий сертификат
		if(!clzz.isCertificationGet(SubClass.CERTIFICATION_65))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}

		if(clzz.isCertificationGet(SubClass.CERTIFICATION_70))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		Functions.addItem(player, 10280, 1);
		clzz.addCertification(SubClass.CERTIFICATION_70);
		player.store(true);
	}

	public static void getCertification75List(NpcInstance npc, Player player)
	{
		if(!checkConditions(75, npc, player, false))
		{
			return;
		}

		SubClass clzz = player.getActiveSubClass();

		// если не взят преведущий сертификат
		if(!clzz.isCertificationGet(SubClass.CERTIFICATION_65) || !clzz.isCertificationGet(SubClass.CERTIFICATION_70))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}

		if(clzz.isCertificationGet(SubClass.CERTIFICATION_75))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		//Functions.show(PATH + "certificate-choose.htm", player, npc);Awakeninger: Проверить, и если книжки старые то раскоментить, а что ниже удалить.
		Functions.addItem(player, 10280, 1);
		clzz.addCertification(SubClass.CERTIFICATION_75);
		player.store(true);
	}

	public static void getCertification75(NpcInstance npc, Player player, boolean classCertifi)
	{
		if(!checkConditions(75, npc, player, false))
		{
			return;
		}

		SubClass clzz = player.getActiveSubClass();

		// если не взят преведущий сертификат
		if(!clzz.isCertificationGet(SubClass.CERTIFICATION_65) || !clzz.isCertificationGet(SubClass.CERTIFICATION_70))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}

		if(clzz.isCertificationGet(SubClass.CERTIFICATION_75))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		if(classCertifi)
		{
			ClassId cl = ClassId.VALUES[clzz.getClassId()];
			if(cl.getType2() == null)
			{
				return;
			}

			Functions.addItem(player, cl.getType2().getCertificateId(), 1);
		}
		else
		{
			Functions.addItem(player, 10612, 1); // master ability
		}

		clzz.addCertification(SubClass.CERTIFICATION_75);
		player.store(true);
	}

	public static void getCertification80(NpcInstance npc, Player player)
	{
		if(!checkConditions(80, npc, player, false))
		{
			return;
		}

		SubClass clzz = player.getActiveSubClass();

		// если не взят(ы) преведущий сертификат(ы)
		if(!clzz.isCertificationGet(SubClass.CERTIFICATION_65) || !clzz.isCertificationGet(SubClass.CERTIFICATION_70) || !clzz.isCertificationGet(SubClass.CERTIFICATION_75))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}

		if(clzz.isCertificationGet(SubClass.CERTIFICATION_80))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		ClassId cl = ClassId.VALUES[clzz.getClassId()];
		if(cl.getType2() == null)
		{
			return;
		}
		Functions.addItem(player, 10280, 1);//Проверить
		//Functions.addItem(player, cl.getType2().getTransformationId(), 1);
		clzz.addCertification(SubClass.CERTIFICATION_80);
		player.store(true);

	}

	//===================================================ДуалклассStart=================================================================================
	public static void getCertification85(NpcInstance npc, Player player)
	{
		if(!checkConditions(85, npc, player, false))
		{
			return;
		}

		SubClass clzz = player.getActiveSubClass();
		if(clzz.isCertificationGet(SubClass.CERTIFICATION_85))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		Functions.addItem(player, 36078, 1);
		clzz.addCertification(SubClass.CERTIFICATION_85);
		player.store(true);
	}

	public static void getCertification90(NpcInstance npc, Player player)
	{
		if(!checkConditions(90, npc, player, false))
		{
			return;
		}

		SubClass clzz = player.getActiveSubClass();
		if(!clzz.isCertificationGet(SubClass.CERTIFICATION_85))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}

		if(clzz.isCertificationGet(SubClass.CERTIFICATION_90))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		Functions.addItem(player, 36078, 1);
		clzz.addCertification(SubClass.CERTIFICATION_90);
		player.store(true);
	}

	public static void getCertification95(NpcInstance npc, Player player)
	{
		if(!checkConditions(95, npc, player, false))
		{
			return;
		}
		SubClass clzz = player.getActiveSubClass();
		if(!clzz.isCertificationGet(SubClass.CERTIFICATION_90) || !clzz.isCertificationGet(SubClass.CERTIFICATION_85))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}

		//SubClass clzz = player.getActiveSubClass();
		if(clzz.isCertificationGet(SubClass.CERTIFICATION_95))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		Functions.addItem(player, 36078, 1);
		clzz.addCertification(SubClass.CERTIFICATION_95);
		player.store(true);
	}

	public static void getCertification99(NpcInstance npc, Player player)
	{
		if(!checkConditions(99, npc, player, false))
		{
			return;
		}
		SubClass clzz = player.getActiveSubClass();
		if(!clzz.isCertificationGet(SubClass.CERTIFICATION_90) || !clzz.isCertificationGet(SubClass.CERTIFICATION_85) || !clzz.isCertificationGet(SubClass.CERTIFICATION_95))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}

		//SubClass clzz = player.getActiveSubClass();
		if(clzz.isCertificationGet(SubClass.CERTIFICATION_99))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}

		Functions.addItem(player, 36078, 1);
		clzz.addCertification(SubClass.CERTIFICATION_99);
		player.store(true);
	}

	public static void cancelDualCertification(NpcInstance npc, Player player)
	{
		if(player.getInventory().getAdena() < 10000000)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		if(!player.getActiveSubClass().isBase())
		{
			return;
		}

		player.getInventory().reduceAdena(10000000);

		for(ClassType2 classType2 : ClassType2.VALUES)
		{
			player.getInventory().destroyItemByItemId(classType2.getCertificateId(), player.getInventory().getCountOf(classType2.getCertificateId()));
			player.getInventory().destroyItemByItemId(classType2.getTransformationId(), player.getInventory().getCountOf(classType2.getTransformationId()));
		}

		Collection<SkillLearn> skillLearnList = SkillAcquireHolder.getInstance().getAvailableSkills(null, AcquireType.CERTIFICATION_DOUBLE);
		for(SkillLearn learn : skillLearnList)
		{
			Skill skill = player.getKnownSkill(learn.getId());
			if(skill != null)
			{
				player.removeSkill(skill, true);
			}
		}

		for(SubClass subClass : player.getSubClassList().values())
		{
			if(!subClass.isBase())
			{
				subClass.setCertification(0);
			}
		}

		player.sendSkillList();
		Functions.show(new CustomMessage("scripts.services.SubclassSkills.SkillsDeleted", player), player);
	}

	public static void cancelCertification(NpcInstance npc, Player player)
	{
		if(player.getInventory().getAdena() < 10000000)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}

		if(!player.getActiveSubClass().isBase())
		{
			return;
		}

		player.getInventory().reduceAdena(10000000);

		for(ClassType2 classType2 : ClassType2.VALUES)
		{
			player.getInventory().destroyItemByItemId(classType2.getCertificateId(), player.getInventory().getCountOf(classType2.getCertificateId()));
			player.getInventory().destroyItemByItemId(classType2.getTransformationId(), player.getInventory().getCountOf(classType2.getTransformationId()));
		}

		Collection<SkillLearn> skillLearnList = SkillAcquireHolder.getInstance().getAvailableSkills(null, AcquireType.CERTIFICATION);
		for(SkillLearn learn : skillLearnList)
		{
			Skill skill = player.getKnownSkill(learn.getId());
			if(skill != null)
			{
				player.removeSkill(skill, true);
			}
		}

		for(SubClass subClass : player.getSubClassList().values())
		{
			if(!subClass.isBase())
			{
				subClass.setCertification(0);
			}
		}

		player.sendSkillList();
		Functions.show(new CustomMessage("scripts.services.SubclassSkills.SkillsDeleted", player), player);
	}

	public static boolean checkConditions(int level, NpcInstance npc, Player player, boolean first)
	{
		if(player.getLevel() < level)
		{
			Functions.show(PATH + "certificate-nolevel.htm", player, npc, "%level%", level);
			return false;
		}

		if(player.getActiveSubClass().isBase())
		{
			Functions.show(PATH + "certificate-nosub.htm", player, npc);
			return false;
		}

		if(first)
		{
			return true;
		}

		for(ClassType2 type : ClassType2.VALUES)
		{
			if(player.getInventory().getCountOf(type.getCertificateId()) > 3)
			{
				Functions.show(PATH + "certificate-already.htm", player, npc);
				return false;
			}
		}

		return true;
	}
}
