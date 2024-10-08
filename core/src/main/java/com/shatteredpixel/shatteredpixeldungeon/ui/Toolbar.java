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
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTerrainTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuickBag;
import com.watabou.input.GameAction;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInventory;
	private QuickslotTool[] btnQuick;
	
	private PickedUpItem pickedUp;
	
	private boolean lastEnabled = true;
	public boolean examining = false;

	private static Toolbar instance;

	public enum Mode {
		SPLIT,
		GROUP,
		CENTER
	}
	
	public Toolbar() {
		super();

		instance = this;

		height = btnInventory.height();
	}
	
	@Override
	protected void createChildren() {

		//改为根据QuickSlot的SIZE(长度)来自动新增按钮
		btnQuick = new QuickslotTool[QuickSlot.SIZE];
		for(int i = 0;i < btnQuick.length ; i++){
			add( btnQuick[i] = new QuickslotTool(64, 0, 18, 24, i) );
		}

		
		add(btnWait = new Tool(24, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (!GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(false);
				}
			}
			
			@Override
			public GameAction keyAction() {
				return SPDAction.WAIT;
			}
			
			protected boolean onLongClick() {
				if (!GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
				return true;
			}
		});

		add(new Button(){
			@Override
			protected void onClick() {
				examining = false;
				Dungeon.hero.rest(true);
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.REST;
			}
		});
		
		add(btnSearch = new Tool(44, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (!examining) {
					GameScene.selectCell(informer);
					examining = true;
				} else {
					informer.onSelect(null);
					Dungeon.hero.search(true);
				}
			}
			
			@Override
			public GameAction keyAction() {
				return SPDAction.SEARCH;
			}
			
			@Override
			protected boolean onLongClick() {
				Dungeon.hero.search(true);
				return true;
			}
		});
		
		add(btnInventory = new Tool(0, 0, 24, 26) {
			private CurrencyIndicator ind;

			@Override
			protected void onClick() {
				GameScene.show(new WndBag(Dungeon.hero.belongings.backpack));
			}
			
			@Override
			public GameAction keyAction() {
				return SPDAction.INVENTORY;
			}
			
			@Override
			protected boolean onLongClick() {
				GameScene.show(new WndQuickBag(null));
				return true;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				ind = new CurrencyIndicator();
				add(ind);
			}

			@Override
			protected void layout() {
				super.layout();
				ind.fill(this);
			}
		});

		add(pickedUp = new PickedUpItem());
	}

	//被隐藏的原UI

	@Override
	protected void layout() {



		//尝试新增按钮

		float wMin = Game.width / PixelScene.MIN_WIDTH_FULL;
		float hMin = Game.height / PixelScene.MIN_HEIGHT_FULL;
		final int maxHorizontalQuickslots = PixelScene.landscape() ? 9 : 3;



		//快捷栏宽度
		for(int i = 0; i < QuickSlot.SIZE; i++) {
			btnQuick[i].border(0, 1);
			btnQuick[i].frame(64, 0, 18, 24);
		}

		float right = width;
		int a = btnQuick.length;
		switch(Mode.valueOf(SPDSettings.toolbarMode())){

			case SPLIT:
				btnWait.setPos(x, y);
				btnSearch.setPos(btnWait.right(), y);

				btnInventory.setPos(right - btnInventory.width(), y);

				for (int i=0;i<QuickSlot.SIZE;i++){
					int ymove = i/4;
					if(i%4==0){
						btnQuick[i].setPos(btnInventory.left() - btnQuick[i].width(), y+2 - ymove*24);
					}else{
						btnQuick[i].setPos((btnQuick[i%4-1].left()) - btnQuick[i].width(), y+2 - ymove*24);
					}
				}
				
				//center the quickslots if they
				if (btnQuick[QuickSlot.SIZE-1].left() < btnSearch.right()){
					float diff = Math.round(btnSearch.right() - btnQuick[QuickSlot.SIZE-1].left())/2;
					for( int i = 0; i < QuickSlot.SIZE; i++){
						btnQuick[i].setPos( btnQuick[i].left()+diff, btnQuick[i].top() );
					}
				}
				
				break;

			//center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
			case CENTER:
				btnWait.setPos(right - btnWait.width(), y);
				btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
				btnInventory.setPos(btnSearch.left() - btnInventory.width(), y);
				/*
				float toolbarWidth = btnWait.width() + btnSearch.width() + btnInventory.width();
				for(Button slot : btnQuick){
					if (slot.visible) toolbarWidth += slot.width();
				}
				right = (width + toolbarWidth)/2;

				 */

			case GROUP:
				btnWait.setPos(right - btnWait.width(), y);
				btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
				btnInventory.setPos(btnSearch.left() - btnInventory.width(), y);


				for (int i=0;i<QuickSlot.SIZE;i++){
					int ymove = i/4;
					if(i%4==0){
						btnQuick[i].setPos(btnInventory.left() - btnQuick[i].width(), y+2 - ymove*24);
					}else{
						btnQuick[i].setPos((btnQuick[i%4-1].left()) - btnQuick[i].width(), y+2 - ymove*24);
					}
				}
				
				if (btnQuick[QuickSlot.SIZE-1].left() < 0){
					float diff = -Math.round(btnQuick[QuickSlot.SIZE-1].left())/2;
					for( int i = 0; i < QuickSlot.SIZE; i++){
						btnQuick[i].setPos( btnQuick[i].left()+diff, btnQuick[i].top() );
					}
				}
				
				break;
		}
		right = width;

		if (SPDSettings.flipToolbar()) {

			btnWait.setPos( (right - btnWait.right()), y);
			btnSearch.setPos( (right - btnSearch.right()), y);
			btnInventory.setPos( (right - btnInventory.right()), y);


			for(int i = 0; i <= QuickSlot.SIZE-1; i++) {
				btnQuick[i].setPos( right - btnQuick[i].right(), y+2);
			}

		}

	}

	public static void updateLayout(){
		if (instance != null) instance.layout();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (lastEnabled != (Dungeon.hero.ready && Dungeon.hero.isAlive())) {
			lastEnabled = (Dungeon.hero.ready && Dungeon.hero.isAlive());
			
			for (Gizmo tool : members.toArray(new Gizmo[0])) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}
		
		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}
	}
	
	public void pickup( Item item, int cell ) {
		pickedUp.reset( item,
			cell,
			btnInventory.centerX(),
			btnInventory.centerY());
	}
	
	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			instance.examining = false;
			GameScene.examineCell( cell );
		}
		@Override
		public String prompt() {
			return Messages.get(Toolbar.class, "examine_prompt");
		}
	};
	
	private static class Tool extends Button {
		
		private static final int BGCOLOR = 0x7B8073;
		
		private Image base;
		
		public Tool( int x, int y, int width, int height ) {
			super();

			hotArea.blockLevel = PointerArea.ALWAYS_BLOCK;
			frame(x, y, width, height);
		}

		public void frame( int x, int y, int width, int height) {
			base.frame( x, y, width, height );

			this.width = width;
			this.height = height;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			base = new Image( Assets.Interfaces.TOOLBAR );
			add( base );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			base.x = x;
			base.y = y;
		}
		
		@Override
		protected void onPointerDown() {
			base.brightness( 1.4f );
		}
		
		@Override
		protected void onPointerUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}
		
		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}
	}
	
	private static class QuickslotTool extends Tool {
		
		private QuickSlotButton slot;
		private int borderLeft = 2;
		private int borderRight = 2;
		
		public QuickslotTool( int x, int y, int width, int height, int slotNum ) {
			super( x, y, width, height );

			slot = new QuickSlotButton( slotNum );
			add( slot );
		}

		public void border( int left, int right ){
			borderLeft = left;
			borderRight = right;
			layout();
		}
		
		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x + borderLeft, y + 2, width - borderLeft-borderRight, height - 4 );
		}
		
		@Override
		public void enable( boolean value ) {
			super.enable( value );
			slot.enable( value );
		}
	}
	
	public static class PickedUpItem extends ItemSprite {
		
		private static final float DURATION = 0.5f;
		
		private float startScale;
		private float startX, startY;
		private float endX, endY;
		private float left;
		
		public PickedUpItem() {
			super();
			
			originToCenter();
			
			active =
			visible =
				false;
		}
		
		public void reset( Item item, int cell, float endX, float endY ) {
			view( item );
			
			active =
			visible =
				true;
			
			PointF tile = DungeonTerrainTilemap.raisedTileCenterToWorld(cell);
			Point screen = Camera.main.cameraToScreen(tile.x, tile.y);
			PointF start = camera().screenToCamera(screen.x, screen.y);
			
			x = this.startX = start.x - width() / 2;
			y = this.startY = start.y - width() / 2;
			
			this.endX = endX - width() / 2;
			this.endY = endY - width() / 2;
			left = DURATION;
			
			scale.set( startScale = Camera.main.zoom / camera().zoom );
			
		}
		
		@Override
		public void update() {
			super.update();
			
			if ((left -= Game.elapsed) <= 0) {
				
				visible =
				active =
					false;
				if (emitter != null) emitter.on = false;
				
			} else {
				float p = left / DURATION;
				scale.set( startScale * (float)Math.sqrt( p ) );
				
				x = startX*p + endX*(1-p);
				y = startY*p + endY*(1-p);
			}
		}
	}
}