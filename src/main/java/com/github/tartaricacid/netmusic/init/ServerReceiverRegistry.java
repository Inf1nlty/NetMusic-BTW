package com.github.tartaricacid.netmusic.init;

import com.github.tartaricacid.netmusic.network.message.SetMusicIDMessage;
import com.github.tartaricacid.netmusic.network.message.SyncVipCookieMessage;
import emi.dev.emi.emi.PacketReader;

public class ServerReceiverRegistry {

    public static void register() {
        PacketReader.registerServerPacketReader(SetMusicIDMessage.ID, SetMusicIDMessage::new);
        PacketReader.registerServerPacketReader(SyncVipCookieMessage.ID, SyncVipCookieMessage::new);
    }
}
