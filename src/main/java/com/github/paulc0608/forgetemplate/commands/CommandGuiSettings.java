package com.github.paulc0608.forgetemplate.commands;

import com.github.paulc0608.forgetemplate.feature.impl.Reconnect;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandGuiSettings extends ClientCommandBase {

    private final Reconnect rc = Reconnect.getInstance();


    public CommandGuiSettings() {
        super("s");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {

            if(rc.isActivated()) {
                rc.deactivate();
            } else {
                rc.activate();
            }
        }
    }
}
