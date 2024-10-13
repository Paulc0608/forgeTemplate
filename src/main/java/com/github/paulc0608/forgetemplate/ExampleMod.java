package com.github.paulc0608.forgetemplate;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = "examplemod", useMetadata = true)
public class ExampleMod {
    public static final ExampleMod instance = new ExampleMod();
    public static final int GUI_ID = 0;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CommandSetYaw());
        ClientCommandHandler.instance.registerCommand(new CommandMelon());
    }
}
