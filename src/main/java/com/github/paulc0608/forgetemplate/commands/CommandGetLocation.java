package com.github.paulc0608.forgetemplate.commands;

import com.github.paulc0608.forgetemplate.handler.EnvHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandGetLocation extends ClientCommandBase {

    private EnvHandler env = EnvHandler.getInstance();
    private static Logger mLogger = LogManager.getLogger(CommandGetLocation.class);

    public CommandGetLocation() {
        super("location");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayer) {
            sender.addChatMessage(new ChatComponentText(env.getLocation()));
            sender.addChatMessage(new ChatComponentText(Minecraft.getMinecraft().getCurrentServerData().serverIP));
            sender.addChatMessage(new ChatComponentText(Minecraft.getMinecraft().getCurrentServerData().serverName));
            mLogger.error("Logger loggt");
        }
    }
}
