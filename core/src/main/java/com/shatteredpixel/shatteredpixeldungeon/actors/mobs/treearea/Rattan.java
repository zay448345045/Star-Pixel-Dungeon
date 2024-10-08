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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.treearea;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.MobLoot;
import com.shatteredpixel.shatteredpixeldungeon.items.bossloot.BossLoot;
import com.shatteredpixel.shatteredpixeldungeon.sprites.newsprite.tree.LifePlantSprite;
import com.watabou.utils.Random;

public class Rattan extends Rat {

	{
		spriteClass = LifePlantSprite.class;
		
		HP = HT = 15+Random.Int(2+(BossLoot.infection*2));
		EXP = 4;
		maxLvl=11;
		
		//loot = Generator.Category.POTION;
		lootChance = 0.25f;
		defenseSkill = 6;
	}

	@Override
	public int attackSkill( Char target ) {
		return 16;
	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 8 + BossLoot.infection );
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 3);
	}

	@Override
	protected Item createLoot() {
		Item loot;
		float a = Random.Float();
		if(a<=(1f * ((8f - Dungeon.LimitedDrops.RATTAN_LOOT.count) / 8f))){
			loot = Generator.randomUsingDefaults(Generator.Category.SEED);
			Dungeon.LimitedDrops.RATTAN_LOOT.count++;
		} else {
			loot = new MobLoot().quantity(Random.Int(2,5));
		}
		return loot;
	}
}