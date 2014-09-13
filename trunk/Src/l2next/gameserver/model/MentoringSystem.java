package l2next.gameserver.model;

import l2next.gameserver.dao.MentoringDAO;
import l2next.gameserver.network.serverpackets.ExMentorList;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.utils.MentorUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Andrey A.
 * <p/>
 * Date: 01.11.12
 * <p/>
 * Time: 12:18
 */
public class MentoringSystem
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

	private List<MenteeInfo> menteeInfo = new ArrayList<MenteeInfo>();
	private Player mentor;

	public MentoringSystem(Player mentor)
	{
		this.mentor = mentor;
	}

	public void addMentee(Player mentee)
	{
		if(menteeInfo.size() <= 3)
		{
			menteeInfo.add(new MenteeInfo(mentee, false));
			MentoringDAO.getInstance().insert(mentor, mentee);
		}
	}

	public void addMentor(Player mentor)
	{
		if(menteeInfo.size() == 0)
		{
			menteeInfo.add(new MenteeInfo(mentor, true));
			MentorUtil.addSkillsToMentor(mentor);
		}
	}

	public void remove(String name, boolean isMentor, boolean notify)
	{
		if(StringUtils.isEmpty(name))
		{
			return;
		}
		int objectId = removeMentee(name);
		if(objectId > 0 && notify)
		{
			Player otherSideMentee = World.getPlayer(name);

			if(otherSideMentee != null)
			{
				otherSideMentee.sendPacket(new SystemMessage2(SystemMsg.THE_MENTORING_RELATIONSHIP_WITH_S1_HAS_BEEN_CANCELED).addString(isMentor ? name : mentor.getName()));
				MentorUtil.removeEffectsFromPlayer(otherSideMentee);
			}
			mentor.sendPacket(new SystemMessage2(SystemMsg.THE_MENTORING_RELATIONSHIP_WITH_S1_HAS_BEEN_CANCELED).addString(isMentor ? name : mentor.getName()));
		}
	}

	private int removeMentee(String name)
	{
		if(name == null)
		{
			return 0;
		}

		MenteeInfo removedMentee = null;
		for(MenteeInfo entry : menteeInfo)
		{
			if(name.equalsIgnoreCase(entry.getName()))
			{
				removedMentee = entry;
				break;
			}
		}

		if(removedMentee != null)
		{
			menteeInfo.remove(removedMentee);
			MentoringDAO.getInstance().delete(mentor.getObjectId(), removedMentee.getObjectId());
			return removedMentee.getObjectId();
		}
		return 0;
	}

	public MenteeInfo getMenteeById(int id)
	{
		for(MenteeInfo info : menteeInfo)
		{
			if(info.getObjectId() == id)
			{
				return info;
			}
		}
		return null;
	}

	public List<MenteeInfo> getMenteeInfo()
	{
		return menteeInfo;
	}

	public int getMentor()
	{
		for(MenteeInfo menteeInfo : getMenteeInfo())
		{
			if(menteeInfo.isMentor())
			{
				return menteeInfo.getObjectId();
			}
		}
		return 0;
	}

	public void restore()
	{
		menteeInfo = MentoringDAO.getInstance().selectMenteeList(mentor);
	}

	public boolean whoIsOnline(boolean login)
	{
		for(MenteeInfo mentee : menteeInfo)
		{
			Player menteePlayer = World.getPlayer(mentee.getObjectId());
			if(menteePlayer != null)
			{
				MenteeInfo thisMentee = menteePlayer.getMentorSystem().checkInList(mentor.getObjectId());
				if(thisMentee == null)
				{
					continue;
				}

				thisMentee.update(mentor, login);

				if(menteePlayer.isOnline())
				{
					return true;
				}
			}
		}
		return false;
	}

	public void notify(boolean online)
	{
		for(MenteeInfo mentee : menteeInfo)
		{
			Player menteePlayer = World.getPlayer(mentee.getObjectId());
			if(menteePlayer != null)
			{
				MenteeInfo thisMentee = menteePlayer.getMentorSystem().checkInList(mentor.getObjectId());
				if(thisMentee == null)
				{
					continue;
				}

				thisMentee.update(mentor, online);

				if(online)
				{
					menteePlayer.sendPacket(new SystemMessage2(mentee.isMentor() ? SystemMsg.YOU_MENTEE_S1_HAS_CONNECTED : SystemMsg.YOU_MENTOR_S1_HAS_CONNECTED).addString(mentor.getName()));
				}
				else
				{
					menteePlayer.sendPacket(new SystemMessage2(mentee.isMentor() ? SystemMsg.YOU_MENTEE_S1_HAS_DISCONNECTED : SystemMsg.YOU_MENTOR_S1_HAS_DISCONNECTED).addString(mentor.getName()));
				}
				menteePlayer.sendPacket(new ExMentorList(menteePlayer));
				mentee.update(menteePlayer, online);
			}
		}
	}

	public MenteeInfo checkInList(int objectId)
	{
		for(MenteeInfo mentee : menteeInfo)
		{
			if(mentee.getObjectId() == objectId)
			{
				return mentee;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return "MentoringSystem[owner=" + mentor.getName() + "]";
	}
}
