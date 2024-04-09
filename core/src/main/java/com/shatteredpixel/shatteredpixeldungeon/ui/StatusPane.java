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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.newbuff.BurnVest;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndGame;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStory;
import com.watabou.input.GameAction;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.ColorMath;
//

import java.util.Calendar;

public class StatusPane extends Component {

	private NinePatch bg;
	private Image avatar;
	public static float talentBlink;
	private float warning;

	private static final float FLASH_RATE = (float)(Math.PI*1.5f); //1.5 blinks per second

	private int lastTier = 0;

	private Image rawShielding;
	private Image shieldedHP;
	private Image hp;
	private BitmapText hpText;

	private Image exp;

	private BossHealthBar bossHP;

	private int lastLvl = -1;

	private BitmapText level;
	private BitmapText depth;

	private DangerIndicator danger;
	private BuffIndicator buffs;
	private Compass compass;

	private JournalButton btnJournal;
	private MenuButton btnMenu;

	private Toolbar.PickedUpItem pickedUp;
	
	private BitmapText version;

	private RenderedTextBlock info;

	//private BitmapText times;
	//时间
	public static Calendar cal=Calendar.getInstance();
	public static int hh = cal.get(Calendar.HOUR);
	public static int mm = cal.get(Calendar.MINUTE);


	@Override
	protected void createChildren() {

		bg = new NinePatch( Assets.Interfaces.STATUS, 0, 0, 128, 36, 85, 0, 45, 0 );
		add( bg );

		add( new Button(){
			@Override
			protected void onClick () {
				Camera.main.panTo( Dungeon.hero.sprite.center(), 5f );
				GameScene.show( new WndHero() );
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.HERO_INFO;
			}
		}.setRect( height/2, width/2, 30, 30 ));

		btnJournal = new JournalButton();
		add( btnJournal );

		btnMenu = new MenuButton();
		add( btnMenu );

		avatar = HeroSprite.avatar( Dungeon.hero.heroClass, lastTier );
		add( avatar );

		talentBlink = 0;

		compass = new Compass( Statistics.amuletObtained ? Dungeon.level.entrance : Dungeon.level.exit );
		add( compass );

		rawShielding = new Image( Assets.Interfaces.SHLD_BAR );
		rawShielding.alpha(0.5f);
		add(rawShielding);

		shieldedHP = new Image( Assets.Interfaces.SHLD_BAR );
		add(shieldedHP);

		hp = new Image( Assets.Interfaces.HP_BAR );
		//icon.hardlight(1f, 0.8f, 0f);


		add( hp );

		hpText = new BitmapText(PixelScene.pixelFont);
		hpText.alpha(0.6f);
		add(hpText);

		exp = new Image( Assets.Interfaces.XP_BAR );
		add( exp );

		bossHP = new BossHealthBar();
		add( bossHP );

		level = new BitmapText( PixelScene.pixelFont);
		level.hardlight( 0xFFFFAA );
		add( level );

		depth = new BitmapText( Integer.toString( Dungeon.depth ), PixelScene.pixelFont);
		depth.hardlight( 0xCACFC2 );
		depth.measure();
		add( depth );

		danger = new DangerIndicator();
		add( danger );

		buffs = new BuffIndicator( Dungeon.hero );
		add( buffs );

		add( pickedUp = new Toolbar.PickedUpItem());

		int assn = (Dungeon.depth % 5);

		if(Dungeon.depth == 0){
			info = PixelScene.renderTextBlock(Messages.get(StatusPane.class, "area0") +assn +Messages.get(StatusPane.class, "depth"), 6);
		}else if(Dungeon.depth>=1 && Dungeon.depth<=5){
			info = PixelScene.renderTextBlock(Messages.get(StatusPane.class, "area1") +assn +Messages.get(StatusPane.class, "depth"), 6);
		}else if(Dungeon.depth>5 && Dungeon.depth<=10){
			info = PixelScene.renderTextBlock(Messages.get(StatusPane.class, "area2") +assn +Messages.get(StatusPane.class, "depth"), 6);
		}else if(Dungeon.depth>10 && Dungeon.depth<=15){
			info = PixelScene.renderTextBlock(Messages.get(StatusPane.class, "area3") +assn +Messages.get(StatusPane.class, "depth"), 6);
		}else if(Dungeon.depth>15 && Dungeon.depth<=20){
			info = PixelScene.renderTextBlock(Messages.get(StatusPane.class, "area4") +assn +Messages.get(StatusPane.class, "depth"), 6);
		}else if(Dungeon.depth>20 && Dungeon.depth<=25){
			info = PixelScene.renderTextBlock(Messages.get(StatusPane.class, "area5") +assn +Messages.get(StatusPane.class, "depth"), 6);
		}else if(Dungeon.depth>25 && Dungeon.depth<=30){
			info = PixelScene.renderTextBlock(Messages.get(StatusPane.class, "area6") +assn +Messages.get(StatusPane.class, "depth"), 6);
		}

		version = new BitmapText( "v" + Game.version, PixelScene.pixelFont);
		version.alpha( 0.5f );
		add(version);
		add(info);

	}

