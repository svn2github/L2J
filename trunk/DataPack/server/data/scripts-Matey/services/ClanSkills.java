package services;

import l2next.commons.collections.GArray;
import l2next.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2next.gameserver.listener.actor.player.OnPlayerExitListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.actor.listener.CharListenerList;
import l2next.gameserver.scripts.ScriptFile;
import l2next.gameserver.tables.SkillTable;

/**
 * User: Samurai
 */
public class ClanSkills implements ScriptFile, OnPlayerEnterListener, OnPlayerExitListener
{
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	@Override
	public void onPlayerEnter(Player player)
	{
		if(player.isClanLeader() && player.getClan().getLevel() >= 5)
		{
			Skill skill = SkillTable.getInstance().getInfo(19009, 1);
			GArray<Creature> target = new GArray<Creature>();
			target.add(player);
			player.callSkill(skill, target, true);
			for(Player member : player.getClan().getOnlineMembers(0))
			{
				target = new GArray<Creature>();
				target.add(member);
				member.callSkill(skill, target, true);
			}
		}
		if(player.getClan() != null && player.getClan().getLevel() >= 5 && player.getClan().getLeader().isOnline())
		{
			Skill skill = SkillTable.getInstance().getInfo(19009, 1);
			GArray<Creature> target = new GArray<Creature>();
			target.add(player);
			player.callSkill(skill, target, true);
		}
	}

	@Override
	public void onPlayerExit(Player player)
	{
		if(player.isClanLeader() && player.getClan().getLevel() >= 5)
		{
			Skill skill = SkillTable.getInstance().getInfo(19009, 1);
			for(Player member : player.getClan().getOnlineMembers(0))
			{
				member.getEffectList().stopEffect(skill);
			}
		}
	}
}