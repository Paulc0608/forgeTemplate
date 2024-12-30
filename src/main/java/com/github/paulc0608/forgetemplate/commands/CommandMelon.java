package com.github.paulc0608.forgetemplate.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.Timer;
import java.util.TimerTask;

public class CommandMelon extends ClientCommandBase {
    private boolean isRunning = false;
    private Timer timer;

    public CommandMelon() {
        super("Melon");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;

            if (args.length < 1 || args.length > 2) {
                player.addChatMessage(new ChatComponentText("Invalid usage! Use: " + this.getCommandUsage(sender)));
                return;
            }

            if (args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("stop")) {
                stopMelonProcess(player);
                return;
            }

            try {
                float time = Float.parseFloat(args[0]) * 1000;
                int rows = (args.length > 1 ? Integer.parseInt(args[1]) : 13);

                if (isRunning) {
                    player.addChatMessage(new ChatComponentText("Der Prozess läuft bereits! Verwende /Melon end, um ihn zu stoppen."));
                    return;
                }


                isRunning = true;
                timer = new Timer();
                performRowActions(timer, time, rows, player);


            } catch (NumberFormatException e) {
                player.addChatMessage(new ChatComponentText("Bitte geben Sie gültige Zahlen für Zeit und Zeilen ein."));
            }
        } else {
            throw new CommandException("This command can only be used by a player.");
        }
    }

    private void performRowActions(Timer timer, float time, int rows, EntityPlayer player) {
        KeyBinding bindLeft = FMLClientHandler.instance().getClient().gameSettings.keyBindLeft;
        KeyBinding bindRight = FMLClientHandler.instance().getClient().gameSettings.keyBindRight;
        KeyBinding bindForward = FMLClientHandler.instance().getClient().gameSettings.keyBindForward;

        final int[] currentRow = {0};

        // Start the first action immediately
        scheduleRowAction(timer, time, currentRow, rows, bindLeft, bindRight, bindForward, player,true);
    }

    private void scheduleRowAction(Timer timer, float time, int[] currentRow, int rows,
                                   KeyBinding bindLeft, KeyBinding bindRight, KeyBinding bindForward,EntityPlayer player,
                                   boolean isFirstAction) {
        if (currentRow[0] < rows) {
            final boolean isRight = currentRow[0] % 2 == 0;

            // Key press action
            Minecraft.getMinecraft().addScheduledTask(() -> {
                KeyBinding.setKeyBindState(bindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(isRight ? bindRight.getKeyCode() : bindLeft.getKeyCode(), true);
            });

            // Schedule to stop key press after the specified time

            long delai = isFirstAction ? 200 : (long) time;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        KeyBinding.setKeyBindState(bindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(isRight ? bindRight.getKeyCode() : bindLeft.getKeyCode(), false);
                        currentRow[0]++;

                        // Check if more rows are left and schedule the next action
                        if (currentRow[0] < rows) {
                            player.rotationYaw = 0;
                            player.rotationPitch = -58;
                            long delay = isFirstAction ? 200 : (long) 200; // Short delay for the first action
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    scheduleRowAction(timer, time, currentRow, rows, bindLeft, bindRight, bindForward,player, false);
                                }
                            }, delay);
                        } else {
                            // All rows processed
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/warp garden");
                            // End the process
                            performRowActions(timer, time, rows, player);


                        }
                    });
                }

                }, delai); // Delay for key press
        }

    }




    private void stopMelonProcess(EntityPlayer player) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
            isRunning = false;
            player.addChatMessage(new ChatComponentText("Der Melon-Prozess wurde gestoppt."));
        } else {
            player.addChatMessage(new ChatComponentText("Kein laufender Melon-Prozess gefunden."));
        }
    }

}
