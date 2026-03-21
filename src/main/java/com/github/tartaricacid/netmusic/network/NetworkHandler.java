package com.github.tartaricacid.netmusic.network;

import com.github.tartaricacid.netmusic.config.GeneralConfig;
import emi.dev.emi.emi.network.EmiNetwork;
import emi.dev.emi.emi.network.EmiPacket;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class NetworkHandler {

    public static void sendToNearBy(World world, int x, int y, int z, EmiPacket packet) {
        if (world == null || packet == null || world.playerEntities == null) {
            return;
        }
        for (Object obj : world.playerEntities) {
            if (!(obj instanceof EntityPlayerMP player)) {
                continue;
            }
            double radius = Math.max(1.0D, GeneralConfig.MUSIC_PLAYER_HEAR_DISTANCE);
            if (player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= radius * radius) {
                EmiNetwork.sendToClient(player, packet);
            }
        }
    }

    public static void sendToNearBy(World world, EntityPlayer center, EmiPacket packet) {
        if (center != null) {
            sendToNearBy(world,
                    MathHelper.floor_double(center.posX),
                    MathHelper.floor_double(center.posY),
                    MathHelper.floor_double(center.posZ),
                    packet);
        }
    }

    public static void sendToClientPlayer(EmiPacket packet, EntityPlayerMP player) {
        if (packet != null && player != null) {
            EmiNetwork.sendToClient(player, packet);
        }
    }
}

