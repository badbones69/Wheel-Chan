package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.commands.*;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

public class CommandListener extends ListenerAdapter {
    
    private WheelChan wheelChan = WheelChan.getInstance();
    private List<String> alases = Arrays.asList("wheelchan", "wc", "wheel", "w");
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        MessageChannel channel = e.getChannel();
        boolean isCommandChannel = wheelChan.isCommandChannel(channel);
        if (!user.isBot() && e.isFromType(ChannelType.TEXT) && isCommand(message)) {
            String subCommand = getSubCommand(message);
            if (!subCommand.isEmpty()) {
                switch (subCommand.toLowerCase()) {
                    case "help":
                        if (isCommandChannel) {
                            HelpCommand.runCommand(e);
                        }
                        return;
                    case "reload":
                        if (isCommandChannel) {
                            ReloadCommand.runCommand(e);
                        }
                        return;
                    case "s":
                    case "spin":
                        if (isCommandChannel) {
                            SpinCommand.runCommand(e);
                        }
                        return;
                    case "l":
                    case "list":
                        if (isCommandChannel) {
                            ListSenpaisCommand.runCommand(e);
                        }
                        return;
                    case "ls":
                    case "listsensei":
                    case "listsenseis":
                        if (isCommandChannel) {
                            ListSenseisCommand.runCommand(e);
                        }
                        return;
                    case "c":
                    case "clear":
                    case "removeall":
                        if (isCommandChannel) {
                            ClearCommand.runCommand(e);
                        }
                        return;
                    case "as":
                    case "addsensei":
                        if (isCommandChannel) {
                            AddSenseiCommand.runCommand(e);
                        }
                        return;
                    case "rs":
                    case "removesensei":
                        if (isCommandChannel) {
                            RemoveSenseiCommand.runCommand(e);
                        }
                        return;
                    case "a":
                    case "add":
                        if (isCommandChannel) {
                            AddSenpaiCommand.runCommand(e);
                        }
                        return;
                    case "r":
                    case "remove":
                        if (isCommandChannel) {
                            RemoveSenpaiCommand.runCommand(e);
                        }
                        return;
                    case "sl":
                    case "setlog":
                    case "setlogging":
                        SetLoggingChannel.runCommand(e);
                        return;
                    case "ac":
                    case "addchannel":
                        AddChannelCommand.runCommand(e);
                        return;
                    case "rc":
                    case "removechannel":
                        RemoveChannelCommand.runCommand(e);
                        return;
                    case "link":
                    case "lc":
                    case "linkcooldown":
                        if (isCommandChannel) {
                            LinkCooldownCommand.runCommand(e);
                        }
                        return;
                    case "m":
                    case "miss":
                    case "missed":
                        if (isCommandChannel) {
                            TrackerMissedCommand.runCommand(e);
                        }
                        return;
                    case "stat":
                    case "stats":
                        if (isCommandChannel) {
                            TrackerStatsCommand.runCommand(e);
                        }
                }
            }
        }
    }
    
    private boolean isCommand(Message message) {
        for (String aliase : alases) {
            if (message.getContentDisplay().toLowerCase().startsWith("!" + aliase + " ")) {
                return true;
            }
        }
        return false;
    }
    
    private String getSubCommand(Message message) {
        String[] split = message.getContentDisplay().split(" ");
        return split.length > 1 ? split[1] : "";
    }
    
}