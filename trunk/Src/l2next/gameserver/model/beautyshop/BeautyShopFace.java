/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2next.gameserver.model.beautyshop;

/**
 * @author Smo
 */
public class BeautyShopFace
{
	private final int _id;
	private final long _adena;
	private final long _coins;
	private final long _resetPrice;
	
	public BeautyShopFace(int id, long adena, long coins, long resetPrice)
	{
		_id = id;
		_adena = adena;
		_coins = coins;
		_resetPrice = resetPrice;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public long getAdena()
	{
		return _adena;
	}
	
	public long getCoins()
	{
		return _coins;
	}
	
	public long getResetPrice()
	{
		return _resetPrice;
	}
}
