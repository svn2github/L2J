package l2next.gameserver.model.items.etcitems;

/**
 * @author ALF
 * @date 27.06.2012
 */
public class LifeStone
{
	private int itemId;
	private int level;
	private StoneGrade grade;

	public int getItemId()
	{
		return itemId;
	}

	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public StoneGrade getGrade()
	{
		return grade;
	}

	public void setGrade(StoneGrade grade)
	{
		this.grade = grade;
	}

	public static enum StoneGrade
	{
		LOW, MIDDLE, HIGHT, TOP, ACCESSORY, FORGOTTEN, UNDERWEAR;
	}
}
