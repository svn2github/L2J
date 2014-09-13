package instances;


import l2next.gameserver.model.entity.Reflection;

import l2next.gameserver.utils.Location;

public class TeredorCavern extends Reflection
/**
 * Класс контролирует инстанс Teredor
 * User: Darvin
 * Date: 16.10.12
 * Time: 22:59
 */
{
    private static int teredor = 25785;
    private Location teredorCoords = new Location(176160, -185200, -3800);

    @Override
    protected void onCreate() {
        super.onCreate();
        this.addSpawnWithoutRespawn(teredor, teredorCoords, 0);
    }

}