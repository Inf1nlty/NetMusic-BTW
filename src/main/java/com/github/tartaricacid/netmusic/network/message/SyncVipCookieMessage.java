package com.github.tartaricacid.netmusic.network.message;

import com.github.tartaricacid.netmusic.NetMusic;
import com.github.tartaricacid.netmusic.config.PlayerVipCookieStore;
import emi.shims.java.net.minecraft.network.PacketByteBuf;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ResourceLocation;

public class SyncVipCookieMessage implements Message {
    public static final ResourceLocation ID = new ResourceLocation(NetMusic.MOD_ID, "sync_vip_cookie");

    private final String neteaseCookie;
    private final String qqCookie;

    public SyncVipCookieMessage(PacketByteBuf buf) {
        this(buf.readString(), buf.readString());
    }

    public SyncVipCookieMessage(String neteaseCookie, String qqCookie) {
        this.neteaseCookie = sanitize(neteaseCookie);
        this.qqCookie = sanitize(qqCookie);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.neteaseCookie);
        buf.writeString(this.qqCookie);
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        if (entityPlayer == null || entityPlayer.worldObj == null || entityPlayer.worldObj.isRemote) {
            return;
        }
        PlayerVipCookieStore.setCookies(entityPlayer, this.neteaseCookie, this.qqCookie);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    private static String sanitize(String cookie) {
        return cookie == null ? "" : cookie.trim();
    }
}

