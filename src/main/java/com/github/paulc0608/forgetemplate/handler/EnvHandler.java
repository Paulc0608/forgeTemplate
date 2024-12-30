package com.github.paulc0608.forgetemplate.handler;


import com.github.paulc0608.forgetemplate.event.SkyblockLeftEvent;
import com.github.paulc0608.forgetemplate.feature.impl.Reconnect;
import com.github.paulc0608.forgetemplate.util.PlayerUtils;
import com.github.paulc0608.forgetemplate.util.ScoreboardUtils;
import com.github.paulc0608.forgetemplate.util.TablistUtils;
import com.github.paulc0608.forgetemplate.util.helper.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class EnvHandler {
    private static EnvHandler instance;
    private final Minecraft mc = Minecraft.getMinecraft();
    private String serverIP;
    private Location lastLocation = Location.TELEPORTING;
    private Location location = Location.TELEPORTING;
    private final Pattern areaPattern = Pattern.compile("Area:\\s(.+)");
    private boolean disconnect = true;
    private boolean onSkyblock = false;
    private final Timer timeBetweenChecks = new Timer();
    private final Timer timeAfterConnection = new Timer();
    private boolean commandAlreadySend = false;

    private static Logger mLogger = LogManager.getLogger(EnvHandler.class);

    @SubscribeEvent
    public void onLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        mc.getCurrentServerData().serverName = null;
        setDisconnect(true);
    }



    private EnvHandler() {

    }

    public static EnvHandler getInstance() {
        if ( instance == null ) {
            instance = new EnvHandler();
        }
        return instance;
    }

    @SubscribeEvent
    public void onTickCheckLocation(TickEvent.ClientTickEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        if(location != lastLocation || timeBetweenChecks.hasPassed(2000)) {
            mLogger.error("Setting commandAlreadySend to false");
            commandAlreadySend = false;
        }

        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null) {
            serverIP = mc.getCurrentServerData().serverIP;
        }

        if (TablistUtils.getTabList().size() == 1 && ScoreboardUtils.getScoreboardLines().isEmpty() && PlayerUtils.isInventoryEmpty(mc.thePlayer)) {
            lastLocation = location;
            location = Location.LIMBO;
            return;
        }

        for (String line : TablistUtils.getTabList()) {
            Matcher matcher = areaPattern.matcher(line);
            if (matcher.find()) {
                String area = matcher.group(1);
                for (Location island : Location.values()) {
                    if (area.equals(island.getName())) {
                        lastLocation = location;
                        location = island;
                        return;
                    }
                }
            }
        }

        if (!ScoreboardUtils.getScoreboardTitle().contains("SKYBLOCK") && !ScoreboardUtils.getScoreboardLines().isEmpty() && ScoreboardUtils.cleanSB(ScoreboardUtils.getScoreboardLines().get(0)).contains("www.hypixel.net")) {
            lastLocation = location;
            location = Location.LOBBY;
            return;
        }
        if (location != Location.TELEPORTING) {
            lastLocation = location;
        }
        location = Location.TELEPORTING;


    }





    @SubscribeEvent
    public void checkServerConnect(TickEvent.ClientTickEvent event) {

    }

    /*
     * Working
     */
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
//        mLogger.fatal("Tick event");
        if(getDisconnect()) {
            mLogger.fatal("Player is disconnected, attempting to reconnect");
            FMLClientHandler.instance().connectToServer(new GuiMultiplayer(FMLClientHandler.instance().getClient().currentScreen),new ServerData("hypixel", "mc.hypixel.net", false));
            disconnect = false;
            timeAfterConnection.reset();
            timeBetweenChecks.schedule();
        }
    }

    @SubscribeEvent
    public void onTickChange(TickEvent.ClientTickEvent event) {
        if(timeBetweenChecks.hasPassed(50)) {
            mLogger.error("Time has passed");
            if (mc.thePlayer == null || commandAlreadySend) {
                mLogger.info("Command already send");
                return;
            } else {

                // Send chat message depending on the state
                switch (location) {

                    case LIMBO:
                        warpLobby();
                        break;
                    case LOBBY:
                        warpSkyblock();

                        break;
                    case GARDEN:

                        break;
                    default:
                        break;
                }
                commandAlreadySend = true;
                timeBetweenChecks.reset();
                timeBetweenChecks.schedule();
            }
        } else {
            mLogger.error("Time has not passed");
            mLogger.info("Elapsed Time: " + timeBetweenChecks.getElapsedTime());
        }

    }

    public void warpSkyblock() {
        mc.thePlayer.sendChatMessage("/skyblock");

    }
    public void warpLobby() {
        mc.thePlayer.sendChatMessage("/lobby");
    }


    public enum Location {
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

        Location(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }


    public String getLocation() {
        return location.getName();
    }

    public String getServerIP() {
        return serverIP;
    }

    public boolean getDisconnect() {
        return disconnect;
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

    public boolean isOnSkyblock() {
        return onSkyblock;
    }
}
