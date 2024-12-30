package com.github.paulc0608.forgetemplate;

import com.github.paulc0608.forgetemplate.commands.CommandGuiSettings;
import com.github.paulc0608.forgetemplate.commands.Commands;
import com.github.paulc0608.forgetemplate.feature.impl.Reconnect;
import com.github.paulc0608.forgetemplate.handler.EnvHandler;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "examplemod", useMetadata = true)
public class ExampleMod {
    public static final ExampleMod instance = new ExampleMod();
    public static final int GUI_ID = 0;
    private static Logger mLogger = LogManager.getLogger(ExampleMod.class);
    private static boolean connected = false;


    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {

        mLogger.fatal("Logging...");
        mLogger.error("Logging...");
        mLogger.info("Logging...");
        mLogger.debug("Logging...");
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Commands());
        Reconnect reconnect = Reconnect.getInstance();
        MinecraftForge.EVENT_BUS.register(reconnect);
        EnvHandler env = EnvHandler.getInstance();
        MinecraftForge.EVENT_BUS.register(env);
        ClientCommandHandler.instance.registerCommand(new CommandGuiSettings());
//        ClientCommandHandler.instance.registerCommand(new CommandSetYaw());
//        ClientCommandHandler.instance.registerCommand(new CommandMelon());
//        ClientCommandHandler.instance.registerCommand(new CommandApi());
    }


}
