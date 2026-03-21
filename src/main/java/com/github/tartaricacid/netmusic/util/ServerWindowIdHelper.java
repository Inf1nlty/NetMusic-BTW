package com.github.tartaricacid.netmusic.util;

import net.minecraft.src.EntityPlayerMP;

import java.lang.reflect.Field;

public final class ServerWindowIdHelper {
    private static final Field CURRENT_WINDOW_ID_FIELD = findWindowField();

    private ServerWindowIdHelper() {
    }

    public static int nextWindowId(EntityPlayerMP player) {
        if (player == null) {
            return 1;
        }
        if (CURRENT_WINDOW_ID_FIELD != null) {
            try {
                int next = (CURRENT_WINDOW_ID_FIELD.getInt(player) % 100) + 1;
                CURRENT_WINDOW_ID_FIELD.setInt(player, next);
                return next;
            } catch (Throwable ignored) {
            }
        }
        if (player.openContainer != null) {
            return (player.openContainer.windowId % 100) + 1;
        }
        return 1;
    }

    private static Field findWindowField() {
        try {
            Field field = EntityPlayerMP.class.getDeclaredField("currentWindowId");
            field.setAccessible(true);
            return field;
        } catch (Throwable ignored) {
            return null;
        }
    }
}

