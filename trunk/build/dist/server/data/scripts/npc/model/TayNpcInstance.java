package npc.model;

import l2next.gameserver.instancemanager.ReflectionManager;
import l2next.commons.threading.RunnableImpl;
import l2next.gameserver.model.*;
import l2next.gameserver.listener.actor.OnDeathListener;
import l2next.gameserver.model.entity.Reflection;
import l2next.gameserver.model.instances.NpcInstance;
import l2next.gameserver.network.serverpackets.components.NpcString;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage;
import l2next.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2next.gameserver.network.serverpackets.NpcHtmlMessage;
import l2next.gameserver.scripts.Functions;
import l2next.gameserver.templates.npc.NpcTemplate;
import l2next.gameserver.utils.Location;
import l2next.gameserver.utils.ReflectionUtils;

import instances.Sansililion;

/**
 * Для работы с инстами - SOA
 */
public final class TayNpcInstance extends NpcInstance
{
	private static final long serialVersionUID = 5984176213940365077L;
	
	private static final int soaSansilion = 171;
	private int Creature2 = 23034;
	private int Creature3 = 23035;
	private int Creature4 = 23036;
	private int Creature5 = 23037;
	private int Creature1 = 23033;
	private int count;
	private DeathListener _deathListener = new DeathListener();
	private int reward;
	private static final Location LOC_OUT = new Location(-178465, 153685, 2488);

