package npc.model;

import l2next.gameserver.Config;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.SubClass;
import l2next.gameserver.model.actor.instances.player.SubClassInfo;
import l2next.gameserver.model.actor.instances.player.SubClassList;
import l2next.gameserver.model.base.AcquireType;
import l2next.gameserver.model.base.ClassId;
import l2next.gameserver.model.entity.olympiad.Olympiad;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.network.serverpackets.components.SystemMsg;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.CertificationFunctions;
import l2next.gameserver.utils.HtmlUtils;
import l2next.gameserver.utils.ItemFunctions;

import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;

public final class SubClassManagerInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;

	// Предмет: Сертификат на Смену Профессии
	private static final int CERTIFICATE_ID = 30433;

	public SubClassManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
		{
			return;
		}

		StringTokenizer st = new StringTokenizer(command, "_");
		String cmd = st.nextToken();
		if(cmd.equalsIgnoreCase("subclass"))
		{
			if(player.getPets().size() > 0)
			{
				showChatWindow(player, "class_manager/" + getNpcId() + "-no_servitor.htm");
				return;
			}

			if(player.getTransformation() != 0)
			{
				showChatWindow(player, "class_manager/" + getNpcId() + "-no_transform.htm");
				return;
			}

			if(player.getWeightPenalty() >= 3 || player.getInventoryLimit() * 0.8 < player.getInventory().getSize())
			{
				showChatWindow(player, "class_manager/" + getNpcId() + "-no_weight.htm");
				return;
			}

			if(player.getLevel() < 40)
			{
				showChatWindow(player, "class_manager/" + getNpcId() + "-no_level.htm");
				return;
			}

			String cmd2 = st.nextToken();
			if(cmd2.equalsIgnoreCase("add"))
			{
				if(!checkSubClassQuest(player))
				{
					showChatWindow(player, "class_manager/" + getNpcId() + "-no_quest.htm");
					return;
				}

				if(player.getSubClassList().size() >= SubClassList.MAX_SUB_COUNT)
				{
					showChatWindow(player, "class_manager/" + getNpcId() + "-add_no_limit.htm");
					return;
				}

				// Проверка хватает ли уровня
				Collection<SubClass> subClasses = player.getSubClassList().values();
				for(SubClass subClass : subClasses)
				{
					if(subClass.getLevel() < Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS)
					{
						showChatWindow(player, "class_manager/" + getNpcId() + "-add_no_level.htm", "<?LEVEL?>", Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS);
						return;
					}
				}

				if(!st.hasMoreTokens())
				{
					StringBuilder availSubList = new StringBuilder();
					//int[] availSubClasses = SubClassTable.getInstance().getAvailableSubClasses(player, player.getActiveClassId());
					Set<ClassId> availSubClasses = SubClassInfo.getAvailableSubClasses(player, player.getRace(), null, true);
					for(ClassId subClsId : availSubClasses)
					{
						availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_add_" + subClsId.getId() + "\">" + HtmlUtils.htmlClassName(subClsId.getId()) + "</a><br>");
					}
					showChatWindow(player, "class_manager/" + getNpcId() + "-add_list.htm", "<?ADD_SUB_LIST?>", availSubList.toString());
					return;
				}
				else
				{
					int addSubClassId = Integer.parseInt(st.nextToken());
					if(!st.hasMoreTokens())
					{
						String addSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_add_" + addSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(addSubClassId) + "</a>";
						showChatWindow(player, "class_manager/" + getNpcId() + "-add_confirm.htm", "<?ADD_SUB_CONFIRM?>", addSubConfirm);
						return;
					}
					else
					{
						String cmd3 = st.nextToken();
						if(cmd3.equalsIgnoreCase("confirm"))
						{
							//TODO: [K1mel] Проверить на оффе.
							if(Config.ENABLE_OLYMPIAD && Olympiad.isRegisteredInComp(player))
							{
								player.sendPacket(SystemMsg.C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_SUBCLASS_CHARACTER_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD);
								return;
							}

							if(player.addSubClass(addSubClassId, true, 0))
							{
								player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
								showChatWindow(player, "class_manager/" + getNpcId() + "-add_success.htm");
								return;
							}
							else
							{
								showChatWindow(player, "class_manager/" + getNpcId() + "-add_error.htm");
								return;
							}
						}
					}
				}
			}
			else if(cmd2.equalsIgnoreCase("change")) // TODO: [K1mel] Доделать.
			{
				if(!player.getSubClassList().haveSubClasses())
				{
					showChatWindow(player, "class_manager/" + getNpcId() + "-no_quest.htm");
					return;
				}

				if(ItemFunctions.getItemCount(player, CERTIFICATE_ID) == 0)
				{
					showChatWindow(player, "class_manager/" + getNpcId() + "-no_certificate.htm");
					return;
				}
			}
			else if(cmd2.equalsIgnoreCase("cancel"))
			{
				if(!checkSubClassQuest(player) && !player.getSubClassList().haveSubClasses())
				{
					showChatWindow(player, "class_manager/" + getNpcId() + "-no_quest.htm");
					return;
				}

				if(checkSubClassQuest(player) && !player.getSubClassList().haveSubClasses()) // TODO: [K1mel] Проверить сообщение на оффе.
				{
					showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_no_subs.htm");
					return;
				}

				if(!st.hasMoreTokens())
				{
					StringBuilder mySubList = new StringBuilder();
					Collection<SubClass> subClasses = player.getSubClassList().values();
					for(SubClass sub : subClasses)
					{
						if(sub == null) // Не может быть, но на всякий пожарный.
						{
							continue;
						}

						if(sub.isBase())
						{
							continue;
						}

						if(sub.isDouble()) // Двойной саб-класс отменить нельзя.
						{
							continue;
						}

						int classId = sub.getClassId();
						mySubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + classId + "\">" + HtmlUtils.htmlClassName(classId) + "</a><br>");
					}
					showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_list.htm", "<?CANCEL_SUB_LIST?>", mySubList.toString());
					return;
				}
				else
				{
					int cancelClassId = Integer.parseInt(st.nextToken());
					if(!st.hasMoreTokens())
					{
						StringBuilder availSubList = new StringBuilder();
						//int[] availSubClasses = SubClassTable.getInstance().getAvailableSubClasses(player, cancelClassId);
						Set<ClassId> availSubClasses = SubClassInfo.getAvailableSubClasses(player, player.getRace(), null, true);
						for(ClassId subClsId : availSubClasses)
						{
							availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + cancelClassId + "_" + subClsId.getId() + "\">" + HtmlUtils.htmlClassName(subClsId.getId()) + "</a><br>");
						}
						showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_change_list.htm", "<?CANCEL_CHANGE_SUB_LIST?>", availSubList.toString());
						return;
					}
					else
					{
						int newSubClassId = Integer.parseInt(st.nextToken());
						if(!st.hasMoreTokens())
						{
							String newSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + cancelClassId + "_" + newSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(newSubClassId) + "</a>";
							showChatWindow(player, "class_manager/" + getNpcId() + "-cancel_confirm.htm", "<?CANCEL_SUB_CONFIRM?>", newSubConfirm);
							return;
						}
						else
						{
							String cmd3 = st.nextToken();
							if(cmd3.equalsIgnoreCase("confirm"))
							{
								if(player.modifySubClass(cancelClassId, newSubClassId))
								{
									player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
									showChatWindow(player, "class_manager/" + getNpcId() + "-add_success.htm");
									return;
								}
								else
								{
									showChatWindow(player, "class_manager/" + getNpcId() + "-add_error.htm");
									return;
								}
							}
						}
					}
				}
			}
			else if(cmd2.equalsIgnoreCase("CertificationList"))
			{
				CertificationFunctions.showCertificationList(this, player);
			}
			else if(cmd2.equalsIgnoreCase("GetCertification65"))
			{
				CertificationFunctions.getCertification65(this, player);
			}
			else if(cmd2.equalsIgnoreCase("GetCertification70"))
			{
				CertificationFunctions.getCertification70(this, player);
			}
			else if(cmd2.equalsIgnoreCase("GetCertification80"))
			{
				CertificationFunctions.getCertification80(this, player);
			}
			else if(cmd2.equalsIgnoreCase("GetCertification75List"))
			{
				CertificationFunctions.getCertification75List(this, player);
			}
			else if(cmd2.equalsIgnoreCase("GetCertification75C"))
			{
				CertificationFunctions.getCertification75(this, player, true);
			}
			else if(cmd2.equalsIgnoreCase("GetCertification75M"))
			{
				CertificationFunctions.getCertification75(this, player, false);
			}
			else if(cmd2.equalsIgnoreCase("CertificationSkillList"))
			{
				showSertifikationSkillList(player, AcquireType.CERTIFICATION);
			}
			else if(cmd2.equalsIgnoreCase("CertificationCancel"))
			{
				CertificationFunctions.cancelCertification(this, player);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile("class_manager/33491.htm");
		player.sendPacket(html);
	}

	public void showSertifikationSkillList(Player player, AcquireType type)
	{
		if(!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
		{
			if(!player.isQuestCompleted("_136_MoreThanMeetsTheEye"))
			{
				showChatWindow(player, "trainer/" + getNpcId() + "-noquest.htm");
				return;
			}
		}

		showAcquireList(type, player);
	}

	private static boolean checkSubClassQuest(Player player)
	{
		if(!Config.ALT_GAME_SUBCLASS_WITHOUT_QUESTS)
		{
			if(!player.isQuestCompleted("_10385_RedThreadofFate"))
			{
				return false;
			}
		}
		return true;
	}
}