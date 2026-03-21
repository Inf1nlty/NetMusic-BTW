package com.github.tartaricacid.netmusic.client.network;

import com.github.tartaricacid.netmusic.network.message.Message;
import emi.dev.emi.emi.network.EmiNetwork;

public class ClientNetWorkHandler {

    public static void sendToServer(Message message) {
        EmiNetwork.sendToServer(message);
    }
}
