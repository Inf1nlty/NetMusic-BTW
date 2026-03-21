package com.github.tartaricacid.netmusic.init;

import com.github.tartaricacid.netmusic.tileentity.TileEntityCDBurner;
import com.github.tartaricacid.netmusic.tileentity.TileEntityComputer;
import com.github.tartaricacid.netmusic.tileentity.TileEntityMusicPlayer;
import net.minecraft.src.TileEntity;

public class InitBlockEntity {

    public static void registerTileEntities() {
        TileEntity.addMapping(TileEntityMusicPlayer.class, "netmusic:music_player");
        TileEntity.addMapping(TileEntityCDBurner.class, "netmusic:cd_burner");
        TileEntity.addMapping(TileEntityComputer.class, "netmusic:computer");
    }
}
