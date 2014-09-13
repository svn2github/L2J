package l2next.gameserver.data.xml.holder;

import l2next.commons.data.xml.AbstractHolder;
import l2next.gameserver.model.items.etcitems.AppearanceStone;
import l2next.gameserver.model.items.etcitems.AttributeStone;
import l2next.gameserver.model.items.etcitems.EnchantScroll;
import l2next.gameserver.model.items.etcitems.LifeStone;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @user: Mifesto
 * @date: 11:01 / 03.05.13
 * @team: http://novell-project.ru/
 */
public class EnchantItemHolder extends AbstractHolder
{
	private static EnchantItemHolder _instance = new EnchantItemHolder();

	private IntObjectMap<LifeStone> _lifeStones = new HashIntObjectMap<LifeStone>();
	private IntObjectMap<EnchantScroll> _enchantScrolls = new HashIntObjectMap<EnchantScroll>();
	private IntObjectMap<AttributeStone> _attributeStones = new HashIntObjectMap<AttributeStone>();
	private IntObjectMap<AppearanceStone> _appearanceStones = new HashIntObjectMap<AppearanceStone>();

	private EnchantItemHolder()
	{
	}

	public static EnchantItemHolder getInstance()
	{
		return _instance;
	}

	public void addLifeStone(LifeStone lifeStone)
	{
		_lifeStones.put(lifeStone.getItemId(), lifeStone);
	}

	public void addEnchantScroll(EnchantScroll enchantScroll)
	{
		_enchantScrolls.put(enchantScroll.getItemId(), enchantScroll);
	}

	public void addAppearanceStone(AppearanceStone appearanceStone)
	{
		_appearanceStones.put(appearanceStone.getItemId(), appearanceStone);
	}

	public void addAttributeStone(AttributeStone attributeStone)
	{
		_attributeStones.put(attributeStone.getItemId(), attributeStone);
	}

	public LifeStone getLifeStone(int id)
	{
		return _lifeStones.get(id);
	}

	public EnchantScroll getEnchantScroll(int id)
	{
		return _enchantScrolls.get(id);
	}

	public AppearanceStone getAppearanceStone(int id)
	{
		return _appearanceStones.get(id);
	}

	public AttributeStone getAttributeStone(int id)
	{
		return _attributeStones.get(id);
	}

	public int[] getEnchantScrolls()
	{
		return _enchantScrolls.keySet().toArray();
	}

	public int[] getAppearanceStones()
	{
		return _appearanceStones.keySet().toArray();
	}

	public int[] getAttributeStones()
	{
		return _attributeStones.keySet().toArray();
	}

	public int[] getLifeStones()
	{
		return _lifeStones.keySet().toArray();
	}

	@Override
	public void log()
	{
		info("loaded " + _lifeStones.size() + " life stone(s) count.");
		info("loaded " + _enchantScrolls.size() + " enchant scroll(s) count.");
		info("loaded " + _attributeStones.size() + " attribute stone(s) count.");
		info("loaded " + _appearanceStones.size() + " appearance stone(s) count.");
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		_lifeStones.clear();
		_enchantScrolls.clear();
		_attributeStones.clear();
		_appearanceStones.clear();
	}
}