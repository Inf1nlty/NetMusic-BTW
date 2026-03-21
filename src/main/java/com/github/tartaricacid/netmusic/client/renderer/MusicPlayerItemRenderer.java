package com.github.tartaricacid.netmusic.client.renderer;

import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;

public final class MusicPlayerItemRenderer {
    private MusicPlayerItemRenderer() {
    }

    public static void render(RenderBlocks renderer, Block block, int metadata) {
        MusicPlayerRenderer.renderInventoryBlock(renderer, block, metadata);
    }
}
