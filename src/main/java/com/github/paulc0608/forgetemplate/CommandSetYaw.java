package com.github.paulc0608.forgetemplate;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class CommandSetYaw extends CommandBase {

    @Override
    public String getCommandName() {
        return "setYaw";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/setYaw <yaw> [pitch]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;

            if (args.length < 1 || args.length > 2) {
                player.addChatMessage(new ChatComponentText("Invalid usage! Use: " + this.getCommandUsage(sender)));
                return;
            }

            try {
                float yaw = -Float.parseFloat(args[0]); // Negativ machen
                player.rotationYaw = yaw; // Setze den Yaw
                float pitch = 0; // Initialisiere pitch

                if (args.length == 2) {
                    pitch = -Float.parseFloat(args[1]); // Negativ machen
                    player.rotationPitch = pitch; // Setze den Pitch
                }

                player.addChatMessage(new ChatComponentText("Yaw set to " + yaw +
                        (args.length == 2 ? " and Pitch set to " + pitch : "")));
            } catch (NumberFormatException e) {
                player.addChatMessage(new ChatComponentText("Invalid number format! Use: " + this.getCommandUsage(sender)));
            }
        } else {
            sender.addChatMessage(new ChatComponentText("This command can only be used by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "0", "90", "180", "270") : null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}