package l2next.gameserver.data.xml;

import l2next.gameserver.data.StringHolder;
import l2next.gameserver.data.htm.HtmCache;
import l2next.gameserver.data.xml.holder.BuyListHolder;
import l2next.gameserver.data.xml.holder.MultiSellHolder;
import l2next.gameserver.data.xml.holder.ProductHolder;
import l2next.gameserver.data.xml.holder.RecipeHolder;
import l2next.gameserver.data.xml.parser.AirshipDockParser;
import l2next.gameserver.data.xml.parser.ArmorItemParser;
import l2next.gameserver.data.xml.parser.ArmorSetsParser;
import l2next.gameserver.data.xml.parser.ClassDataParser;
import l2next.gameserver.data.xml.parser.CubicParser;
import l2next.gameserver.data.xml.parser.DomainParser;
import l2next.gameserver.data.xml.parser.DoorParser;
import l2next.gameserver.data.xml.parser.DropListDataParser;
import l2next.gameserver.data.xml.parser.EnchantItemParser;
import l2next.gameserver.data.xml.parser.EtcItemParser;
import l2next.gameserver.data.xml.parser.EventParser;
import l2next.gameserver.data.xml.parser.FishDataParser;
import l2next.gameserver.data.xml.parser.HennaParser;
import l2next.gameserver.data.xml.parser.InstantZoneParser;
import l2next.gameserver.data.xml.parser.ItemLevelParser;
import l2next.gameserver.data.xml.parser.JumpTracksParser;
import l2next.gameserver.data.xml.parser.LevelBonusParser;
import l2next.gameserver.data.xml.parser.NpcParser;
import l2next.gameserver.data.xml.parser.OptionDataParser;
import l2next.gameserver.data.xml.parser.PetitionGroupParser;
import l2next.gameserver.data.xml.parser.PlayerTemplateParser;
import l2next.gameserver.data.xml.parser.RecipeParser;
import l2next.gameserver.data.xml.parser.ResidenceParser;
import l2next.gameserver.data.xml.parser.RestartPointParser;
import l2next.gameserver.data.xml.parser.ShuttleTemplateParser;
import l2next.gameserver.data.xml.parser.SkillAcquireParser;
import l2next.gameserver.data.xml.parser.SoulCrystalParser;
import l2next.gameserver.data.xml.parser.SpawnParser;
import l2next.gameserver.data.xml.parser.SpawnsData;
import l2next.gameserver.data.xml.parser.StartItemParser;
import l2next.gameserver.data.xml.parser.StaticObjectParser;
import l2next.gameserver.data.xml.parser.StatuesSpawnParser;
import l2next.gameserver.data.xml.parser.SummonPointsParser;
import l2next.gameserver.data.xml.parser.WalkerRoutesParser;
import l2next.gameserver.data.xml.parser.WalkerAgroParser;
import l2next.gameserver.data.xml.parser.WeaponItemParser;
import l2next.gameserver.data.xml.parser.ZoneParser;
import l2next.gameserver.instancemanager.ReflectionManager;
import l2next.gameserver.tables.SkillTable;
import l2next.gameserver.tables.SpawnTable;

/**
 * @author VISTALL
 * @date 20:55/30.11.2010
 */
public abstract class Parsers
{
	public static void parseAll()
	{
		HtmCache.getInstance().reload();
		StringHolder.getInstance().load();

		SkillTable.getInstance().load(); // - SkillParser.getInstance();
		OptionDataParser.getInstance().load();
		EtcItemParser.getInstance().load();
		WeaponItemParser.getInstance().load();
		ArmorItemParser.getInstance().load();
		EnchantItemParser.getInstance().load();

		//
		NpcParser.getInstance().load();
		DoorParser.getInstance().load();

		DomainParser.getInstance().load();
		RestartPointParser.getInstance().load();

		StaticObjectParser.getInstance().load();
		ZoneParser.getInstance().load();
		SpawnTable.getInstance();
		SpawnsData.getInstance().load();
		SpawnParser.getInstance().load();
		InstantZoneParser.getInstance().load();
		WalkerRoutesParser.getInstance().load();
		WalkerAgroParser.getInstance().load();
		DropListDataParser.getInstance().load();
		RecipeParser.getInstance().load();

		ReflectionManager.getInstance();
		//
		AirshipDockParser.getInstance().load();
		SkillAcquireParser.getInstance().load();
		//
		ResidenceParser.getInstance().load();
		ShuttleTemplateParser.getInstance().load();
		EventParser.getInstance().load();
		// support(cubic & agathion)
		CubicParser.getInstance().load();
		//
		StartItemParser.getInstance().load();
		ItemLevelParser.getInstance().load();

		BuyListHolder.getInstance();
		RecipeHolder.getInstance();
		MultiSellHolder.getInstance();
		ProductHolder.getInstance();
		// AgathionParser.getInstance();
		// item support
		HennaParser.getInstance().load();
		JumpTracksParser.getInstance().load();
		SoulCrystalParser.getInstance().load();
		ArmorSetsParser.getInstance().load();
		FishDataParser.getInstance().load();

		// etc
		PetitionGroupParser.getInstance().load();
		// Player templates
		PlayerTemplateParser.getInstance().load();
		// Classes load
		ClassDataParser.getInstance().load();
		// LvL bonus load
		LevelBonusParser.getInstance().load();

		SummonPointsParser.getInstance().load();

		StatuesSpawnParser.getInstance().load();

	}
}