	@Override
	protected void layout() {


		height = 32;

		bg.size( width, bg.height );


		avatar.x = bg.x + 15 - avatar.width / 2f;
		avatar.y = bg.y + 16 - avatar.height / 2f;
		PixelScene.align(avatar);

		compass.x = avatar.x + avatar.width / 2f - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2f - compass.origin.y;
		PixelScene.align(compass);

		hp.x = shieldedHP.x = rawShielding.x = 30;
		hp.y = shieldedHP.y = rawShielding.y = 3;

		hpText.scale.set(PixelScene.align(0.5f));
		hpText.x = hp.x + 1;
		hpText.y = hp.y + (hp.height - (hpText.baseLine()+hpText.scale.y))/2f;
		hpText.y -= 0.001f; //prefer to be slightly higher
		PixelScene.align(hpText);

		bossHP.setPos( 6 + (width - bossHP.width())/2, 20);


		depth.x = width - 35.5f - depth.width() / 2f;
		depth.y = 8f - depth.baseLine() / 2f;
		PixelScene.align(depth);

		danger.setPos( width - danger.width(), 20 );

		buffs.setPos( 31, 9 );

		btnJournal.setPos( width - 42, 1 );

		btnMenu.setPos( width - btnMenu.width(), 1 );


		
		version.scale.set(PixelScene.align(0.5f));
		version.measure();
		version.x = width - version.width();
		version.y = btnMenu.bottom() + (4 - version.baseLine());
		PixelScene.align(version);

		//定义文本的范围（使用到了版本的位置，因此要排在他后面）
//		info.setPos(version.x-20, version.y+10);

		//基于相对数字定位则更加能全面显示 JDSALing 批注 2024.1.4 1:40
		info.setPos(width - info.width(),version.y+20);



	}
	
	private static final int[] warningColors = new int[]{0x660000, 0xCC0000, 0x660000};


	private float time;

