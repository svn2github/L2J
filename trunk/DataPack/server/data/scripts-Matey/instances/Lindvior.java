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

public class Lindvior extends Reflection
{
	
 private static int lindvior = 19425;
    private Location lindviorCoords = new Location(46328, -28200, -1408, 60699);

    @Override
    protected void onCreate() {
        super.onCreate();
        this.addSpawnWithoutRespawn(lindvior, lindviorCoords, 0);
    }
}