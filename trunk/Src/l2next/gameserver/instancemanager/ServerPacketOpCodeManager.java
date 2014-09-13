package l2next.gameserver.instancemanager;

import javolution.util.FastList;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServerPacketOpCodeManager
{
	private static final Logger _log = LogManager.getLogger(ServerPacketOpCodeManager.class);
	private static final HashMap<Integer, Integer> _serverOpCodes = new HashMap<>();

	private ServerPacketOpCodeManager()
	{
		hashServerPackets();
		_log.log(Level.INFO, getClass().getSimpleName() + ": Loaded " + _serverOpCodes.size() + " active server packet opcodes.");
	}

	public static List<Class> getClassesForPackage(String packageName) throws ClassNotFoundException
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		List names = new FastList();

		String packetPath = packageName.replace(".", "/");
		URL packageURL = classLoader.getResource(packetPath);

		if(packageURL != null)
		{
			if(packageURL.getProtocol().equals("jar"))
			{
				String jarFileName = null;

				Enumeration jarEntries = null;
				try
				{
					jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
				}
				catch(UnsupportedEncodingException e)
				{
					_log.error("Failed to get JAR name.", e);
				}

				if(jarFileName != null)
				{
					jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
					try
					{
						JarFile jf = new JarFile(jarFileName);
						jarEntries = jf.entries();
					}
					catch(IOException e)
					{
						_log.error("Failed to load JAR File.", e);
					}

					if(jarEntries != null)
					{
						while(jarEntries.hasMoreElements())
						{
							String entryName = ((JarEntry) jarEntries.nextElement()).getName();

							if(entryName.endsWith(".class"))
							{
								if((entryName.startsWith(packetPath)) && (entryName.length() > packetPath.length() + 5))
								{
									entryName = entryName.substring(packetPath.length() + 1, entryName.lastIndexOf('.'));
									names.add(Class.forName(packageName.concat(".").concat(entryName.replace("/", "."))));
								}
							}
							else
							{
								names.addAll(getClassesForPackage(packageName.concat(".").concat(entryName.replace("/", "."))));
							}
						}
					}
				}
			}
			else
			{
				File folder = new File(packageURL.getFile());
				File[] fileList = folder.listFiles();

				if(fileList != null)
				{
					for(File actual : fileList)
					{
						String entryName = actual.getName();

						if(entryName.endsWith(".class"))
						{
							entryName = entryName.substring(0, entryName.lastIndexOf('.'));
							names.add(Class.forName(packageName.concat(".").concat(entryName)));
						}
						else
						{
							names.addAll(getClassesForPackage(packageName.concat(".").concat(entryName)));
						}
					}
				}
			}
		}
		return names;
	}

	public static ServerPacketOpCodeManager getInstance()
	{
		return SingletonHolder._instance;
	}

	private void hashServerPackets()
	{
		try
		{
			List<Class> classes = getClassesForPackage("l2next.gameserver.network.serverpackets");
			for(Class clazz : classes)
			{
				for(ServerPacketEnum t : ServerPacketEnum.values())
				{
					if(t.name().equals(clazz.getSimpleName()))
					{
						_serverOpCodes.put(Integer.valueOf(clazz.hashCode()), Integer.valueOf(t.ordinal()));
//						Для создания костылей :)
//						if(clazz.getSimpleName().equalsIgnoreCase("ExBuySellList"))
//						{
//							System.out.println("ExBuySellList " + t.ordinal());
//						}
						break;
					}
				}
				// Костыли, переделать
				if(clazz.getSimpleName().equalsIgnoreCase("SystemMessage2"))
				{
					_serverOpCodes.put(Integer.valueOf(clazz.hashCode()), 98);
				}
				if(clazz.getSimpleName().equalsIgnoreCase("NpcSayNative"))
				{
					_serverOpCodes.put(Integer.valueOf(clazz.hashCode()), 48);
				}
				if(clazz.getSimpleName().equalsIgnoreCase("SellRefundList"))
				{
					_serverOpCodes.put(Integer.valueOf(clazz.hashCode()), 439);
				}
				if(clazz.getSimpleName().equalsIgnoreCase("BuyList"))
				{
					_serverOpCodes.put(Integer.valueOf(clazz.hashCode()), 439);
				}
			}
		}
		catch(ClassNotFoundException e)
		{
			_log.log(Level.ERROR, getClass().getSimpleName() + " : Error while reflecting server packet classes!", e);
		}
	}

	public int getOpCodeForPacketHash(int hash)
	{
		return _serverOpCodes.get(hash);
	}

	static enum ServerPacketEnum
	{
		Die,
		Revive,
		AttackOutofRange,
		AttackinCoolTime,
		AttackDeadTarget,
		SpawnItem,
		SellList,
		BuyList,
		DeleteObject,
		CharacterSelectionInfo,
		LoginFail,
		CharSelected,
		NpcInfo,
		NewCharacterSuccess,
		NewCharacterFail,
		CharacterCreateSuccess,
		CharacterCreateFail,
		ItemList,
		SunRise,
		SunSet,
		TradeStart,
		TradeStartOk,
		DropItem,
		GetItem,
		StatusUpdate,
		NpcHtmlMessage,
		TradeOwnAdd,
		TradeOtherAdd,
		SendTradeDone,
		CharacterDeleteSuccess,
		CharacterDeleteFail,
		ActionFail,
		SeverClose,
		InventoryUpdate,
		TeleportToLocation,
		TargetSelected,
		TargetUnselected,
		AutoAttackStart,
		AutoAttackStop,
		SocialAction,
		ChangeMoveType,
		ChangeWaitType,
		ManagePledgePower,
		CreatePledge,
		AskJoinPledge,
		JoinPledge,
		VersionCheck,
		CharMoveToLocation,
		NpcSay,
		CharInfo,
		UserInfo,
		Attack,
		WithdrawalPledge,
		OustPledgeMember,
		SetOustPledgeMember,
		DismissPledge,
		SetDismissPledge,
		AskJoinParty,
		JoinParty,
		WithdrawalParty,
		OustPartyMember,
		SetOustPartyMember,
		DismissParty,
		SetDismissParty,
		MagicAndSkillList,
		WareHouseDepositList,
		WareHouseWithdrawList,
		WareHouseDone,
		ShortCutRegister,
		ShortCutInit,
		ShortCutDelete,
		StopMove,
		MagicSkillUse,
		MagicSkillCanceled,
		Say2,
		EquipUpdate,
		DoorInfo,
		DoorStatusUpdate,
		PartySmallWindowAll,
		PartySmallWindowAdd,
		PartySmallWindowDeleteAll,
		PartySmallWindowDelete,
		PartySmallWindowUpdate,
		TradePressOwnOk,
		MagicSkillLaunched,
		FriendAddRequestResult,
		FriendAdd,
		FriendRemove,
		FriendList,
		FriendStatus,
		PledgeShowMemberListAll,
		PledgeShowMemberListUpdate,
		PledgeShowMemberListAdd,
		PledgeShowMemberListDelete,
		MagicList,
		SkillList,
		VehicleInfo,
		FinishRotating,
		SystemMessage,
		StartPledgeWar,
		ReplyStartPledgeWar,
		StopPledgeWar,
		ReplyStopPledgeWar,
		SurrenderPledgeWar,
		ReplySurrenderPledgeWar,
		SetPledgeCrest,
		PledgeCrest,
		SetupGauge,
		VehicleDeparture,
		VehicleCheckLocation,
		GetOnVehicle,
		GetOffVehicle,
		SendTradeRequest,
		RestartResponse,
		MoveToPawn,
		SSQInfo,
		GameGuardQuery,
		L2FriendList,
		L2Friend,
		L2FriendStatus,
		L2FriendSay,
		ValidateLocation,
		StartRotating,
		ShowBoard,
		ChooseInventoryItem,
		Dummy,
		MoveToLocationInVehicle,
		StopMoveInVehicle,
		ValidateLocationInVehicle,
		TradeUpdate,
		TradePressOtherOk,
		FriendAddRequest,
		LeaveWorld,
		AbnormalStatusUpdate,
		QuestList,
		EnchantResult,
		PledgeShowMemberListDeleteAll,
		PledgeInfo,
		PledgeExtendedInfo,
		SurrenderPersonally,
		Ride,
		Dummy_1,
		PledgeShowInfoUpdate,
		ClientAction,
		AcquireSkillList,
		AcquireSkillInfo,
		ServerObjectInfo,
		GMHide,
		AcquireSkillDone,
		GMViewCharacterInfo,
		GMViewPledgeInfo,
		GMViewSkillInfo,
		GMViewMagicInfo,
		GMViewQuestInfo,
		GMViewItemList,
		GMViewWarehouseWithdrawList,
		ListPartyWaiting,
		PartyRoomInfo,
		PlaySound,
		StaticObject,
		PrivateStoreManageListSell,
		PrivateStoreListSell,
		PrivateStoreMsgSell,
		ShowMiniMap,
		ReviveRequest,
		AbnormalVisualEffect,
		TutorialShowHtml,
		TutorialShowQuestionMark,
		TutorialEnableClientEvent,
		TutorialCloseHtml,
		ShowRadar,
		WithdrawAlliance,
		OustAllianceMemberPledge,
		DismissAlliance,
		SetAllianceCrest,
		AllianceCrest,
		ServerCloseSocket,
		PetStatusShow,
		PetInfo,
		PetItemList,
		PetInventoryUpdate,
		AllianceInfo,
		PetStatusUpdate,
		PetDelete,
		DeleteRadar,
		MyTargetSelected,
		PartyMemberPosition,
		AskJoinAlliance,
		JoinAlliance,
		PrivateStoreManageListBuy,
		PrivateStoreListBuy,
		PrivateStoreMsgBuy,
		VehicleStart,
		RequestTimeCheck,
		StartAllianceWar,
		ReplyStartAllianceWar,
		StopAllianceWar,
		ReplyStopAllianceWar,
		SurrenderAllianceWar,
		SkillCoolTime,
		PackageToList,
		CastleSiegeInfo,
		CastleSiegeAttackerList,
		CastleSiegeDefenderList,
		NickNameChanged,
		PledgeStatusChanged,
		RelationChanged,
		EventTrigger,
		MultiSellList,
		SetSummonRemainTime,
		PackageSendableList,
		Earthquake,
		FlyToLocation,
		BlockList,
		SpecialCamera,
		NormalCamera,
		SkillRemainSec,
		NetPing,
		Dice,
		Snoop,
		RecipeBookItemList,
		RecipeItemMakeInfo,
		RecipeShopManageList,
		RecipeShopSellList,
		RecipeShopItemInfo,
		RecipeShopMsg,
		ShowCalc,
		MonRaceInfo,
		HennaItemInfo,
		HennaInfo,
		HennaUnequipList,
		HennaUnequipInfo,
		SendMacroList,
		BuyListSeed,
		ShowTownMap,
		ObserverStart,
		ObserverEnd,
		ChairSit,
		HennaEquipList,
		SellListProcure,
		GMHennaInfo,
		RadarControl,
		ClientSetTime,
		ConfirmDlg,
		PartySpelled,
		ShopPreviewList,
		ShopPreviewInfo,
		CameraMode,
		ShowXMasSeal,
		EtcStatusUpdate,
		ShortBuffStatusUpdate,
		SSQStatus,
		PetitionVote,
		AgitDecoInfo,
		Dummy_2,
		ExDummy,
		ExRegenMax,
		ExEventMatchUserInfo,
		ExColosseumFenceInfo,
		ExEventMatchSpelledInfo,
		ExEventMatchFirecracker,
		ExEventMatchTeamUnlocked,
		ExEventMatchGMTest,
		ExPartyRoomMember,
		ExClosePartyRoom,
		ExManagePartyRoomMember,
		ExEventMatchLockResult,
		ExAutoSoulShot,
		ExEventMatchList,
		ExEventMatchObserver,
		ExEventMatchMessage,
		ExEventMatchScore,
		ExServerPrimitive,
		ExOpenMPCC,
		ExCloseMPCC,
		ExShowCastleInfo,
		ExShowFortressInfo,
		ExShowAgitInfo,
		ExShowFortressSiegeInfo,
		ExPartyPetWindowAdd,
		ExPartyPetWindowUpdate,
		ExAskJoinMPCC,
		ExPledgeEmblem,
		ExEventMatchTeamInfo,
		ExEventMatchCreate,
		ExFishingStart,
		ExFishingEnd,
		ExShowQuestInfo,
		ExShowQuestMark,
		ExSendManorList,
		ExShowSeedInfo,
		ExShowCropInfo,
		ExShowManorDefaultInfo,
		ExShowSeedSetting,
		ExFishingStartCombat,
		ExFishingHpRegen,
		ExEnchantSkillList,
		ExEnchantSkillInfo,
		ExShowCropSetting,
		ExShowSellCropList,
		ExOlympiadMatchEnd,
		ExMailArrived,
		ExStorageMaxCount,
		ExEventMatchManage,
		ExMultiPartyCommandChannelInfo,
		ExPCCafePointInfo,
		ExSetCompassZoneCode,
		ExGetBossRecord,
		ExAskJoinPartyRoom,
		ExListPartyMatchingWaitingRoom,
		ExSetMpccRouting,
		ExShowAdventurerGuideBook,
		ExShowScreenMessage,
		PledgeSkillList,
		PledgeSkillListAdd,
		PledgeSkillListRemove,
		PledgePowerGradeList,
		PledgeReceivePowerInfo,
		PledgeReceiveMemberInfo,
		PledgeReceiveWarList,
		PledgeReceiveSubPledgeCreated,
		ExRedSky,
		PledgeReceiveUpdatePower,
		FlySelfDestination,
		ShowPCCafeCouponShowUI,
		ExSearchOrc,
		ExCursedWeaponList,
		ExCursedWeaponLocation,
		ExRestartClient,
		ExRequestHackShield,
		ExUseSharedGroupItem,
		ExMPCCShowPartyMemberInfo,
		ExDuelAskStart,
		ExDuelReady,
		ExDuelStart,
		ExDuelEnd,
		ExDuelUpdateUserInfo,
		ExShowVariationMakeWindow,
		ExShowVariationCancelWindow,
		ExPutItemResultForVariationMake,
		ExPutIntensiveResultForVariationMake,
		ExPutCommissionResultForVariationMake,
		ExVariationResult,
		ExPutItemResultForVariationCancel,
		ExVariationCancelResult,
		ExDuelEnemyRelation,
		ExPlayAnimation,
		ExMPCCPartyInfoUpdate,
		ExPlayScene,
		SpawnEmitter,
		ExEnchantSkillInfoDetail,
		ExBasicActionList,
		ExAirShipInfo,
		ExAttributeEnchantResult,
		ExChooseInventoryAttributeItem,
		ExGetOnAirShip,
		ExGetOffAirShip,
		ExMoveToLocationAirShip,
		ExStopMoveAirShip,
		ExShowTrace,
		ExItemAuctionInfo,
		ExNeedToChangeName,
		ExPartyPetWindowDelete,
		ExTutorialList,
		ExRpItemLink,
		ExMoveToLocationInAirShip,
		ExStopMoveInAirShip,
		ExValidateLocationInAirShip,
		ExUISetting,
		ExMoveToTargetInAirShip,
		ExAttackInAirShip,
		ExMagicSkillUseInAirShip,
		ExShowBaseAttributeCancelWindow,
		ExBaseAttributeCancelResult,
		ExSubPledgetSkillAdd,
		ExResponseFreeServer,
		ExShowProcureCropDetail,
		ExHeroList,
		ExOlympiadUserInfo,
		ExOlympiadSpelledInfo,
		ExOlympiadMode,
		ExShowFortressMapInfo,
		ExPVPMatchRecord,
		ExPVPMatchUserDie,
		ExPrivateStoreWholeMsg,
		ExPutEnchantTargetItemResult,
		ExPutEnchantSupportItemResult,
		ExChangeNicknameNColor,
		ExGetBookMarkInfo,
		ExNotifyPremiumItem,
		ExGetPremiumItemList,
		ExPeriodicItemList,
		ExJumpToLocation,
		ExPVPMatchCCRecord,
		ExPVPMatchCCMyRecord,
		ExPVPMatchCCRetire,
		ExShowTerritory,
		ExNpcQuestHtmlMessage,
		ExSendUIEvent,
		ExNotifyBirthDay,
		ExShowDominionRegistry,
		ExReplyRegisterDominion,
		ExReplyDominionInfo,
		ExShowOwnthingPos,
		ExCleftList,
		ExCleftState,
		ExDominionChannelSet,
		ExBlockUpSetList,
		ExBlockUpSetState,
		ExStartScenePlayer,
		ExAirShipTeleportList,
		ExMpccRoomInfo,
		ExListMpccWaiting,
		ExDissmissMpccRoom,
		ExManageMpccRoomMember,
		ExMpccRoomMember,
		ExVitalityPointInfo,
		ExShowSeedMapInfo,
		ExMpccPartymasterList,
		ExDominionWarStart,
		ExDominionWarEnd,
		ExShowLines,
		ExPartyMemberRenamed,
		ExEnchantSkillResult,
		ExRefundList,
		ExNoticePostArrived,
		ExShowReceivedPostList,
		ExReplyReceivedPost,
		ExShowSentPostList,
		ExReplySentPost,
		ExResponseShowStepOne,
		ExResponseShowStepTwo,
		ExResponseShowContents,
		ExShowPetitionHtml,
		ExReplyPostItemList,
		ExChangePostState,
		ExReplyWritePost,
		ExInitializeSeed,
		ExRaidReserveResult,
		ExBuySellList,
		ExCloseRaidSocket,
		ExPrivateMarketList,
		ExRaidCharacterSelected,
		ExAskCoupleAction,
		ExBrBroadcastEventState,
		ExBR_LoadEventTopRankers,
		ExChangeNpcState,
		ExAskModifyPartyLooting,
		ExSetPartyLooting,
		ExRotation,
		ExChangeClientEffectInfo,
		ExMembershipInfo,
		ExReplyHandOverPartyMaster,
		ExQuestNpcLogList,
		ExQuestItemList,
		ExGMViewQuestItemList,
		ExResartResponse,
		ExVoteSystemInfo,
		ExShuttleInfoPacket,
		ExShuttleGetOnPacket,
		ExShuttleGetOffPacket,
		ExShuttleMovePacket,
		ExMTLInShuttlePacket,
		ExStopMoveInShuttlePacket,
		ExValidateLocationInShuttlePacket,
		ExAgitAuctionCmd,
		ExConfirmAddingPostFriend,
		ExReceiveShowPostFriend,
		ExReceiveOlympiad,
		ExBR_GamePoint,
		ExBR_ProductList,
		ExBR_ProductInfo,
		ExBR_BuyProduct,
		ExBR_PremiumState,
		ExBR_ExtraUserInfo,
		ExBrBuffEventState,
		ExBR_RecentProductList,
		ExBR_MinigameLoadScores,
		ExBR_AgathionEnergyInfo,
		ExShowChannelingEffect,
		ExGetCrystalizingEstimation,
		ExGetCrystalizingFail,
		ExNavitAdventPointInfo,
		ExNavitAdventEffect,
		ExNavitAdventTimeChange,
		ExAbnormalStatusUpdateFromTargetPacket,
		ExStopScenePlayer,
		ExFlyMove,
		ExDynamicQuestPacket,
		ExSubjobInfo,
		ExChangeMPCost,
		ExFriendDetailInfo,
		ExBlockAddResult,
		ExBlockRemoveResult,
		ExBlockDefailInfo,
		ExLoadInzonePartyHistory,
		ExFriendNotifyNameChange,
		ExShowCommission,
		ExResponseCommissionItemList,
		ExResponseCommissionInfo,
		ExResponseCommissionRegister,
		ExResponseCommissionDelete,
		ExResponseCommissionList,
		ExResponseCommissionBuyInfo,
		ExResponseCommissionBuyItem,
		ExAcquirableSkillListByClass,
		ExMagicAttackInfo,
		ExAcquireSkillInfo,
		ExNewSkillToLearnByLevelUp,
		ExCallToChangeClass,
		ExChangeToAwakenedClass,
		ExTacticalSign,
		ExLoadStatWorldRank,
		ExLoadStatUser,
		ExLoadStatHotLink,
		ExWaitWaitingSubStituteInfo,
		ExRegistWaitingSubstituteOk,
		ExRegistPartySubstitute,
		ExDeletePartySubstitute,
		ExTimeOverPartySubstitute,
		ExGet24HzSessionID,
		Ex2NDPasswordCheck,
		Ex2NDPasswordVerify,
		Ex2NDPasswordAck,
		ExFlyMoveBroadcast,
		ExShowUsmVideo,
		ExShowStatPage,
		ExIsCharNameCreatable,
		ExGoodsInventoryChangedNotify,
		ExGoodsInventoryInfo,
		ExGoodsInventoryResult,
		ExAlterSkillRequest,
		ExNotifyFlyMoveStart,
		ExDummy_1,
		ExCloseCommission,
		ExChangeAttributeItemList,
		ExChangeAttributeInfo,
		ExChangeAttributeOk,
		ExChangeAttributeFail,
		ExExchangeSubstitute,
		ExLightingCandleEvent,
		ExVitalityEffectInfo,
		ExLoginVitalityEffectInfo,
		ExBR_PresentBuyProduct,
		ExMentorList,
		ExMentorAdd,
		ListMenteeWaiting,
		ExInzoneWaitingInfo,
		ExCuriousHouseState,
		ExCuriousHouseEnter,
		ExCuriousHouseLeave,
		ExCuriousHouseMemberList,
		ExCuriousHouseMemberUpdate,
		ExCuriousHouseRemainTime,
		ExCuriousHouseResult,
		ExCuriousHouseObserveList,
		ExCuriousHouseObserveMode,
		ExSysstring,
		ExChooseShapeShiftingItem,
		ExPutShapeShiftingTargetItemResult,
		ExPutShapeShiftingExtractionItemResult,
		ExShapeShiftingResult,
		ExCastleState,
		ExNCGuardReceiveDataFromServer,
		ExKalieEvent,

		ExPledgeUnionState,
		ExPledgeUnionFlow,
		ExPledgeUnionStateInfo,
		ExUnionPoint,

		ExKalieEventJackpotUser,

		ExAbnormalVisualEffectInfo,
		ExNpcSpeedInfo,
		ExSetPledgeEmblemAck,
		ExShowBeautyMenuPacket,
		ExResponseBeautyListPacket,
		ExResponseBeautyRegistResetPacket,
		ExResponseResetList,
		ExShuffleSeedAndPublicKey,
		ExCheck_SpeedHack,
		ExBR_NewIConCashBtnWnd,
		ExEvent_Campaign_Info,
		ExUnReadMailCount,
		ExPledgeCount,
		ExAdenaInvenCount,
		ExPledgeRecruitInfo,
		ExPledgeRecruitApplyInfo,
		ExPledgeRecruitBoardSearch,
		ExPledgeRecruitBoardDetail,
		ExPledgeWaitingListApplied,
		ExPledgeWaitingList,
		ExPledgeWaitingUser,
		ExPledgeDraftListSearch,
		ExPledgeWaitingListAlarm,
		ExValidateActiveCharacter,
		ExCloseCommissionRegister,
		ExTeleportToLocationActivate,
		ExNotifyWebPetitionReplyAlarm,
		ExEventShowXMasWishCard,
		ExPutEnchantScrollItemResult,
		ExRemoveEnchantSupportItemResult,
		ExShowCardRewardList,

		ExPartySmallWindowUpdateVital,
		ExPartySmallWindowUpdateHPMPCP,
		ExPartySmallWindowUpdateSubtitute,
		ExGmViewCharacterInfo,
		ExUserInfo,
		ExUserInfoEquipSlot,
		ExUserInfoCubic,
		ExUserInfoAbnormalVisualEffect,
		ExUserInfoFishing,
		ExPartySpelledInfoUpdate,
		ExDivideAdenaStart,
		ExDivideAdenaCancel,
		ExDivideAdenaDone,
		ExPetInfo,
		ExSummonInfo,
		ExNpcInfo,
		ExAcquirePotentialSkillList;
	}

	private static class SingletonHolder
	{
		protected static final ServerPacketOpCodeManager _instance = new ServerPacketOpCodeManager();
	}
}