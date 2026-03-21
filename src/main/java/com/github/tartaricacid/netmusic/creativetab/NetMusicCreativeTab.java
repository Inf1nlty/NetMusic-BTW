package com.github.tartaricacid.netmusic.creativetab;

import com.github.tartaricacid.netmusic.init.InitBlocks;
import com.github.tartaricacid.netmusic.init.InitItems;
import com.github.tartaricacid.netmusic.item.ItemMusicCD;
import com.inf1nlty.soulforgecore.api.extension.creativetab.SFCCreativeTabs;
import net.minecraft.src.ItemStack;

import java.util.List;

public final class NetMusicCreativeTab extends SFCCreativeTabs {

    public static final NetMusicCreativeTab TAB = new NetMusicCreativeTab();

    public NetMusicCreativeTab() {
        super("NetMusic");
    }

    public int getTabIconItemIndex() {
        return InitBlocks.MUSIC_PLAYER.blockID;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void displayAllReleventItems(List itemList) {
        super.displayAllReleventItems(itemList);

        if (InitItems.MUSIC_CD == null) {
            return;
        }

        for (ItemMusicCD.SongInfo info : BuiltinMusicDiscs.getSongs()) {
            ItemStack stack = new ItemStack(InitItems.MUSIC_CD);
            ItemMusicCD.setSongInfo(info, stack);
            itemList.add(stack);
        }
    }
}
