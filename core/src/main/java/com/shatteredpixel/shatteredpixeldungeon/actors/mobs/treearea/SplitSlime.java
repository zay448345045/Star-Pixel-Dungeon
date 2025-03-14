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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SplitSlime extends Mob {

	{
		spriteClass = SlimeSprite.class;

		hpPole=6;
		attackPloe=6;
		
		flying = true;

		loot = Generator.Category.SEED;
		lootChance = 1; //by default, see rollToDropLoot()
	}
	
	private static final float SPLIT_DELAY	= 1f;
	
	int generation	= 0;
	
	private static final String GENERATION	= "generation";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GENERATION, generation );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		generation = bundle.getInt( GENERATION );
		if (generation > 0) EXP = 0;
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {

		if (HP >= damage + 2) {
			ArrayList<Integer> candidates = new ArrayList<>();
			
			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (!Dungeon.level.solid[n] && Actor.findChar( n ) == null
						&& (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[n])) {
					candidates.add( n );
				}
			}
	
			if (candidates.size() > 0) {
				
				SplitSlime clone = split();
				clone.HP = (HP - damage) / 2;
				clone.pos = Random.element( candidates );
				clone.state = clone.HUNTING;

				GameScene.add( clone, SPLIT_DELAY );
				Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

				Dungeon.level.occupyCell(clone);
				
				HP -= clone.HP;
			}
		}
		
		return super.defenseProc(enemy, damage);
	}

	private SplitSlime split() {
		SplitSlime clone = new SplitSlime();
		clone.generation = generation + 1;
		clone.EXP = 0;
		if (buff( Burning.class ) != null) {
			Buff.affect( clone, Burning.class ).reignite( clone );
		}
		if (buff( Poison.class ) != null) {
			Buff.affect( clone, Poison.class ).set(2);
		}
		for (Buff b : buffs(AllyBuff.class)){
			Buff.affect( clone, b.getClass());
		}
		for (Buff b : buffs(ChampionEnemy.class)){
			Buff.affect( clone, b.getClass());
		}
		return clone;
	}

	/*
	@Override
	protected Item createLoot(){
		for (int i = 0; i < Random.Int(2,8000); i++) {
			super.createLoot();
		}
		return super.createLoot();
	}

	 */
}