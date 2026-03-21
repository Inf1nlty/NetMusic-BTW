package com.github.tartaricacid.netmusic.init;

import com.github.tartaricacid.netmusic.item.ItemMusicCD;
import net.minecraft.src.Item;

public class InitItems {

    public static final int MUSIC_CD_ITEM_ID = 23657;

    public static Item MUSIC_CD;
    public static Item MUSIC_PLAYER;
    public static Item CD_BURNER;
    public static Item COMPUTER;

    public static void registerItems() {

        MUSIC_CD = new ItemMusicCD(MUSIC_CD_ITEM_ID);
        MUSIC_PLAYER = Item.itemsList[InitBlocks.MUSIC_PLAYER.blockID];
        CD_BURNER = Item.itemsList[InitBlocks.CD_BURNER.blockID];
        COMPUTER = Item.itemsList[InitBlocks.COMPUTER.blockID];
    }
}