	public TayNpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("request_startWorld"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(r instanceof Sansililion)
				{
					Sansililion sInst = (Sansililion) r;
					if(sInst.getStatus() == 0)
						sInst.startWorld();
				}
				NpcInstance C1 = getReflection().addSpawnWithRespawn(Creature5, new Location(-187080, 148600, -15337, 26634), 1, 30);
				C1.addListener(_deathListener);
				NpcInstance C2 = getReflection().addSpawnWithRespawn(Creature3, new Location(-187432, 148456, -15336, 36818), 1, 30);
				C2.addListener(_deathListener);
				NpcInstance C3 = getReflection().addSpawnWithRespawn(Creature5, new Location(-187176, 148056, -15337, 55191), 1, 30);
				C3.addListener(_deathListener);
				NpcInstance C4 = getReflection().addSpawnWithRespawn(Creature3, new Location(-187384, 147560, -15337, 41319), 1, 30);
				C4.addListener(_deathListener);
				NpcInstance C5 = getReflection().addSpawnWithRespawn(Creature1, new Location(-187032, 146968, -15337, 54933), 1, 30);
				C5.addListener(_deathListener);
				NpcInstance C6 = getReflection().addSpawnWithRespawn(Creature2, new Location(-186824, 147176, -15337, 8191), 1, 30);
				C6.addListener(_deathListener);
				NpcInstance C7 = getReflection().addSpawnWithRespawn(Creature3, new Location(-186856, 146456, -15336, 47722), 1, 30);
				C7.addListener(_deathListener);
				NpcInstance C8 = getReflection().addSpawnWithRespawn(Creature3, new Location(-186392, 146568, -15337, 2470), 1, 30);
				C8.addListener(_deathListener);
				NpcInstance C9 = getReflection().addSpawnWithRespawn(Creature1, new Location(-186104, 146888, -15337, 8740), 1, 30);
				C9.addListener(_deathListener);
				NpcInstance A1 = getReflection().addSpawnWithRespawn(Creature4, new Location(-185720, 146120, -15336, 49803), 1, 30);
				A1.addListener(_deathListener);
				NpcInstance A2 = getReflection().addSpawnWithRespawn(Creature4, new Location(-185560, 146376, -15330, 10557), 1, 30);
				A2.addListener(_deathListener);
				NpcInstance A3 = getReflection().addSpawnWithRespawn(Creature3, new Location(-185576, 146712, -15337, 13828), 1, 30);
				A3.addListener(_deathListener);
				NpcInstance A4 = getReflection().addSpawnWithRespawn(Creature4, new Location(-185096, 146664, -15332, 64381), 1, 30);
				A4.addListener(_deathListener);
				NpcInstance A5 = getReflection().addSpawnWithRespawn(Creature3, new Location(-184856, 146520, -15336, 57956), 1, 30);
				A5.addListener(_deathListener);
				NpcInstance A6 = getReflection().addSpawnWithRespawn(Creature2, new Location(-184840, 147032, -15335, 15640), 1, 30);
				A6.addListener(_deathListener);
				NpcInstance A7 = getReflection().addSpawnWithRespawn(Creature1, new Location(-184872, 147400, -15325, 17288), 1, 30);
				A7.addListener(_deathListener);
				NpcInstance A8 = getReflection().addSpawnWithRespawn(Creature2, new Location(-184328, 147048, -15336, 60384), 1, 30);
				A8.addListener(_deathListener);
				NpcInstance A9 = getReflection().addSpawnWithRespawn(Creature1, new Location(-184248, 147448, -15335, 14325), 1, 30);
				A9.addListener(_deathListener);

				NpcInstance B1 = getReflection().addSpawnWithRespawn(Creature4, new Location(-184584, 147656, -15337, 26986), 1, 30);
				B1.addListener(_deathListener);
				NpcInstance B2 = getReflection().addSpawnWithRespawn(Creature4, new Location(-184408, 147784, -15337, 24575), 1, 30);
				B2.addListener(_deathListener);
				NpcInstance B3 = getReflection().addSpawnWithRespawn(Creature1, new Location(-184120, 148040, -15336, 13028), 1, 30);
				B3.addListener(_deathListener);
				NpcInstance B4 = getReflection().addSpawnWithRespawn(Creature2, new Location(-184536, 148184, -15337, 29292), 1, 30);
				B4.addListener(_deathListener);
				NpcInstance B5 = getReflection().addSpawnWithRespawn(Creature2, new Location(-184264, 148456, -15336, 9137), 1, 30);
				B5.addListener(_deathListener);
				NpcInstance B6 = getReflection().addSpawnWithRespawn(Creature1, new Location(-184872, 148600, -15337, 27714), 1, 30);
				B6.addListener(_deathListener);
				NpcInstance B7 = getReflection().addSpawnWithRespawn(Creature4, new Location(-184584, 148744, -15337, 4836), 1, 30);
				B7.addListener(_deathListener);
				NpcInstance B8 = getReflection().addSpawnWithRespawn(Creature5, new Location(-184488, 149080, -15335, 13481), 1, 30);
				B8.addListener(_deathListener);
				NpcInstance B9 = getReflection().addSpawnWithRespawn(Creature4, new Location(-184696, 149128, -15336, 30402), 1, 30);
				B9.addListener(_deathListener);
				NpcInstance D1 = getReflection().addSpawnWithRespawn(Creature3, new Location(-184984, 148904, -15337, 39662), 1, 30);
				D1.addListener(_deathListener);
				NpcInstance D2 = getReflection().addSpawnWithRespawn(Creature1, new Location(-185080, 149272, -15337, 19045), 1, 30);
				D2.addListener(_deathListener);
				NpcInstance D3 = getReflection().addSpawnWithRespawn(Creature2, new Location(-185048, 149576, -15335, 15290), 1, 30);
				D3.addListener(_deathListener);
				NpcInstance D4 = getReflection().addSpawnWithRespawn(Creature5, new Location(-185368, 149560, -15336, 33289), 1, 30);
				D4.addListener(_deathListener);
				NpcInstance D5 = getReflection().addSpawnWithRespawn(Creature4, new Location(-185608, 149288, -15337, 41611), 1, 30);
				D5.addListener(_deathListener);
				NpcInstance D6 = getReflection().addSpawnWithRespawn(Creature3, new Location(-185448, 149160, -15337, 58498), 1, 30);
				D6.addListener(_deathListener);
				NpcInstance D7 = getReflection().addSpawnWithRespawn(Creature2, new Location(-185816, 149080, -15337, 35000), 1, 30);
				D7.addListener(_deathListener);
				NpcInstance D8 = getReflection().addSpawnWithRespawn(Creature1, new Location(-185928, 149480, -15336, 14325), 1, 30);
				D8.addListener(_deathListener);
				NpcInstance D9 = getReflection().addSpawnWithRespawn(Creature2, new Location(-186232, 149656, -15335, 27294), 1, 30);
				D9.addListener(_deathListener);
				NpcInstance E1 = getReflection().addSpawnWithRespawn(Creature5, new Location(-186536, 149400, -15336, 40068), 1, 30);
				E1.addListener(_deathListener);
				NpcInstance E2 = getReflection().addSpawnWithRespawn(Creature3, new Location(-186440, 149032, -15337, 51813), 1, 30);
				E2.addListener(_deathListener);
				NpcInstance E3 = getReflection().addSpawnWithRespawn(Creature3, new Location(-186792, 149160, -15337, 29130), 1, 30);
				E3.addListener(_deathListener);
				NpcInstance E4 = getReflection().addSpawnWithRespawn(Creature5, new Location(-187000, 149320, -15335, 25928), 1, 30);
				E4.addListener(_deathListener);
				NpcInstance E5 = getReflection().addSpawnWithRespawn(Creature4, new Location(-187128, 148904, -15336, 46038), 1, 30);
				E5.addListener(_deathListener);
				NpcInstance E6 = getReflection().addSpawnWithRespawn(Creature2, new Location(-186872, 148792, -15337, 61312), 1, 30);
				E6.addListener(_deathListener);
				NpcInstance E7 = getReflection().addSpawnWithRespawn(Creature1, new Location(-186536, 148808, -15337, 3355), 1, 30);
				E7.addListener(_deathListener);
				NpcInstance E8 = getReflection().addSpawnWithRespawn(Creature1, new Location(-187016, 148328, -15337, 39714), 1, 30);
				E8.addListener(_deathListener);
				NpcInstance E9 = getReflection().addSpawnWithRespawn(Creature3, new Location(-187096, 147384, -15335, 50306), 1, 30);
				E9.addListener(_deathListener);
				NpcInstance F1 = getReflection().addSpawnWithRespawn(Creature3, new Location(-186664, 146856, -15337, 6469), 1, 30);
				F1.addListener(_deathListener);
				NpcInstance F2 = getReflection().addSpawnWithRespawn(Creature2, new Location(-186520, 146328, -15336, 52054), 1, 30);
				F2.addListener(_deathListener);
				NpcInstance F3 = getReflection().addSpawnWithRespawn(Creature1, new Location(-185992, 146264, -15336, 64277), 1, 30);
				F3.addListener(_deathListener);
				NpcInstance F4 = getReflection().addSpawnWithRespawn(Creature3, new Location(-185864, 146696, -15335, 13379), 1, 30);
				F4.addListener(_deathListener);
				NpcInstance F5 = getReflection().addSpawnWithRespawn(Creature5, new Location(-184312, 146792, -15335, 65119), 1, 30);
				F5.addListener(_deathListener);
			}
		}
		else if(command.equalsIgnoreCase("request_exchange"))
		{
			Reflection r = player.getActiveReflection();
			if(r != null)
			{
				if(r instanceof Sansililion)
				{
					Sansililion sInst = (Sansililion) r;
					if(sInst.getStatus() == 2 || sInst.getStatus() == 1)
					{
						int amount = 0;
						int points = sInst._points;
						if(points > 1 && points < 800)
							amount = 10;
						else if(points < 1600)
							amount = 60;
						else if(points < 2000)
							amount = 160;
						else if(points < 2000)
							amount = 160;
						else if(points < 2400)
							amount = 200;
						else if(points < 2800)
							amount = 240;
						else if(points < 3200)
							amount = 280;
						else if(points < 3600)
							amount = 320;
						else if(points < 4000)
							amount = 360;
						else if(points > 4000)
							amount = 400;

						if(amount > 0)
							Functions.addItem(player, 17602, amount);

						player.teleToLocation(LOC_OUT, ReflectionManager.DEFAULT);
						sInst.collapse();
					}
				}
			}
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	private class DeathListener implements OnDeathListener
	{
		private NpcInstance exchange;

		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc())
			{
				if(self.getNpcId() == Creature1 || self.getNpcId() == Creature2 || self.getNpcId() == Creature3 || self.getNpcId() == Creature4 || self.getNpcId() == Creature5)
				{
					count = reward + 5;
					reward = count++;
					Player player = killer.getPlayer();
					Reflection r = player.getActiveReflection();
					if(r != null)
					{
						if(r instanceof Sansililion)
						{
							Sansililion sInst = (Sansililion) r;
							sInst.setPoints(count);
							sInst.updateTimer();
						}
					}
				}
			}
		}
	}

	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		Reflection r = player.getActiveReflection();
		if(r != null)
		{
			if(r instanceof Sansililion)
			{
				Sansililion sInst = (Sansililion) r;
				if(sInst.getStatus() == 2)
				{
					html.setFile("default/33152-1.htm");
				}
				else if(sInst.getStatus() == 1)
				{
					html.setFile("default/33152-1.htm");
					if(player.getEffectList().getEffectsCountForSkill(14228) != 0)
					{
						player.sendPacket(new ExShowScreenMessage(NpcString.SOLDER_TIE_RECEIVED_S1_PRIECES_OF_BIO_ENERGY_RESIDUE, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false, 1, -1, false, "40"));
						player.getEffectList().stopEffect(14228);
						sInst._points += 40;
						sInst._lastBuff = 0;
					}
					else
					{
						if(player.getEffectList().getEffectsCountForSkill(14229) != 0)
						{
							player.sendPacket(new ExShowScreenMessage(NpcString.SOLDER_TIE_RECEIVED_S1_PRIECES_OF_BIO_ENERGY_RESIDUE, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false, 1, -1, false, "60"));							
							player.getEffectList().stopEffect(14229);
							sInst._points += 60;
							sInst._lastBuff = 0;
						}
						else
						{
							if(player.getEffectList().getEffectsCountForSkill(14230) != 0)
							{
								player.sendPacket(new ExShowScreenMessage(NpcString.SOLDER_TIE_RECEIVED_S1_PRIECES_OF_BIO_ENERGY_RESIDUE, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false, 1, -1, false, "80"));
								player.getEffectList().stopEffect(14230);
								sInst._points += 80;
								sInst._lastBuff = 0;
							}
						}
					}
				}
				else
				{
					html.setFile("default/33152-0.htm");
				}
				player.sendPacket(html);
			}
		}
	}
}
