package l2next.gameserver.model;

import l2next.gameserver.instancemanager.WorldStatisticsManager;
import l2next.gameserver.model.worldstatistics.CharacterStatistic;
import l2next.gameserver.network.serverpackets.ExLoadStatHotLink;
import l2next.gameserver.network.serverpackets.L2GameServerPacket;
import l2next.gameserver.network.serverpackets.ServerObjectInfo;
import l2next.gameserver.templates.StatuesSpawnTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Дмитрий
 * @date 17.10.12 0:16
 */
public final class WinnerStatueInstance extends GameObject
{
	private StatuesSpawnTemplate template;

	public WinnerStatueInstance(int objectId, StatuesSpawnTemplate template)
	{
		super(objectId);
		this.template = template;
	}

	@Override
	public boolean isAttackable(Creature creature)
	{
		return false;
	}

	@Override
	public double getColRadius()
	{
		return 30.0;
	}

	@Override
	public double getColHeight()
	{
		return 40.0;
	}

	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		//List<L2GameServerPacket> list = new ArrayList<>(1);
		List<L2GameServerPacket> list = new ArrayList<L2GameServerPacket>(1);
		list.add(new ServerObjectInfo(this, forPlayer));
		return list;
	}

	@Override
	public void onAction(Player player, boolean shift)
	{
		List<CharacterStatistic> globalStatistic = WorldStatisticsManager.getInstance().getWinners(template.getCategoryType(), true, WorldStatisticsManager.STATUES_TOP_PLAYER_LIMIT);
		List<CharacterStatistic> monthlyStatistic = WorldStatisticsManager.getInstance().getWinners(template.getCategoryType(), false, WorldStatisticsManager.STATUES_TOP_PLAYER_LIMIT);
		player.sendPacket(new ExLoadStatHotLink(template.getCategoryType().getClientId(), template.getCategoryType().getSubcat(), globalStatistic, monthlyStatistic));
		player.sendActionFailed();
	}

	public StatuesSpawnTemplate getTemplate()
	{
		return template;
	}
}
