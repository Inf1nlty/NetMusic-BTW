package com.github.tartaricacid.netmusic.network.message;

import com.github.tartaricacid.netmusic.NetMusic;
import com.github.tartaricacid.netmusic.client.config.MusicListManage;
import emi.shims.java.net.minecraft.network.PacketByteBuf;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.StatCollector;

public class GetMusicListMessage implements Message {
    public static final ResourceLocation ID = new ResourceLocation(NetMusic.MOD_ID, "get_music_list");
    public final long musicListId;
    public static final long RELOAD_MESSAGE = -1;

    public GetMusicListMessage(PacketByteBuf packetByteBuf) {
        this(parseMusicListId(packetByteBuf.readString()));
    }

    public GetMusicListMessage(long musicListId) {
        this.musicListId = musicListId;
    }

    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeString(Long.toString(this.musicListId));
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        try {
            if (this.musicListId == RELOAD_MESSAGE) {
                MusicListManage.loadConfigSongs();
                if (entityPlayer != null) {
                    entityPlayer.addChatMessage(StatCollector.translateToLocal("command.netmusic.music_cd.reload.success"));
                }
            } else {
                MusicListManage.add163List(this.musicListId);
                if (entityPlayer != null) {
                    entityPlayer.addChatMessage(StatCollector.translateToLocal("command.netmusic.music_cd.add163.success"));
                }
            }
        } catch (Exception e) {
            if (entityPlayer != null) {
                entityPlayer.addChatMessage(StatCollector.translateToLocal("command.netmusic.music_cd.add163.fail"));
            }
            NetMusic.LOGGER.error("Failed to get music list from NetEase Cloud Music", e);
        }
    }

    private static long parseMusicListId(String raw) {
        if (raw == null) {
            return 0L;
        }
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }
}

