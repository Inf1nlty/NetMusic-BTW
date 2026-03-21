package com.github.tartaricacid.netmusic;

import com.github.tartaricacid.netmusic.api.NetEaseMusic;
import com.github.tartaricacid.netmusic.api.WebApi;
import com.github.tartaricacid.netmusic.config.GeneralConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;

public final class NetMusic {

    public static final String MOD_ID = "netmusic";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static WebApi NET_EASE_WEB_API;

    private NetMusic() {}

    public static void refreshNetEaseApi() {
        String cookie = GeneralConfig.NETEASE_VIP_COOKIE;
        NET_EASE_WEB_API = StringUtils.isBlank(cookie)
                ? new NetEaseMusic().getApi()
                : new NetEaseMusic(cookie).getApi();
    }
}
