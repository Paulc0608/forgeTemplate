package com.github.paulc0608.forgetemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.Timer;
import java.util.TimerTask;

public class CommandMelon extends CommandBase {

    @Override
    public String getCommandName() {
        return "Melon";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/Melon <time> [rows]";
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
                float time = Float.parseFloat(args[0]) * 1000;
                int rows = args.length > 1 ? Integer.parseInt(args[1]) : 12; // Default to 12 rows if not specified

                Timer timer = new Timer();
                performRowActions(timer, time, rows, player);

            } catch (NumberFormatException e) {
                player.addChatMessage(new ChatComponentText("Please provide valid numbers for time and rows."));
            }
        } else {
            throw new CommandException("This command can only be used by a player.");
        }
    }

    private void performRowActions(Timer timer, float time, int rows, EntityPlayer player) {
        KeyBinding bindForward = FMLClientHandler.instance().getClient().gameSettings.keyBindForward;
        KeyBinding bindLeft = FMLClientHandler.instance().getClient().gameSettings.keyBindLeft;
        KeyBinding bindRight = FMLClientHandler.instance().getClient().gameSettings.keyBindRight;
        while (true) {
            for (int i = 0; i < rows; i++) {
                final int rowIndex = i;
                final boolean isRight = i % 2 == 0; // Alternate between left and right
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        KeyBinding.setKeyBindState(isRight ? bindRight.getKeyCode() : bindLeft.getKeyCode(), true);
                        KeyBinding.setKeyBindState(bindForward.getKeyCode(),true);
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/setYaw 0 58");
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                KeyBinding.setKeyBindState(bindForward.getKeyCode(), true);
                                Minecraft.getMinecraft().thePlayer.sendChatMessage("/setYaw 0 58");
                                KeyBinding.setKeyBindState(isRight ? bindRight.getKeyCode() : bindLeft.getKeyCode(), false);
                                if (rowIndex == rows - 1) {
                                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/warp garden");
                                }
                            }
                        }, (long) time);
                    }
                }, (long) (i * time));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
