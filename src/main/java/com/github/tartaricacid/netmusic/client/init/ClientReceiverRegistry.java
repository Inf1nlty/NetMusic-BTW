package com.github.tartaricacid.netmusic.client.init;

import com.github.tartaricacid.netmusic.network.message.GetMusicListMessage;
import com.github.tartaricacid.netmusic.network.message.MusicToClientMessage;
import com.github.tartaricacid.netmusic.network.message.MusicPlayerStateMessage;
import com.github.tartaricacid.netmusic.network.message.OpenMenuMessage;
import emi.dev.emi.emi.PacketReader;

public class ClientReceiverRegistry {

    public static void register() {
        PacketReader.registerClientPacketReader(MusicToClientMessage.ID, MusicToClientMessage::new);
        PacketReader.registerClientPacketReader(MusicPlayerStateMessage.ID, MusicPlayerStateMessage::new);
        PacketReader.registerClientPacketReader(GetMusicListMessage.ID, GetMusicListMessage::new);
        PacketReader.registerClientPacketReader(OpenMenuMessage.ID, OpenMenuMessage::new);
    }
}

