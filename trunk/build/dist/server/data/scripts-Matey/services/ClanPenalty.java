package services;

import l2next.gameserver.model.Player;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.utils.Htm;
import l2next.gameserver.utils.Merchant;

/**
 * User: Samurai
 */
public class ClanPenalty extends Functions
{
	public void clanPenaltyPage()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<font color=LEVEL>Снятие клан штрафов</font>");
		sb.append("<br>");

		sb.append("<font color=3293F3>Штраф на вступление в клан</font>");
		sb.append(Htm.button("Снять за 1 CoL", "_bbsscripts:services.ClanPenalty:charLeaveClan", 100, 26));
		sb.append("<br>");

		sb.append("<font color=3293F3>Штраф на создание клана</font>");
		sb.append(Htm.button("Снять за 1 CoL", "_bbsscripts:services.ClanPenalty:charDeleteClan", 100, 26));
		sb.append("<br>");

		sb.append("<font color=3293F3>Штраф на принятие новичков в клан</font>");
		sb.append(Htm.button("Снять за 1 CoL", "_bbsscripts:services.ClanPenalty:clanExpelledMember", 100, 26));
		sb.append("<br>");

		sb.append("<font color=3293F3>Штраф на вступление в альянс</font>");
		sb.append(Htm.button("Снять за 1 CoL", "_bbsscripts:services.ClanPenalty:clanLeavedAlly", 100, 26));
		sb.append("<br>");

		sb.append("<font color=3293F3>Штраф на создание альянса</font>");
		sb.append(Htm.button("Снять за 1 CoL", "_bbsscripts:services.ClanPenalty:clanDissolvedAlly", 100, 26));
		sb.append("<br>");

		show(sb.toString(), player);
	}

	public void charLeaveClan()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		if(player.getLeaveClanTime() == 0)
		{
			player.sendMessage("Штрафа нет");
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			player.setLeaveClanTime(0);
			player.sendMessage("Штраф снят");
		}
	}

	public void charDeleteClan()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		if(player.getDeleteClanTime() == 0)
		{
			player.sendMessage("Штрафа нет");
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			player.setDeleteClanTime(0);
			player.sendMessage("Штраф снят");
		}
	}

	public void clanExpelledMember()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		Clan clan = player.getClan();
		if(clan == null)
		{
			player.sendMessage("Вы не состоите в клане");
			return;
		}
		if(clan.getExpelledMemberTime() == 0)
		{
			player.sendMessage("Штрафа нет");
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			clan.setExpelledMemberTime(0);
			player.sendMessage("Штраф снят");
		}
	}

	public void clanLeavedAlly()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		Clan clan = player.getClan();
		if(clan == null)
		{
			player.sendMessage("Вы не состоите в клане");
			return;
		}
		if(clan.getLeavedAllyTime() == 0)
		{
			player.sendMessage("Штрафа нет");
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			clan.setLeavedAllyTime(0);
			player.sendMessage("Штраф снят");
		}
	}

	public void clanDissolvedAlly()
	{
		Player player = getSelf();
		if(player == null)
		{
			return;
		}
		Clan clan = player.getClan();
		if(clan == null)
		{
			player.sendMessage("Вы не состоите в клане");
			return;
		}
		if(clan.getDissolvedAllyTime() == 0)
		{
			player.sendMessage("Штрафа нет");
			return;
		}
		if(Merchant.pay(player, 4037, 1))
		{
			clan.setDissolvedAllyTime(0);
			player.sendMessage("Штраф снят");
		}
	}
}