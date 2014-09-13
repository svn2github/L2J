package l2next.gameserver.exception;

/**
 * @author ALF
 * @date 21.08.2012
 */
public class ItemNotFoundException extends Exception
{
	private static final long serialVersionUID = -3494068632550807572L;

	private final int id;

	public ItemNotFoundException(int _id)
	{
		id = _id;
	}

	@Override
	public String toString()
	{
		return "Item not found! ItemId: " + id + " !";
	}

}
