package l2next.gameserver.network.clientpackets;

import l2next.gameserver.Announcements;
import l2next.gameserver.Config;
import l2next.gameserver.dao.MailDAO;
import l2next.gameserver.data.StringHolder;
import l2next.gameserver.data.xml.holder.ResidenceHolder;
import l2next.gameserver.instancemanager.AwakingManager;
import l2next.gameserver.instancemanager.CoupleManager;
import l2next.gameserver.instancemanager.CursedWeaponsManager;
import l2next.gameserver.instancemanager.PetitionManager;
import l2next.gameserver.instancemanager.PlayerMessageStack;
import l2next.gameserver.instancemanager.QuestManager;
import l2next.gameserver.listener.actor.player.OnAnswerListener;
import l2next.gameserver.listener.actor.player.impl.ReviveAnswerListener;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Effect;
import l2next.gameserver.model.GameObjectsStorage;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Skill;
import l2next.gameserver.model.Summon;
import l2next.gameserver.model.World;
import l2next.gameserver.model.base.ClassLevel;
import l2next.gameserver.model.base.InvisibleType;
import l2next.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import l2next.gameserver.model.entity.residence.Castle;
import l2next.gameserver.model.entity.residence.ClanHall;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.model.mail.Mail;
import l2next.gameserver.model.party.PartySubstitute;
import l2next.gameserver.model.pledge.Clan;
import l2next.gameserver.model.pledge.SubUnit;
import l2next.gameserver.model.pledge.UnitMember;
import l2next.gameserver.model.quest.Quest;
import l2next.gameserver.network.GameClient;
import l2next.gameserver.network.serverpackets.ChangeWaitType;
import l2next.gameserver.network.serverpackets.ClientSetTime;
import l2next.gameserver.network.serverpackets.ConfirmDlg;
import l2next.gameserver.network.serverpackets.Die;
import l2next.gameserver.network.serverpackets.EtcStatusUpdate;
import l2next.gameserver.network.serverpackets.EventTrigger;
import l2next.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import l2next.gameserver.network.serverpackets.ExAutoSoulShot;
import l2next.gameserver.network.serverpackets.ExBR_NewIConCashBtnWnd;
import l2next.gameserver.network.serverpackets.ExBR_PremiumState;
import l2next.gameserver.network.serverpackets.ExBasicActionList;
import l2next.gameserver.network.serverpackets.ExCastleState;
import l2next.gameserver.network.serverpackets.ExChangeMPCost;
import l2next.gameserver.network.serverpackets.ExGoodsInventoryChangedNotify;
import l2next.gameserver.network.serverpackets.ExMPCCOpen;
import l2next.gameserver.network.serverpackets.ExNoticePostArrived;
import l2next.gameserver.network.serverpackets.ExNotifyPremiumItem;
import l2next.gameserver.network.serverpackets.ExPCCafePointInfo;
import l2next.gameserver.network.serverpackets.ExPledgeCount;
import l2next.gameserver.network.serverpackets.ExReceiveShowPostFriend;
import l2next.gameserver.network.serverpackets.ExSetCompassZoneCode;
import l2next.gameserver.network.serverpackets.ExShowUsmVideo;
import l2next.gameserver.network.serverpackets.ExStorageMaxCount;
import l2next.gameserver.network.serverpackets.ExSubjobInfo;
import l2next.gameserver.network.serverpackets.ExTutorialList;
import l2next.gameserver.network.serverpackets.ExVitalityEffectInfo;
import l2next.gameserver.network.serverpackets.HennaInfo;
import l2next.gameserver.network.serverpackets.L2FriendList;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.MagicSkillLaunched;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.network.serverpackets.PartySmallWindowAll;
import l2next.gameserver.network.serverpackets.PartySpelled;
import l2next.gameserver.network.serverpackets.PetInfo;
import l2next.gameserver.network.serverpackets.PledgeRecruit.ExPledgeWaitingListAlarm;
import l2next.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import l2next.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import l2next.gameserver.network.serverpackets.PledgeSkillList;
import l2next.gameserver.network.serverpackets.PrivateStoreMsgBuy;
import l2next.gameserver.network.serverpackets.PrivateStoreMsgSell;
import l2next.gameserver.network.serverpackets.QuestList;
import l2next.gameserver.network.serverpackets.RecipeShopMsg;
import l2next.gameserver.network.serverpackets.RelationChanged;
import l2next.gameserver.network.serverpackets.Ride;
import l2next.gameserver.network.serverpackets.SSQInfo;
import l2next.gameserver.network.serverpackets.ShortCutInit;
import l2next.gameserver.network.serverpackets.SkillCoolTime;
import l2next.gameserver.network.serverpackets.SkillList;
import l2next.gameserver.network.serverpackets.SystemMessage2;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.skills.AbnormalEffect;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.templates.item.ItemTemplate;
import l2next.gameserver.utils.GameStats;
import l2next.gameserver.utils.ItemFunctions;
import l2next.gameserver.utils.MentorUtil;
import l2next.gameserver.utils.TradeHelper;
import org.apache.commons.lang3.tuple.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Calendar;


