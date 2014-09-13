package l2next.gameserver.model.items.etcitems;

import l2next.gameserver.templates.item.ItemTemplate.Grade;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ALF
 * @date 28.06.2012
 */
public class EnchantScroll
{
	private int itemId;
	private ScrollType type;
	private ScrollTarget target;
	private Grade grade;
	private int chance, min, safe, max;
	private Set<Integer> targetItems;

	public int getItemId()
	{
		return itemId;
	}

	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

	public ScrollType getType()
	{
		return type;
	}

	public void setType(ScrollType type)
	{
		this.type = type;
	}

	public Grade getGrade()
	{
		return grade;
	}

	public void setGrade(Grade grade)
	{
		this.grade = grade;
	}

	public int getMin()
	{
		return min;
	}

	public void setMin(int min)
	{
		this.min = min;
	}

	public int getSafe()
	{
		return safe;
	}

	public void setSafe(int safe)
	{
		this.safe = safe;
	}

	public int getChance()
	{
		return chance;
	}

	public void setChance(int chance)
	{
		this.chance = chance;
	}

	public int getMax()
	{
		return max;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public ScrollTarget getTarget()
	{
		return target;
	}

	public void setTarget(ScrollTarget target)
	{
		this.target = target;
	}

	public Set<Integer> getTargetItems()
	{
		return targetItems;
	}

	public void addTargetItems(int itemId)
	{
		if(targetItems == null)
		{
			targetItems = new HashSet<Integer>();
		}

		targetItems.add(itemId);
	}

	/**
	 * @author ALF
	 * @date 28.06.2012
	 */
	public static enum ScrollTarget
	{
		WEAPON, ARMOR
	}

	/**
	 * @author ALF
	 * @date 28.06.2012
	 */
	public static enum ScrollType
	{
		NORMAL, BLESSED, ANCIENT, DESTRUCTION, ITEM_MALL, DIVINE, CRYSTAL
	}
}
