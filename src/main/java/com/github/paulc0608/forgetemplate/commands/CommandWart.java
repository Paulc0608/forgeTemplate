package com.github.paulc0608.forgetemplate.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class CommandWart extends ClientCommandBase{

    public CommandWart() {
        super("wart");
    }



    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;
        if (player.getCurrentEquippedItem() != null) {
            player.addChatMessage(new ChatComponentText(player.getCurrentEquippedItem().getDisplayName()));
        }
    }

}

