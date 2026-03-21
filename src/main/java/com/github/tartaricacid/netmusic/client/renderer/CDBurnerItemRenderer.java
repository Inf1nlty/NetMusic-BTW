package com.github.tartaricacid.netmusic.client.renderer;

import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;

public final class CDBurnerItemRenderer {
    private CDBurnerItemRenderer() {}

    public static void render(RenderBlocks renderer, Block block, int metadata) {
        CDBurnerRenderer.renderInventoryBlock(renderer, block, metadata);
    }
}

