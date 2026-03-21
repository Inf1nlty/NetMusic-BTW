package com.github.tartaricacid.netmusic.client;

import com.github.tartaricacid.netmusic.client.init.InitContainerGui;
import com.github.tartaricacid.netmusic.client.init.InitModel;
import com.github.tartaricacid.netmusic.client.init.ClientReceiverRegistry;
import com.github.tartaricacid.netmusic.client.renderer.CDBurnerTileEntityRenderer;
import com.github.tartaricacid.netmusic.client.renderer.ComputerTileEntityRenderer;
import com.github.tartaricacid.netmusic.client.renderer.MusicPlayerTileEntityRenderer;
import com.github.tartaricacid.netmusic.tileentity.TileEntityCDBurner;
import com.github.tartaricacid.netmusic.tileentity.TileEntityComputer;
import com.github.tartaricacid.netmusic.tileentity.TileEntityMusicPlayer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.src.TileEntityRenderer;

public class NetMusicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InitContainerGui.init();
        InitModel.init();
        ClientReceiverRegistry.register();
        TileEntityRenderer.instance.addSpecialRendererForClass(TileEntityCDBurner.class, new CDBurnerTileEntityRenderer());
        TileEntityRenderer.instance.addSpecialRendererForClass(TileEntityComputer.class, new ComputerTileEntityRenderer());
        TileEntityRenderer.instance.addSpecialRendererForClass(TileEntityMusicPlayer.class, new MusicPlayerTileEntityRenderer());
    }
}
