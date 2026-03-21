package com.github.tartaricacid.netmusic;

import api.BTWAddon;
import com.github.tartaricacid.netmusic.command.NetMusicCommand;
import com.github.tartaricacid.netmusic.init.InitBlockEntity;
import com.github.tartaricacid.netmusic.init.InitBlocks;
import com.github.tartaricacid.netmusic.init.InitContainer;
import com.github.tartaricacid.netmusic.init.InitEvents;
import com.github.tartaricacid.netmusic.init.InitItems;
import com.github.tartaricacid.netmusic.init.InitRecipes;
import com.github.tartaricacid.netmusic.init.InitSounds;
import com.github.tartaricacid.netmusic.init.ServerReceiverRegistry;

public class NetMusicAddon extends BTWAddon {

    @Override
    public void initialize() {
        InitEvents.init();
        NetMusic.refreshNetEaseApi();
        InitBlocks.registerBlocks();
        InitItems.registerItems();
        InitBlockEntity.registerTileEntities();
        InitRecipes.registerRecipes();
        InitContainer.init();
        InitSounds.init();
        ServerReceiverRegistry.register();
        this.registerAddonCommand(new NetMusicCommand());
    }
}
