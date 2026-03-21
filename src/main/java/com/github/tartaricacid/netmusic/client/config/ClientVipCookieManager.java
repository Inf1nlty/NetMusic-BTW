package com.github.tartaricacid.netmusic.client.config;

import com.github.tartaricacid.netmusic.NetMusic;
import com.github.tartaricacid.netmusic.client.network.ClientNetWorkHandler;
import com.github.tartaricacid.netmusic.config.GeneralConfig;
import com.github.tartaricacid.netmusic.config.MusicProviderType;
import com.github.tartaricacid.netmusic.config.PlayerVipCookieStore;
import com.github.tartaricacid.netmusic.network.message.SyncVipCookieMessage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Minecraft;
import org.apache.commons.lang3.StringUtils;

@Environment(EnvType.CLIENT)
public final class ClientVipCookieManager {
    private static String activePlayerKey;
    private static String lastSyncedNetease = "";
    private static String lastSyncedQq = "";

    private ClientVipCookieManager() {
    }

    public static void clientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.thePlayer == null || mc.theWorld == null) {
            activePlayerKey = null;
            return;
        }
        String key = PlayerVipCookieStore.resolvePlayerKey(mc.thePlayer);
        if (StringUtils.isBlank(key)) {
            return;
        }
        if (!key.equals(activePlayerKey)) {
            activePlayerKey = key;
            PlayerVipCookieStore.CookiePair pair = PlayerVipCookieStore.getCookies(mc.thePlayer);
            applyRuntimeCookies(pair.neteaseCookie, pair.qqCookie);
            syncToServer(pair.neteaseCookie, pair.qqCookie, true);
        }
    }

    public static void updateCookieForCurrentPlayer(MusicProviderType provider, String cookie) {
        MusicProviderType actualProvider = provider == null ? MusicProviderType.NETEASE : provider;
        String sanitized = cookie == null ? "" : cookie.trim();
        String netease = GeneralConfig.NETEASE_VIP_COOKIE;
        String qq = GeneralConfig.QQ_VIP_COOKIE;
        if (actualProvider == MusicProviderType.QQ) {
            qq = sanitized;
        } else {
            netease = sanitized;
        }
        applyRuntimeCookies(netease, qq);

        EntityPlayer currentPlayer = getCurrentPlayer();
        if (currentPlayer != null) {
            activePlayerKey = PlayerVipCookieStore.resolvePlayerKey(currentPlayer);
            PlayerVipCookieStore.setCookies(currentPlayer, netease, qq);
        }
        syncToServer(netease, qq, false);
    }

    private static EntityPlayer getCurrentPlayer() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) {
            return null;
        }
        return mc.thePlayer;
    }

    private static void applyRuntimeCookies(String neteaseCookie, String qqCookie) {
        GeneralConfig.setVipCookies(neteaseCookie, qqCookie);
        NetMusic.refreshNetEaseApi();
    }

    private static void syncToServer(String neteaseCookie, String qqCookie, boolean force) {
        String netease = neteaseCookie == null ? "" : neteaseCookie.trim();
        String qq = qqCookie == null ? "" : qqCookie.trim();
        if (!force && lastSyncedNetease.equals(netease) && lastSyncedQq.equals(qq)) {
            return;
        }
        ClientNetWorkHandler.sendToServer(new SyncVipCookieMessage(netease, qq));
        lastSyncedNetease = netease;
        lastSyncedQq = qq;
    }
}

