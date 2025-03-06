package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.bust.GoldBust;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.bust.KillBust;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.sell.ElementalWell;
import com.watabou.utils.Random;

public class Level0 extends Level {

    private static final int SIZE = 45;


    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;

    }

    //定义地块
 private int mapToTerrain(int code){
        switch (code){
            default:
                return Terrain.WATER;
            case 49:
                return Terrain.CHASM;
            case 1:
                if(Random.Int(50)==0){
                    return Terrain.EMPTY_DECO;
                }else {
                    return Terrain.EMPTY;
                }
            case 102:
                return Terrain.GRASS;
            case 20:
                return Terrain.EMPTY_WELL;
            case 65:
                return Terrain.WALL;
                //return Terrain.ALCHEMY;
            case 81:
                return Terrain.DOOR;
            case 82:
                return Terrain.OPEN_DOOR;
            case 17:
                return Terrain.ENTRANCE;
            case 18:
                return Terrain.EXIT;
            case 4:
                return Terrain.EMBERS;
            case 83:
                return Terrain.LOCKED_DOOR;
            case 21:
                return Terrain.PEDESTAL;
            case 66:
                return Terrain.WALL_DECO;
            case 101:
                return Terrain.BARRICADE;
            case 5:
                return Terrain.EMPTY_SP;
            case 166:
                return Terrain.HIGH_GRASS;
            case 103:
                return Terrain.FURROWED_GRASS;
            case 2:
                return Terrain.EMPTY_DECO;
            case 86:
                return Terrain.LOCKED_EXIT;
            case 87:
                return Terrain.UNLOCKED_EXIT;
            case 97:
                return Terrain.SIGN;
            case 19:
                return Terrain.WELL;
            case 98:
                return Terrain.STATUE;
            case 99:
                return Terrain.STATUE_SP;
            case 67:
                return Terrain.BOOKSHELF;
            case 100:
                return Terrain.ALCHEMY;
        }
    }

    private static final int[] pre_map = {
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,67,67,67,67,67,65,21,5,5,5,21,65,1,1,1,101,101,101,65,5,5,5,65,65,65,1,1,1,1,1,1,1,1,1,65,
            65,65,65,65,65,65,65,65,101,65,5,5,5,5,5,65,5,5,5,5,5,65,1,1,1,1,101,101,65,5,5,5,5,5,65,65,65,1,1,1,1,1,1,1,65,
            65,65,65,65,65,65,65,101,101,65,5,5,21,5,5,81,5,5,100,5,5,65,1,1,1,1,1,1,65,5,5,5,5,5,5,5,65,65,1,1,1,1,1,1,65,
            65,65,65,65,65,1,1,1,1,65,5,5,5,5,5,65,5,5,5,5,5,65,1,1,1,1,1,1,81,5,5,5,5,5,5,5,5,65,65,1,1,1,1,1,65,
            65,65,65,65,1,1,1,1,1,65,5,5,5,5,5,65,5,5,5,5,5,65,1,1,97,1,1,1,65,5,5,5,5,5,5,5,5,5,65,65,1,1,1,1,65,
            65,65,65,101,1,1,1,1,1,65,65,65,65,65,65,65,65,65,81,65,65,65,65,1,1,1,1,65,65,5,5,5,5,5,5,5,5,5,5,65,1,1,1,1,65,
            65,65,101,101,1,1,1,1,1,65,1,1,1,1,1,65,101,101,1,1,1,1,1,1,1,1,1,1,65,5,5,5,5,5,5,5,5,5,5,65,1,1,1,1,65,
            65,65,65,65,65,1,1,1,1,65,1,98,1,98,1,65,101,1,1,1,1,1,1,1,1,1,1,1,65,65,5,5,5,5,5,5,5,5,5,65,65,1,1,1,65,
            65,166,166,166,65,1,1,1,1,65,1,1,1,1,1,65,1,1,1,1,1,1,1,1,1,1,1,1,1,65,65,65,5,5,5,5,5,5,5,5,65,1,1,1,65,
            65,166,19,166,1,1,1,1,1,65,1,1,97,1,1,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,65,65,5,5,5,5,5,5,65,1,1,1,65,
            65,166,98,166,1,1,1,1,1,65,1,1,1,1,1,65,1,1,1,1,1,1,1,5,5,5,5,5,1,1,1,1,1,65,65,5,5,5,5,5,65,1,1,1,65,
            65,166,19,166,1,1,1,1,1,65,65,65,81,65,65,65,1,1,1,1,1,1,1,5,257,257,257,5,1,1,1,1,1,1,65,65,65,81,65,65,65,1,1,1,65,
            65,166,166,166,65,97,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,257,19,257,5,1,1,1,1,1,1,1,1,1,1,1,1,65,65,1,1,65,
            65,65,65,65,65,65,65,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,257,257,257,5,1,1,1,1,1,1,1,1,1,1,1,1,1,65,1,1,65,
            65,65,1,1,1,1,1,65,65,1,1,1,1,1,65,65,65,65,81,65,65,1,1,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,65,1,1,65,
            65,65,1,1,1,1,1,65,65,65,1,1,1,1,65,5,5,65,5,5,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,81,1,1,65,
            65,65,65,1,1,1,1,65,101,65,65,1,1,1,65,5,5,84,5,5,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,1,1,65,
            65,65,65,65,65,83,65,65,101,101,65,1,1,1,65,5,5,65,5,5,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,1,1,65,
            65,65,65,65,1,1,1,1,1,1,65,1,1,1,65,65,65,65,65,65,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,65,1,1,65,
            65,65,65,65,1,1,1,1,1,1,81,1,1,1,1,1,1,1,1,1,1,1,1,1,65,65,65,1,1,65,65,65,65,65,65,1,1,1,1,1,65,65,1,1,65,
            65,65,65,65,65,65,1,1,1,1,65,1,1,1,1,1,1,1,1,1,1,1,65,65,65,1,1,1,1,1,1,65,1,1,65,65,101,101,101,65,65,65,1,1,65,
            65,65,65,19,257,65,65,1,1,1,65,5,5,5,5,5,5,5,5,1,1,1,65,1,1,1,1,1,1,1,1,65,1,1,1,1,1,1,1,1,1,65,1,1,65,
            65,65,166,166,166,166,65,65,1,1,65,5,166,166,166,166,166,166,5,1,1,1,65,1,1,1,18,1,1,1,1,65,1,1,1,1,1,1,1,1,1,65,1,1,65,
            65,65,166,98,166,166,166,65,65,65,65,5,166,19,166,166,19,166,5,1,1,65,65,1,1,1,1,1,1,1,65,65,1,1,1,1,1,1,1,1,1,65,65,65,65,
            65,65,166,166,166,166,166,166,65,65,65,5,166,166,166,166,166,166,5,1,65,65,1,1,1,1,1,1,1,65,65,1,1,1,1,1,1,1,1,1,1,1,1,1,65,
            65,65,1,1,1,1,1,1,1,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,
            65,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,5,5,5,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,
            65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,257,257,257,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,
            65,1,1,1,1,1,1,1,1,65,65,65,65,65,1,1,1,1,5,257,19,257,5,1,1,1,1,1,1,1,65,65,65,65,81,65,65,65,1,1,1,1,1,1,65,
            65,1,1,1,1,1,1,1,1,65,1,1,1,65,1,1,1,1,5,257,257,257,5,1,1,1,1,1,1,1,65,5,5,65,5,5,5,65,1,1,1,1,1,1,65,
            65,1,1,1,1,1,1,1,1,65,1,1,1,65,1,1,1,1,5,5,5,5,5,1,1,1,1,1,1,1,65,5,5,65,5,5,5,65,1,1,1,1,1,65,65,
            65,1,1,1,1,1,1,1,1,65,1,1,1,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,81,5,5,65,65,65,65,65,1,1,1,1,1,65,65,
            65,65,1,1,1,1,1,1,1,65,1,1,1,81,1,1,1,1,1,1,65,65,65,65,65,81,65,65,65,65,65,65,65,65,65,5,5,81,1,1,1,1,1,65,65,
            65,65,65,1,1,1,1,1,1,65,1,1,1,65,1,1,1,1,1,1,65,1,1,1,1,5,1,1,1,1,65,5,5,5,65,5,5,65,1,1,1,1,1,65,65,
            65,65,65,65,65,1,1,1,1,65,1,1,1,65,1,1,1,1,1,1,65,1,1,1,1,5,1,1,1,1,65,5,5,5,65,5,5,65,1,1,1,1,1,65,65,
            65,65,65,65,65,65,1,1,1,65,65,65,65,65,1,1,1,1,1,1,65,1,1,1,1,5,1,1,1,1,65,65,65,81,65,65,65,65,1,1,1,1,65,65,65,
            65,65,65,65,65,65,65,65,1,1,1,1,1,1,1,1,1,1,1,1,65,1,1,1,1,5,1,1,1,1,65,1,1,1,1,1,1,1,1,1,1,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,1,1,1,1,1,1,1,1,1,1,65,65,65,65,65,81,65,65,65,65,65,1,1,1,1,1,1,1,1,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65
    };


    //地块贴图
    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_SAND;
    }
    //水体贴图
    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    private static final int WIDTH = 45;
    private static final int HEIGHT = 41;

    //创建出入口
    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        //上楼梯
        entrance = 417;
        //下楼梯
        exit =1061 ;

        for (int map = 0; map < this.map.length; map++) this.map[map] = mapToTerrain(pre_map[map]);
        return true;
    };


    @Override
    public Mob createMob() {
        return null;
    }


    //创建生物

    @Override
    protected void createMobs() {
/*
        VineDerived i = new VineDerived();
            i.pos = 518;
            mobs.add(i);

 */

        //杀戮雕像
        KillBust killBust = new KillBust();
        GoldBust goldBust = new GoldBust();
        ElementalWell elementalWell = new ElementalWell();
        killBust.pos = 518;
        goldBust.pos = 522;
        elementalWell.pos = 520;
        mobs.add(killBust);
        mobs.add(goldBust);
        mobs.add(elementalWell);

    }

    public Actor addRespawner() {
        return null;
    }






    @Override
    protected void createItems() {
       // drop(new Ammo.AmmoBox(),29);
//        if(Badges.isUnlocked(Badges.Badge.ALL_ARMOR_IDENTIFIED)){
//            drop(new Torch(), 29);
//        }
//        drop(new Torch(), 162);
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        return entrance-width();
    }




















}