	@Override
	public void update() {
		super.update();

		Calendar cal=Calendar.getInstance();
		
		int health = Dungeon.hero.HP;
		int shield = Dungeon.hero.shielding();
		int max = Dungeon.hero.HT;

//		Visual visual = new Visual(0,0,0,0);
//		visual.am = 0.01f + 0.01f*Math.max(0f, (float)Math.sin( time += Game.elapsed ));
//		time += Game.elapsed / 0.05f;;
//		float r = 0.93f+0.57f*Math.max(0f, (float)Math.sin( time));
//		float g = 0.53f+0.57f*Math.max(0f, (float)Math.sin( time - 10/Math.PI/5 ));
//		float b = 0.03f+0.57f*Math.max(0f, (float)Math.sin( time + 4/Math.PI/2 ));
//		hp.color( r,g,b );
		//hp.color(a);
		if(Dungeon.hero.HP<=0.7*Dungeon.hero.HT&&Dungeon.hero.HP>=0.3*Dungeon.hero.HT){
			hp.color(255,255,0);
		}else if(Dungeon.hero.HP<=0.3*Dungeon.hero.HT){
			hp.color(120,0,0);
		}else{
			hp.color(0,0.45f,0);
		}



		if (!Dungeon.hero.isAlive()) {
			avatar.tint(0x000000, 0.5f);
		} else if ((health/(float)max) < 0.3f) {
			warning += Game.elapsed * 5f *(0.4f - (health/(float)max));
			warning %= 1f;
			avatar.tint(ColorMath.interpolate(warning, warningColors), 0.5f );
		} else if (talentBlink > 0.33f){ //stops early so it doesn't end in the middle of a blink
			talentBlink -= Game.elapsed;
			avatar.tint(1, 1, 0, (float)Math.abs(Math.cos(talentBlink*FLASH_RATE))/2f);
		} else {
			avatar.resetColor();
		}

		hp.scale.x = Math.max( 0, (health-shield)/(float)max);

		//hp.color( r,g,b );




		shieldedHP.scale.x = health/(float)max;
		rawShielding.scale.x = shield/(float)max;


		if (shield <= 0){
			hpText.text(health + "/" + max);
		} else {
			hpText.text(health + "+" + shield +  "/" + max);
		}

		exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();

		if (Dungeon.hero.lvl != lastLvl) {

			if (lastLvl != -1) {
				showStarParticles();
			}

			lastLvl = Dungeon.hero.lvl;
			level.text( Integer.toString( lastLvl ) );
			level.measure();
			level.x = 27.5f - level.width() / 2f;
			level.y = 28.0f - level.baseLine() / 2f;
			PixelScene.align(level);
		}

		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}
	}

	public void showStarParticles(){
		Emitter emitter = (Emitter)recycle( Emitter.class );
		emitter.revive();
		emitter.pos( 27, 27 );
		emitter.burst( Speck.factory( Speck.STAR ), 12 );
	}

	public void pickup( Item item, int cell) {
		pickedUp.reset( item,
			cell,
			btnJournal.journalIcon.x + btnJournal.journalIcon.width()/2f,
			btnJournal.journalIcon.y + btnJournal.journalIcon.height()/2f);
	}

	public void flashForPage( String page ){
		btnJournal.flashingPage = page;
	}
	
	public void updateKeys(){
		btnJournal.updateKeyDisplay();
	}

	private static class JournalButton extends Button {

		private Image bg;
		private Image journalIcon;
		private KeyDisplay keyIcon;
		
		private String flashingPage = null;

		public JournalButton() {
			super();

			width = bg.width + 13; //includes the depth display to the left
			height = bg.height + 4;
		}
		
		@Override
		public GameAction keyAction() {
			return SPDAction.JOURNAL;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();

			bg = new Image( Assets.Interfaces.MENU, 2, 2, 13, 11 );
			add( bg );
			
			journalIcon = new Image( Assets.Interfaces.MENU, 31, 0, 11, 7);
			add( journalIcon );
			
			keyIcon = new KeyDisplay();
			add(keyIcon);
			updateKeyDisplay();
		}

		@Override
		protected void layout() {
			super.layout();

			bg.x = x + 13;
			bg.y = y + 2;
			
			journalIcon.x = bg.x + (bg.width() - journalIcon.width())/2f;
			journalIcon.y = bg.y + (bg.height() - journalIcon.height())/2f;
			PixelScene.align(journalIcon);
			
			keyIcon.x = bg.x + 1;
			keyIcon.y = bg.y + 1;
			keyIcon.width = bg.width - 2;
			keyIcon.height = bg.height - 2;
			PixelScene.align(keyIcon);
		}

		private float time;
		
		@Override
		public void update() {
			super.update();
			
			if (flashingPage != null){
				journalIcon.am = (float)Math.abs(Math.cos( FLASH_RATE * (time += Game.elapsed) ));
				keyIcon.am = journalIcon.am;
				if (time >= Math.PI/FLASH_RATE) {
					time = 0;
				}
			}
		}

		public void updateKeyDisplay() {
			keyIcon.updateKeys();
			keyIcon.visible = keyIcon.keyCount() > 0;
			journalIcon.visible = !keyIcon.visible;
			if (keyIcon.keyCount() > 0) {
				bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
			} else {
				bg.resetColor();
			}
		}

		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			if (keyIcon.keyCount() > 0) {
				bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
			} else {
				bg.resetColor();
			}
		}

		@Override
		protected void onClick() {
			time = 0;
			keyIcon.am = journalIcon.am = 1;
			if (flashingPage != null){
				if (Document.ADVENTURERS_GUIDE.pageNames().contains(flashingPage)){
					GameScene.show( new WndStory( WndJournal.GuideTab.iconForPage(flashingPage),
							Document.ADVENTURERS_GUIDE.pageTitle(flashingPage),
							Document.ADVENTURERS_GUIDE.pageBody(flashingPage) ));
					Document.ADVENTURERS_GUIDE.readPage(flashingPage);
				} else {
					GameScene.show( new WndJournal() );
				}
				flashingPage = null;
			} else {
				GameScene.show( new WndJournal() );
			}
		}

	}

	private static class MenuButton extends Button {

		private Image image;

		public MenuButton() {
			super();

			width = image.width + 4;
			height = image.height + 4;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image( Assets.Interfaces.MENU, 17, 2, 12, 11 );
			add( image );
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = x + 2;
			image.y = y + 2;
		}

		@Override
		protected void onPointerDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			image.resetColor();
		}

		@Override
		protected void onClick() {
			GameScene.show( new WndGame() );
		}
	}


}