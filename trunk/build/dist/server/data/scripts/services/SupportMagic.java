package services;

import l2next.commons.collections.GArray;
import l2next.gameserver.model.Creature;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.Summon;
import l2next.gameserver.model.base.ClassLevel;
import l2next.gameserver.model.base.Race;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.MagicSkillUse;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.tables.SkillTable;

public class SupportMagic extends Functions
{
	private final static int[][] _mageBuff = new int[][]{
		// minlevel maxlevel skill skilllevel
		{
			1,
			75,
			4322,
			1
		},
		// windwalk
		{
			1,
			75,
			4323,
			1
		},
		// shield
		{
			1,
			75,
			5637,
			1
		},
		// Magic Barrier 1
		{
			1,
			75,
			4328,
			1
		},
		// blessthesoul
		{
			1,
			75,
			4329,
			1
		},
		// acumen
		{
			1,
			75,
			4330,
			1
		},
		// concentration
		{
			1,
			75,
			4331,
			1
		},
		// empower
		{
			16,
			34,
			4338,
			1
		},
		// life cubic
	};

	private final static int[][] _warrBuff = new int[][]{
		// minlevel maxlevel skill
		{
			1,
			75,
			4322,
			1
		},
		// windwalk
		{
			1,
			75,
			4323,
			1
		},
		// shield
		{
			1,
			75,
			5637,
			1
		},
		// Magic Barrier 1
		{
			1,
			75,
			4324,
			1
		},
		// btb
		{
			1,
			75,
			4325,
			1
		},
		// vampirerage
		{
			1,
			75,
			4326,
			1
		},
		// regeneration
		{
			1,
			39,
			4327,
			1
		},
		// haste 1
		{
			40,
			75,
			5632,
			1
		},
		// haste 2
		{
			16,
			34,
			4338,
			1
		},
		// life cubic
	};

	private final static int[][] _summonBuff = new int[][]{
		// minlevel maxlevel skill
		{
			1,
			75,
			4322,
			1
		},
		// windwalk
		{
			1,
			75,
			4323,
			1
		},
		// shield
		{
			1,
			75,
			5637,
			1
		},
		// Magic Barrier 1
		{
			1,
			75,
			4324,
			1
		},
		// btb
		{
			1,
			75,
			4325,
			1
		},
		// vampirerage
		{
			1,
			75,
			4326,
			1
		},
		// regeneration
		{
			1,
			75,
			4328,
			1
		},
		// blessthesoul
		{
			1,
			75,
			4329,
			1
		},
		// acumen
		{
			1,
			75,
			4330,
			1
		},
		// concentration
		{
			1,
			75,
			4331,
			1
		},
		// empower
		{
			1,
			39,
			4327,
			1
		},
		// haste 1
		{
			40,
			75,
			5632,
			1
		},
		// haste 2
	};

	private final static int minSupLvl = 1;
	private final static int maxSupLvl = 75;

	public void getSupportMagic()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();

		doSupportMagic(npc, player, false);
	}

	public void getSupportServitorMagic()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();

		doSupportMagic(npc, player, true);
	}

	public void getProtectionBlessing()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();

		// Не выдаём блессиг протекшена ПКшникам.
		if(player.getKarma() > 0)
		{
			return;
		}
		if(player.getLevel() > 39 || player.getClassId().isOfLevel(ClassLevel.Second))
		{
			show("default/newbie_blessing_no.htm", player, npc);
			return;
		}
		npc.doCast(SkillTable.getInstance().getInfo(5182, 1), player, true);
	}

	public static void doSupportMagic(NpcInstance npc, Player player, boolean servitor)
	{
		// Prevent a cursed weapon weilder of being buffed
		if(player.isCursedWeaponEquipped())
		{
			return;
		}
		int lvl = player.getLevel();

		if(servitor && (player.getFirstPet() == null || !player.getFirstPet().isSummon()))
		{
			show("default/newbie_nosupport_servitor.htm", player, npc);
			return;
		}
		else
		{
			if(lvl < minSupLvl)
			{
				show("default/newbie_nosupport_min.htm", player, npc);
				return;
			}
			if(lvl > maxSupLvl)
			{
				show("default/newbie_nosupport_max.htm", player, npc);
				return;
			}
		}

		GArray<Creature> target = new GArray<Creature>();

		if(servitor)
		{
			for(Summon summon : player.getPets())
			{
				target.add(summon);

				for(int[] buff : _summonBuff)
				{
					if(lvl >= buff[0] && lvl <= buff[1])
					{
						npc.broadcastPacket(new MagicSkillUse(npc, summon, buff[2], buff[3], 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
					}
				}
			}
		}
		else
		{
			target.add(player);

			if(!player.isMageClass() || player.getTemplate().getRace() == Race.orc)
			{
				for(int[] buff : _warrBuff)
				{
					if(lvl >= buff[0] && lvl <= buff[1])
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
					}
				}
			}
			else
			{
				for(int[] buff : _mageBuff)
				{
					if(lvl >= buff[0] && lvl <= buff[1])
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
					}
				}
			}
		}
	}

}
