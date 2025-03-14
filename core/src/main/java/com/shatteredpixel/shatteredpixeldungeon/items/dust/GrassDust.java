/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.dust;

import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class GrassDust extends Dust {
	
	{
		
		stackable = true;
		unique = true;
		image = ItemSpriteSheet.GRASS_DUST;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	public static class GreenDusttoPotion extends Recipe.SimpleRecipe {
		{
			inputs = new Class[]{GrassDust.class};
			inQuantity = new int[]{1};
			cost = 2;
			output = PotionOfCleansing.class;
			outQuantity = 1;
		}
	}
	public static class GreenDusttoScroll extends Recipe.SimpleRecipe {
		{
			inputs = new Class[]{GrassDust.class};
			inQuantity = new int[]{1};
			cost = 2;
			output = ScrollOfMysticalEnergy.class;
			outQuantity = 1;
		}
	}

}