public class EnterWorld extends L2GameClientPacket
{
	//private Castle _castle;
	private Clan _clan;
	private static final Logger _log = LoggerFactory.getLogger(EnterWorld.class);
	
	@Override
	protected void readImpl()
	{
		// readS(); - клиент всегда отправляет строку "narcasse"
	}

	@Override
	protected void runImpl()
	{
		GameClient client = getClient();
		Player activeChar = client.getActiveChar();

		if(activeChar == null)
		{
			client.closeNow(false);
			return;
		}

		GameStats.incrementPlayerEnterGame();

		boolean first = activeChar.entering;

		if(first)
		{
			activeChar.setOnlineStatus(true);
			if(activeChar.getPlayerAccess().GodMode && !Config.SHOW_GM_LOGIN)
			{
				activeChar.setInvisibleType(InvisibleType.NORMAL);
			}

			activeChar.setNonAggroTime(Long.MAX_VALUE);
			activeChar.spawnMe();

			if(activeChar.isInStoreMode())
			{
				if(!TradeHelper.checksIfCanOpenStore(activeChar, activeChar.getPrivateStoreType()))
				{
					activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
					activeChar.standUp();
					activeChar.broadcastCharInfo();
				}
			}

			activeChar.setRunning();
			activeChar.standUp();
			activeChar.startTimers();
		}

		activeChar.sendPacket(new ExBR_PremiumState(activeChar, activeChar.hasBonus()));

		activeChar.getMacroses().sendUpdate(0x01, 0, true);
		activeChar.sendPacket(new SSQInfo(), new HennaInfo(activeChar));
		activeChar.sendItemList(false);
		activeChar.sendPacket(new ShortCutInit(activeChar), new SkillList(activeChar), new SkillCoolTime(activeChar));
		
		if(activeChar.getClan() != null)
		{
			int count = activeChar.getClan().getOnlineCount();
			activeChar.sendPacket(new ExPledgeCount(count));
		}
		activeChar.sendPacket(new ExVitalityEffectInfo(activeChar));
		for(Castle castle : ResidenceHolder.getInstance().getResidenceList(Castle.class))
		{
			activeChar.sendPacket(new ExCastleState(castle));
		}

		activeChar.unsetVar("Altar1");
		activeChar.unsetVar("Altar2");
		activeChar.unsetVar("Altar3");
		activeChar.sendPacket(SystemMsg.WELCOME_TO_THE_WORLD_OF_LINEAGE_II);
		activeChar.sendPacket(new ExBR_NewIConCashBtnWnd(0), new ExPledgeWaitingListAlarm());
		Announcements.getInstance().showAnnouncements(activeChar);

		if(first)
		{
			activeChar.getListeners().onEnter();
		}

		if(first && activeChar.getCreateTime() > 0)
		{
			Calendar create = Calendar.getInstance();
			create.setTimeInMillis(activeChar.getCreateTime());
			Calendar now = Calendar.getInstance();

			int day = create.get(Calendar.DAY_OF_MONTH);
			if(create.get(Calendar.MONTH) == Calendar.FEBRUARY && day == 29)
			{
				day = 28;
			}

			int myBirthdayReceiveYear = activeChar.getVarInt(Player.MY_BIRTHDAY_RECEIVE_YEAR, 0);
			if(create.get(Calendar.MONTH) == now.get(Calendar.MONTH) && create.get(Calendar.DAY_OF_MONTH) == day)
			{
				if(myBirthdayReceiveYear == 0 && create.get(Calendar.YEAR) != now.get(Calendar.YEAR) || myBirthdayReceiveYear > 0 && myBirthdayReceiveYear != now.get(Calendar.YEAR))
				{
					Mail mail = new Mail();
					mail.setSenderId(1);
					mail.setSenderName(StringHolder.getInstance().getNotNull(activeChar, "birthday.npc"));
					mail.setReceiverId(activeChar.getObjectId());
					mail.setReceiverName(activeChar.getName());
					mail.setTopic(StringHolder.getInstance().getNotNull(activeChar, "birthday.title"));
					mail.setBody(StringHolder.getInstance().getNotNull(activeChar, "birthday.text"));

					ItemInstance item = ItemFunctions.createItem(21169);
					item.setLocation(ItemInstance.ItemLocation.MAIL);
					item.setCount(1L);
					item.save();

					mail.addAttachment(item);
					mail.setUnread(true);
					mail.setType(Mail.SenderType.BIRTHDAY);
					mail.setExpireTime(720 * 3600 + (int) (System.currentTimeMillis() / 1000L));
					mail.save();

					activeChar.setVar(Player.MY_BIRTHDAY_RECEIVE_YEAR, String.valueOf(now.get(Calendar.YEAR)), -1);
				}
			}
		}

		if(activeChar.getClan() != null)
		{
			notifyClanMembers(activeChar);

			activeChar.sendPacket(activeChar.getClan().listAll());
			activeChar.sendPacket(new PledgeShowInfoUpdate(activeChar.getClan()), new PledgeSkillList(activeChar.getClan()));

			//            if (activeChar.isClanLeader() & activeChar.getClan().getLevel() >= 5)
			//                for (Player allmembers : activeChar.getClan().getOnlineMembers(activeChar.getObjectId())) {
			//                    if (allmembers != activeChar) {
			//                        Skill skill = SkillTable.getInstance().getInfo(19009, 1);
			//                        for (EffectTemplate et : skill.getEffectTemplates()) {
			//                            Env env = new Env(allmembers, allmembers, skill);
			//                            Effect effect = et.getEffect(env);
			//                            effect.setPeriod(3600000);
			//                            allmembers.getEffectList().addEffect(effect);
			//                        }
			//                    }
			//                }
			//            Player clanLeader = activeChar.getClan().getLeader().getPlayer();
			//
			//    		if (clanLeader != null && !activeChar.isClanLeader() && clanLeader.isOnline() && activeChar.getClan().getLevel() >= 5)
			//    		{
			//                Skill skill = SkillTable.getInstance().getInfo(19009, 1);
			//                for (EffectTemplate et : skill.getEffectTemplates()) {
			//                    Env env = new Env(activeChar, activeChar, skill);
			//                    Effect effect = et.getEffect(env);
			//                    effect.setPeriod(3600000);
			//                    activeChar.getEffectList().addEffect(effect);
			//                }
			//    		}
		}

		// engage and notify Partner
		if(first && Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance().engage(activeChar);
			CoupleManager.getInstance().notifyPartner(activeChar);
		}

		if(first)
		{
			activeChar.getFriendList().notifyFriends(true);
			loadTutorial(activeChar);
			activeChar.restoreDisableSkills();
			activeChar.mentoringLoginConditions();
		}

		if(activeChar.getLevel() >= 85)
		{
			MentorUtil.removeSkills(activeChar);
		}

		sendPacket(new L2FriendList(activeChar), new ExStorageMaxCount(activeChar), new QuestList(activeChar), new ExBasicActionList(activeChar), new EtcStatusUpdate(activeChar));
		//Announcements.getInstance().announceToAll(new ExCastleState(_castle));
		activeChar.checkHpMessages(activeChar.getMaxHp(), activeChar.getCurrentHp());
		activeChar.checkDayNightMessages();
		if(activeChar.getVar("Letter") == null && activeChar.getLevel() >= 90)
		{
			ItemFunctions.addItem(activeChar, 17725, 1, true);
			activeChar.setVar("Letter", "give", -1);
		}
		if(Config.PETITIONING_ALLOWED)
		{
			PetitionManager.getInstance().checkPetitionMessages(activeChar);
		}

		if(!first)
		{
			if(activeChar.isCastingNow())
			{
				Creature castingTarget = activeChar.getCastingTarget();
				Skill castingSkill = activeChar.getCastingSkill();
				long animationEndTime = activeChar.getAnimationEndTime();
				if(castingSkill != null && castingTarget != null && castingTarget.isCreature() && activeChar.getAnimationEndTime() > 0)
				{
					sendPacket(new MagicSkillUse(activeChar, castingTarget, castingSkill.getId(), castingSkill.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0));
				}
			}

			if(activeChar.isInBoat())
			{
				activeChar.sendPacket(activeChar.getBoat().getOnPacket(activeChar, activeChar.getInBoatPosition()));
			}

			if(activeChar.isMoving || activeChar.isFollow)
			{
				sendPacket(activeChar.movePacket());
			}

			if(activeChar.getMountNpcId() != 0)
			{
				sendPacket(new Ride(activeChar));
			}

			if(activeChar.isFishing())
			{
				activeChar.stopFishing();
			}
		}

		activeChar.entering = false;
		activeChar.sendUserInfo(true);

		if(activeChar.isSitting())
		{
			activeChar.sendPacket(new ChangeWaitType(activeChar, ChangeWaitType.WT_SITTING));
		}
		if(activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			if(activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_BUY)
			{
				sendPacket(new PrivateStoreMsgBuy(activeChar));
			}
			else if(activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL || activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE)
			{
				sendPacket(new PrivateStoreMsgSell(activeChar));
			}
			else if(activeChar.getPrivateStoreType() == Player.STORE_PRIVATE_MANUFACTURE)
			{
				sendPacket(new RecipeShopMsg(activeChar));
			}
		}

		if(activeChar.isDead())
		{
			sendPacket(new Die(activeChar));
		}

		activeChar.unsetVar("offline");

		// на всякий случай
		activeChar.sendActionFailed();

		if(first && activeChar.isGM() && Config.SAVE_GM_EFFECTS && activeChar.getPlayerAccess().CanUseGMCommand)
		{
			// silence
			if(activeChar.getVarB("gm_silence"))
			{
				activeChar.setMessageRefusal(true);
				activeChar.sendPacket(SystemMsg.MESSAGE_REFUSAL_MODE);
			}
			// invul
			if(activeChar.getVarB("gm_invul"))
			{
				activeChar.setIsInvul(true);
				activeChar.startAbnormalEffect(AbnormalEffect.S_INVULNERABLE);
				activeChar.sendMessage(activeChar.getName() + " is now immortal.");
			}
			// gmspeed
			try
			{
				int var_gmspeed = Integer.parseInt(activeChar.getVar("gm_gmspeed"));
				if(var_gmspeed >= 1 && var_gmspeed <= 4)
				{
					activeChar.doCast(SkillTable.getInstance().getInfo(7029, var_gmspeed), activeChar, true);
				}
			}
			catch(Exception E)
			{
			}
		}

		PlayerMessageStack.getInstance().CheckMessages(activeChar);

		sendPacket(ClientSetTime.STATIC, new ExSetCompassZoneCode(activeChar));

		Pair<Integer, OnAnswerListener> entry = activeChar.getAskListener(false);
		if(entry != null && entry.getValue() instanceof ReviveAnswerListener)
		{
			sendPacket(new ConfirmDlg(SystemMsg.C1_IS_MAKING_AN_ATTEMPT_TO_RESURRECT_YOU_IF_YOU_CHOOSE_THIS_PATH_S2_EXPERIENCE_WILL_BE_RETURNED_FOR_YOU, 0).addString("Other player").addString("some"));
		}

		if(activeChar.isCursedWeaponEquipped())
		{
			CursedWeaponsManager.getInstance().showUsageTime(activeChar, activeChar.getCursedWeaponEquippedId());
		}

		if(!first)
		{
			// Персонаж вылетел во время просмотра
			if(activeChar.isInObserverMode())
			{
				if(activeChar.getObserverMode() == Player.OBSERVER_LEAVING)
				{
					activeChar.returnFromObserverMode();
				}
				else if(activeChar.getOlympiadObserveGame() != null)
				{
					activeChar.leaveOlympiadObserverMode(true);
				}
				else
				{
					activeChar.leaveObserverMode();
				}
			}
			else if(activeChar.isVisible())
			{
				World.showObjectsToPlayer(activeChar);
			}

			for(Summon summon : activeChar.getPets())
			{
				sendPacket(new PetInfo(summon));
			}

			if(activeChar.isInParty())
			{
				// sends new member party window for all members
				// we do all actions before adding member to a list, this speeds
				// things up a little
				sendPacket(new PartySmallWindowAll(activeChar.getParty(), activeChar));

				for(Player member : activeChar.getParty().getPartyMembers())
				{
					if(member != activeChar)
					{
						sendPacket(new PartySpelled(member, true));
						for(Summon memberPet : member.getPets())
						{
							sendPacket(new PartySpelled(memberPet, true));
						}

						sendPacket(RelationChanged.update(activeChar, member, activeChar));
					}
				}

				// Если партия уже в СС, то вновь прибывшем посылаем пакет
				// открытия окна СС
				if(activeChar.getParty().isInCommandChannel())
				{
					sendPacket(ExMPCCOpen.STATIC);
				}
			}

			for(int shotId : activeChar.getAutoSoulShot())
			{
				sendPacket(new ExAutoSoulShot(shotId, true));
			}

			for(Effect e : activeChar.getEffectList().getAllFirstEffects())
			{
				if(e.getSkill().isToggle())
				{
					sendPacket(new MagicSkillLaunched(activeChar.getObjectId(), e.getSkill().getId(), e.getSkill().getLevel(), activeChar));
				}
			}

			activeChar.broadcastCharInfo();
		}
		else
		{
			activeChar.sendUserInfo(); // Отобразит права в клане
		}

		activeChar.updateEffectIcons();
		activeChar.updateStats();

		if(Config.ALT_PCBANG_POINTS_ENABLED)
		{
			activeChar.sendPacket(new ExPCCafePointInfo(activeChar, 0, 1, 2, 12));
		}

		if(!activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(Config.GOODS_INVENTORY_ENABLED ? ExGoodsInventoryChangedNotify.STATIC : ExNotifyPremiumItem.STATIC);
		}

		activeChar.sendVoteSystemInfo();
		activeChar.sendPacket(new ExReceiveShowPostFriend(activeChar));

		checkNewMail(activeChar);

		// GoD
		activeChar.sendPacket(new ExSubjobInfo(activeChar.getPlayer(), false));

		activeChar.sendPacket(new ExTutorialList());

		activeChar.sendPacket(new ExAcquirableSkillListByClass(activeChar));

		if(first)
		{
			PartySubstitute.getInstance().addPlayerToParty(activeChar);
		}

		if(activeChar.getVar("startMovie") == null)
		{
			activeChar.setVar("startMovie", "1", 0);
			sendPacket(new ExShowUsmVideo(ExShowUsmVideo.GD1_INTRO));
		} //TODO: Был на оффе, там сразу показывало

		activeChar.sendPacket(new ExChangeMPCost(1, -3.0));
		activeChar.sendPacket(new ExChangeMPCost(1, -5.0));
		activeChar.sendPacket(new ExChangeMPCost(0, 20.0));
		activeChar.sendPacket(new ExChangeMPCost(1, -10.0));
		activeChar.sendPacket(new ExChangeMPCost(3, -20.0));
		activeChar.sendPacket(new ExChangeMPCost(22, -20.0));

		if(activeChar.getClassId().isOfLevel(ClassLevel.Awaking))
		{
			AwakingManager.getInstance().getRaceSkill(activeChar);
		}

		if(activeChar.getLevel() >= 85 & activeChar.getVar("GermunkusUSM") == null & !activeChar.isAwaking())
		{
			AwakingManager.getInstance().SendReqToStartQuest(activeChar);
		}

		if(activeChar.getLevel() == 40 && activeChar.getInventory().getCountOf(32777) == 0)
		{
			ItemFunctions.addItem(activeChar, 32777, 1, true);
		}

		if(activeChar.getLevel() == 85 && activeChar.getInventory().getCountOf(32778) == 0)
		{
			ItemFunctions.addItem(activeChar, 32778, 1, true);
		}
		
		broadcastPacket(24230010, true);
		broadcastPacket(24230012, true);
			if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 0 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 2 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4)
			{
				broadcastPacket(24230014, true);
				broadcastPacket(24230016, false);
				broadcastPacket(24230018, false);
			}
			else if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 3 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 5)
			{
				broadcastPacket(24230014, false);
				broadcastPacket(24230016, true);
				broadcastPacket(24230018, false);
			}
			else
			{
				broadcastPacket(24230014, false);
				broadcastPacket(24230016, false);
				broadcastPacket(24230018, true);
			}
	}

	private static void notifyClanMembers(Player activeChar)
	{
		Clan clan = activeChar.getClan();
		SubUnit subUnit = activeChar.getSubUnit();
		if(clan == null || subUnit == null)
		{
			return;
		}

		UnitMember member = subUnit.getUnitMember(activeChar.getObjectId());
		if(member == null)
		{
			return;
		}

		member.setPlayerInstance(activeChar, false);

		int sponsor = activeChar.getSponsor();
		int apprentice = activeChar.getApprentice();
		L2GameServerPacket msg = new SystemMessage2(SystemMsg.CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME).addName(activeChar);
		PledgeShowMemberListUpdate memberUpdate = new PledgeShowMemberListUpdate(activeChar);
		for(Player clanMember : clan.getOnlineMembers(activeChar.getObjectId()))
		{
			clanMember.sendPacket(memberUpdate);
			if(clanMember.getObjectId() == sponsor)
			{
				clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_APPRENTICE_C1_HAS_LOGGED_OUT).addName(activeChar));
			}
			else if(clanMember.getObjectId() == apprentice)
			{
				clanMember.sendPacket(new SystemMessage2(SystemMsg.YOUR_SPONSOR_C1_HAS_LOGGED_IN).addName(activeChar));
			}
			else
			{
				clanMember.sendPacket(msg);
			}
		}

		if(!activeChar.isClanLeader())
		{
			return;
		}

		ClanHall clanHall = clan.getHasHideout() > 0 ? ResidenceHolder.getInstance().getResidence(ClanHall.class, clan.getHasHideout()) : null;
		if(clanHall == null || clanHall.getAuctionLength() != 0)
		{
			return;
		}

		if(clanHall.getSiegeEvent().getClass() != ClanHallAuctionEvent.class)
		{
			return;
		}

		if(clan.getWarehouse().getCountOf(ItemTemplate.ITEM_ID_ADENA) < clanHall.getRentalFee())
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_ME_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW).addLong(clanHall.getRentalFee()));
		}
	}

	private void loadTutorial(Player player)
	{
		Quest q = QuestManager.getQuest(255);
		if(q != null)
		{
			player.processQuestEvent(q.getName(), "UC", null);
		}
	}
	
	public void broadcastPacket(int value, boolean b)
	{
		L2GameServerPacket trigger = new EventTrigger(value, b);
		for(Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(trigger);
		}
	}

	private void checkNewMail(Player activeChar)
	{
		for(Mail mail : MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId()))
		{
			if(mail.isUnread())
			{
				sendPacket(ExNoticePostArrived.STATIC_FALSE);
				break;
			}
		}
	}
}