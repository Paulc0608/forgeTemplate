package com.github.paulc0608.forgetemplate.commands;

import net.minecraftforge.client.ClientCommandHandler;

public class Commands {

public Commands() {

    ClientCommandHandler.instance.registerCommand(new CommandMelon());
    ClientCommandHandler.instance.registerCommand(new CommandSetYaw());
    ClientCommandHandler.instance.registerCommand(new CommandWart());
    ClientCommandHandler.instance.registerCommand(new CommandGuiSettings());
    ClientCommandHandler.instance.registerCommand(new CommandGetLocation());
}
}
