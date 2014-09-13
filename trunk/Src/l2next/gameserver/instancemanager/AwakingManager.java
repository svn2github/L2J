package l2next.gameserver.instancemanager;

import gnu.trove.map.hash.TIntIntHashMap;
import javolution.util.FastList;
import l2next.gameserver.Config;
import l2next.gameserver.data.xml.holder.SkillAcquireHolder;
import l2next.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.actor.listener.CharListenerList;
import l2next.gameserver.model.base.ClassLevel;
import l2next.gameserver.model.base.EnchantSkillLearn;
import l2next.gameserver.network.serverpackets.ExCallToChangeClass;
import l2next.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import l2next.gameserver.network.serverpackets.ExShowUsmVideo;
import l2next.gameserver.network.serverpackets.SocialAction;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.tables.SkillTreeTable;
import l2next.gameserver.utils.ItemFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author ALF
 * @data 04.02.2012
 */
public class AwakingManager implements OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(AwakingManager.class);
	private static AwakingManager _instance;

	private static TIntIntHashMap _CA = new TIntIntHashMap(34);
	private static final int ESSENCE_OF_THE_LESSER_GIANTS = 30306;

	private static final int[] count30T = {
		0,
		//0
		0,
		//1
		0,
		//2
		0,
		//3
		1,
		//4
		1,
		//5
		2,
		//6
		3,
		//7
		4,
		//8
		5,
		//9
		6,
		//10
		7,
		//11
		9,
		//12
		10,
		//13
		12,
		//14
		13,
		//15
		15,
		//16
		17,
		//17
		19,
		//18
		22,
		//19
		24,
		//20
		27,
		//21
		29,
		//22
		32,
		//23
		35,
		//24
		42,
		//25
		45,
		//26
		48,
		//27
		63,
		//28
		70,
		//29
		83
		//30
	};

	private static final int[] count30 = {
		0,
		//0
		0,
		//1
		0,
		//2
		0,
		//3
		1,
		//4
		1,
		//5
		1,
		//6
		1,
		//7
		2,
		//8
		2,
		//9
		2,
		//10
		3,
		//11
		3,
		//12
		3,
		//13
		4,
		//14
		4,
		//15
		5,
		//16
		6,
		//17
		6,
		//18
		7,
		//19
		8,
		//20
		9,
		//21
		9,
		//22
		10,
		//23
		11,
		//24
		13,
		//25
		14,
		//26
		15,
		//27
		19,
		//28
		21,
		//29
		25
		//30
	};

	private static final int[] count15T = {
		0,
		//0
		0,
		//1
		0,
		//2
		0,
		//3
		1,
		//4
		1,
		//5
		2,
		//6
		3,
		//7
		4,
		//8
		5,
		//9
		7,
		//10
		9,
		//11
		10,
		//12
		19,
		//13
		24,
		//14
		35
		//15
	};

	private static final int[] count15 = {
		0,
		//0
		0,
		//1
		0,
		//2
		0,
		//3
		1,
		//4
		1,
		//5
		1,
		//6
		1,
		//7
		2,
		//8
		2,
		//9
		3,
		//10
		3,
		//11
		3,
		//12
		6,
		//13
		8,
		//14
		11
		//15
	};

	/**
	 * ************************************************************************* ***************** 139 Рыцарь Савуло: Рыцарь Феникса, Рыцарь Ада,
	 * Храмовник Евы, Храмовник Шилен. 140 Воин Тейваза: Полководец, Дуэлист, Титан, Аватар, Мастер, Каратель. 141 Разбойник Отилы: Авантюрист,
	 * Странник Ветра, Призрачный Охотник, Кладоискатель. 142 Лучник Эйваза: Снайпер, Страж Лунного Света, Страж Теней, Диверсант. 143 Волшебник Фео:
	 * Archmahe, Soultaker, Mystic muse, StormScreamer, SoulHound 144 Ис Заклинатель: Hierophant, Doomcryer, Dominator, Sword Muse, Spectral Dancer,
	 * Judicator 145 Призыватель Веньо: Arcana Lord, Elemental master, Spectral Master 146 Целитель Альгиза: Cardinal, Eva’s Saint, Shilien saint
	 * ******************************************************************** ************************
	 */

	public void load()
	{

		if(Config.AWAKING_FREE)
		{
			CharListenerList.addGlobal(this);
		}

		_CA.clear();
		//@Author Awakeninger
		//Reworked For Lindvior
		//Knight
		_CA.put(90, 148);
		_CA.put(91, 149);
		_CA.put(99, 150);
		_CA.put(106, 151);
		_CA.put(139, 148);
		_CA.put(139, 149);
		_CA.put(139, 150);
		_CA.put(139, 151);
		//Tyrr
		_CA.put(89, 153);
		_CA.put(88, 152);
		_CA.put(113, 154);
		_CA.put(114, 155);
		_CA.put(118, 156);
		_CA.put(131, 157);
		_CA.put(140, 152);
		_CA.put(140, 153);
		_CA.put(140, 154);
		_CA.put(140, 155);
		//_CA.put(140, 156); Гнум Маэстро
		_CA.put(140, 157);
		//Dagger
		_CA.put(93, 158);
		_CA.put(101, 159);
		_CA.put(108, 160);
		_CA.put(117, 161);
		_CA.put(141, 158);
		_CA.put(141, 159);
		_CA.put(141, 160);
		_CA.put(141, 161);
		//Archer
		_CA.put(92, 162);
		_CA.put(102, 163);
		_CA.put(109, 164);
		_CA.put(134, 165);
		_CA.put(142, 162);
		_CA.put(142, 163);
		_CA.put(142, 164);
		_CA.put(142, 165);
		//Feo
		_CA.put(94, 166);
		_CA.put(95, 167);
		_CA.put(103, 168);
		_CA.put(110, 169);
		_CA.put(132, 170);
		_CA.put(133, 170); // Я так понимаю у камаэлей сб нет разделения на пол
		_CA.put(143, 166);
		_CA.put(143, 167);
		_CA.put(143, 168);
		_CA.put(143, 169);
		_CA.put(143, 170);

		//Enchanters
		_CA.put(98, 171);
		_CA.put(100, 172);
		_CA.put(115, 174);
		_CA.put(116, 175);
		_CA.put(107, 173);
		_CA.put(144, 171);
		_CA.put(144, 172);
		_CA.put(144, 173);
		//_CA.put(144, 174); ИС Доминатор
		_CA.put(144, 175);
		//_CA.put(136, 176); без перерождения

		_CA.put(96, 176);
		_CA.put(104, 177);
		_CA.put(111, 178);
		_CA.put(145, 176);
		_CA.put(145, 177);
		_CA.put(145, 178);

		_CA.put(97, 179);
		_CA.put(146, 179);
		_CA.put(105, 180);
		_CA.put(146, 180);
		_CA.put(112, 181);
		_CA.put(146, 181);

		_log.info("AwakingManager: Loaded 34 Awaking class for " + _CA.size() + " normal class.");

	}

	public static AwakingManager getInstance()
	{
		if(_instance == null)
		{
			_log.info("Initializing: AwakingManager");
			_instance = new AwakingManager();
			_instance.load();
		}
		return _instance;
	}

	// TODO: Проверки для начала квеста...
	public void SendReqToStartQuest(Player player)
	{
		if(!player.getClassId().isOfLevel(ClassLevel.Fourth))
		{
			return;
		}

		int newClass = _CA.get(player.getClassId().getId());

		player.sendPacket(new ExCallToChangeClass(newClass, false));

	}

	// TODO: Проверки для пробуждения...
	public void SendReqToAwaking(Player player)
	{
		if(!player.getClassId().isOfLevel(ClassLevel.Fourth))
		{
			return;
		}
		int newClass = _CA.get(player.getClassId().getId());
		player.sendPacket(new ExChangeToAwakenedClass(newClass));
		return;
	}

	public void onStartQuestAccept(Player player)
	{
		// Телепортируем в музей
		player.teleToLocation(-114708, 243918, -7968);
		// Показываем видео
		player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
		return;
	}

	public void SetAwakingId(Player player)
	{
		int _oldId = player.getClassId().getId();
		player.broadcastPacket(new SocialAction(player.getObjectId(), 20));
		onTransferOnlyRemoveSkills(player);
		getRaceSkill(player);
		player.setClassId(_CA.get(_oldId), false, false);
		player.broadcastUserInfo(true);
		player.sendUserInfo();
		player.updateStats();
		giveGiantEssences(player, true);
	}
	

	public void SetAwakingIdForAlreadyAwakened(Player player)
	{
		int _oldId = player.getClassId().getId();
		//onTransferOnlyRemoveSkills(player);
		getRaceSkill(player);
		player.setClassId(_CA.get(_oldId), false, false);
		player.broadcastUserInfo(true);
		player.sendUserInfo();
		player.updateStats();
		//giveGiantEssences(player, true);
		player.broadcastPacket(new SocialAction(player.getObjectId(), (_CA.get(_oldId) - 139)));

	}

	@Override
	public void onPlayerEnter(Player player)
	{

		if(!player.getClassId().isOfLevel(ClassLevel.Fourth))
		{
			return;
		}
		if(player.getLevel() < 85)
		{
			return;
		}
		if(player.isAwaking())
		{
			return;
		}
		if(player.getActiveSubClass().isBase() || player.getActiveSubClass().isDouble())
		{
			player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
			player.sendPacket(new ExCallToChangeClass(_CA.get(player.getClassId().getId()), true));
		}

	}

	public Skill getRaceSkill(Player player)
	{
		int race = player.getRace().ordinal();

		Skill skill = null;
		if(player.getClassId().isOfLevel(ClassLevel.Awaking))
		{
			switch(race)
			{
				case 0:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 1:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 2:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 3:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 4:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 5:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
			}
		}
		else
		{
			player.sendActionFailed();
		}
		player.updateStats();
		return null;
	}

	public int giveGiantEssences(Player player, boolean onlyCalculateCount)
	{
		List<Integer> enchantedSkills = new FastList<Integer>();
		int count = 0;

		for(Skill skill : player.getAllSkills())
		{
			if(SkillTreeTable.isEnchantable(skill) != 0 && player.getSkillDisplayLevel(skill.getId()) > 99)
			{

				int skillLvl = skill.getDisplayLevel();
				int elevel = skillLvl % 100;

				EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(skill.getId(), skillLvl);
				if(sl != null)
				{
					if(sl.getMaxLevel() == 15)
					{

						elevel = Math.min(count15.length, elevel);
						count += count15[elevel];
					}
					else
					{
						elevel = Math.min(count30T.length, elevel);
						count += count30T[elevel];
					}
				}
			}
			enchantedSkills.add(Integer.valueOf(skill.getId()));
		}
		if(!onlyCalculateCount)
		{
			if(count > 0)
			{
				for(int i = 0; i < enchantedSkills.size(); i++)
				{
					player.removeSkillById(enchantedSkills.get(i));
					player.addSkill(SkillTable.getInstance().getInfo(enchantedSkills.get(i), SkillTable.getInstance().getBaseLevel(enchantedSkills.get(i))), true);
				}
				ItemFunctions.addItem(player, ESSENCE_OF_THE_LESSER_GIANTS, count, true);
			}
		}
		return count;
	}

	private void onTransferOnlyRemoveSkills(Player player)
	{
		int previousClassId = player.getClassId().getId();
		int newClassId = _CA.get(previousClassId);
		boolean delete = true; //Awakeninger: Аплоадит в дб либо нет
		//if(Config.ALT_DELETE_AWAKEN_SKILL_FROM_DB)
		//delete = true;
		List<Integer> skillsToMantain = SkillAcquireHolder.getInstance().getMaintainSkillOnAwake(previousClassId, newClassId);
		List<Integer> allSkillsId = SkillAcquireHolder.getInstance().getAllClassSkillId();
		for(Skill skl : player.getAllSkills())
		{
			if(allSkillsId.contains(skl.getId()))
			{
				player.removeSkill(skl, delete);
			}
		}
		for(int skillId : skillsToMantain)
		{
			int skillLv = SkillTable.getInstance().getBaseLevel(skillId);
			Skill newSkill = SkillTable.getInstance().getInfo(skillId, skillLv);
			player.addSkill(newSkill, true);
		}
		player.sendSkillList();
	}
}
