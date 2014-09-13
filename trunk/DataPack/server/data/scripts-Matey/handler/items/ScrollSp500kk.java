package handler.items;

import l2next.gameserver.model.Playable;
import l2next.gameserver.model.Player;
import l2next.gameserver.model.items.ItemInstance;
import l2next.gameserver.utils.ItemFunctions;

public class ScrollSp500kk extends ScriptItemHandler 
{
    private static final int[] _itemIds = {34742};

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) 
	{
        if (!playable.isPlayer())
            return false;
        if (playable.getLevel() < 76)
            return false;
        Player activeChar = (Player) playable;
        if (activeChar.getActiveSubClass().isSub() || activeChar.getActiveSubClass().isDouble()) 
		{
			activeChar.addExpAndSp(0, 500000000);
			ItemFunctions.removeItem(activeChar,34742,1,true);
        }
        return true;
    }

    @Override
    public int[] getItemIds() 
	{
        return _itemIds;
    }
}
