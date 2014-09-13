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

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Smotocel-PC
 */
public class BeautyShopSet
{
	private final int _id;
	private final TIntObjectHashMap<BeautyShopHairStyle> _hairStyles;
	private final TIntObjectHashMap<BeautyShopFace> _faces;
	
	public BeautyShopSet(int id, TIntObjectHashMap<BeautyShopHairStyle> hairStyles, TIntObjectHashMap<BeautyShopFace> faces)
	{
		_id = id;
		_hairStyles = hairStyles;
		_faces = faces;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public TIntObjectHashMap<BeautyShopHairStyle> getHairStyles()
	{
		return _hairStyles;
	}
	
	public BeautyShopHairStyle getHairStyle(int id)
	{
		return (_hairStyles.get(id));
	}
	
	public TIntObjectHashMap<BeautyShopFace> getFaces()
	{
		return _faces;
	}
	
	public BeautyShopFace getFace(int id)
	{
		return (_faces.get(id));
	}
}
