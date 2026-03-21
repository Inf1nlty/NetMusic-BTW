package com.github.tartaricacid.netmusic.client.renderer;

import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;

public final class ComputerItemRenderer {
    private ComputerItemRenderer() {
    }

    public static void render(RenderBlocks renderer, Block block, int metadata) {
        ComputerRenderer.renderInventoryBlock(renderer, block, metadata);
    }
}


