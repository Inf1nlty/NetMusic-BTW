package com.github.tartaricacid.netmusic.init;

import com.github.tartaricacid.netmusic.block.BlockCDBurner;
import com.github.tartaricacid.netmusic.block.BlockComputer;
import com.github.tartaricacid.netmusic.block.BlockMusicPlayer;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;

public class InitBlocks {

    public static final int MUSIC_PLAYER_BLOCK_ID = 3920;
    public static final int CD_BURNER_BLOCK_ID = 3921;
    public static final int COMPUTER_BLOCK_ID = 3922;

    public static Block MUSIC_PLAYER;
    public static Block CD_BURNER;
    public static Block COMPUTER;

    public static void registerBlocks() {

        MUSIC_PLAYER = new BlockMusicPlayer(MUSIC_PLAYER_BLOCK_ID);
        Item.itemsList[MUSIC_PLAYER.blockID] = new ItemBlock(MUSIC_PLAYER.blockID - 256);

        CD_BURNER = new BlockCDBurner(CD_BURNER_BLOCK_ID);
        Item.itemsList[CD_BURNER.blockID] = new ItemBlock(CD_BURNER.blockID - 256);

        COMPUTER = new BlockComputer(COMPUTER_BLOCK_ID);
        Item.itemsList[COMPUTER.blockID] = new ItemBlock(COMPUTER.blockID - 256);
    }
}
