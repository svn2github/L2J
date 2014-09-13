package instances;


import l2next.gameserver.model.entity.Reflection;

import l2next.gameserver.utils.Location;

/**
 * Класс контролирует Валлока, рб 99 лвл
 *
 * @author Awakeninger
 * Логгеры поставлены исключительно на время тестов.
 * Чтобы видеть стадии инстанса.
 *
 * По факту - это просто болванка инстанса.
 */

public class Vullock extends Reflection
{
	
 private static int vullock = 29218;
    private Location vullockCoords = new Location(153576, 142088, -12762, 16383);

    @Override
    protected void onCreate() {
        super.onCreate();
        this.addSpawnWithoutRespawn(vullock, vullockCoords, 0);
    }
}