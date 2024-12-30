package com.github.paulc0608.forgetemplate.feature.impl;


import com.github.paulc0608.forgetemplate.feature.IFeature;
import com.github.paulc0608.forgetemplate.handler.EnvHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Reconnect implements IFeature {

    private static Reconnect instance;
    private boolean activated = false;
    private State state = State.NONE;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final EnvHandler env = EnvHandler.getInstance();

    // Private constructor to ensure only one instance
    private Reconnect() {}

    // Get the singleton instance
    public static Reconnect getInstance() {
        if (instance == null) {
            instance = new Reconnect();  // Only create instance once
        }
        return instance;
    }

    @Override
    public void activate() {
        activated = true;
    }

    @Override
    public void deactivate() {
        activated = false;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    // Enum to handle different states
    public enum State {
        NONE("None"),
        CONNECTING("Connecting"),
        PRIVATE_ISLAND("Private Island"),
        HUB("Hub"),
        THE_PARK("The Park"),
        THE_FARMING_ISLANDS("The Farming Islands"),
        SPIDER_DEN("Spider's Den"),
        THE_END("The End"),
        CRIMSON_ISLE("Crimson Isle"),
        GOLD_MINE("Gold Mine"),
        DEEP_CAVERNS("Deep Caverns"),
        DWARVEN_MINES("Dwarven Mines"),
        CRYSTAL_HOLLOWS("Crystal Hollows"),
        JERRY_WORKSHOP("Jerry's Workshop"),
        DUNGEON_HUB("Dungeon Hub"),
        LIMBO("UNKNOWN"),
        LOBBY("PROTOTYPE"),
        GARDEN("Garden"),
        DUNGEON("Dungeon"),
        TELEPORTING("Teleporting");

        private final String name;

        State(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null || !isActivated()) return;

        // Send chat message depending on the state
        switch (state) {
            case NONE:
                warpLobby();
                break;
            case LIMBO:
                warpLobby();
                break;
            case CONNECTING:

                break;
            case LOBBY:
                warpSkyblock();

                break;
            case GARDEN:

                break;
            default:
                break;
        }

    }

    public void warpSkyblock() {
        mc.thePlayer.sendChatMessage("/skyblock");

    }
    public void warpLobby() {
        mc.thePlayer.sendChatMessage("/lobby");
        state = State.LOBBY;
    }




}
