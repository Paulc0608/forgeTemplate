package com.github.paulc0608.forgetemplate.util;

import ibxm.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

import java.util.regex.Matcher;

public class PlayerUtils {

    private Matcher matcher;

    public static boolean isInventoryEmpty(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i) != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isOnHypixel() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        String server = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        if(server.contains("hypixel")) {
            return true;
        } else {
            return false;
        }

    }
}
