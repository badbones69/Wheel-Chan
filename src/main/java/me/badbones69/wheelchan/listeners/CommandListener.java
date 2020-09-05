package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.Server;
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
        Server server = wheelChan.getServer(e.getGuild());
        User user = e.getAuthor();
        Message message = e.getMessage();
        MessageChannel channel = e.getChannel();
        boolean isCommandChannel = server.isCommandChannel(channel);
        boolean isTesting = false;
        if (!user.isBot() && e.isFromType(ChannelType.TEXT) && isCommand(message)) {
            String subCommand = getSubCommand(message);
            if (!subCommand.isEmpty()) {
                switch (subCommand.toLowerCase()) {
                    case "help":
                        if (isCommandChannel && !isTesting) {
                            HelpCommand.runCommand(e, server);
                        }
                        return;
                    case "reload":
                        if (isCommandChannel && !isTesting) {
                            ReloadCommand.runCommand(e, server);
                        }
                        return;
                    case "s":
                    case "spin":
                        if (isCommandChannel && !isTesting) {
                            SpinCommand.runCommand(e, server);
                        }
                        return;
                    case "l":
                    case "list":
                        if (isCommandChannel && !isTesting) {
                            ListSenpaisCommand.runCommand(e, server);
                        }
                        return;
                    case "ls":
                    case "listsensei":
                    case "listsenseis":
                        if (isCommandChannel && !isTesting) {
                            ListSenseisCommand.runCommand(e, server);
                        }
                        return;
//                    case "c":
//                    case "clear":
//                    case "removeall":
//                        if (isCommandChannel && !isTesting) {
//                            ClearCommand.runCommand(e, server);
//                        }
//                        return;
                    case "as":
                    case "addsensei":
                        if (isCommandChannel && !isTesting) {
                            AddSenseiCommand.runCommand(e, server);
                        }
                        return;
                    case "rs":
                    case "removesensei":
                        if (isCommandChannel && !isTesting) {
                            RemoveSenseiCommand.runCommand(e, server);
                        }
                        return;
                    case "a":
                    case "add":
                        if (isCommandChannel && !isTesting) {
                            AddSenpaiCommand.runCommand(e, server);
                        }
                        return;
                    case "r":
                    case "remove":
                        if (isCommandChannel && !isTesting) {
                            RemoveSenpaiCommand.runCommand(e, server);
                        }
                        return;
                    case "sl":
                    case "setlog":
                    case "setlogging":
                        SetLoggingChannel.runCommand(e, server);
                        return;
                    case "ac":
                    case "addchannel":
                        AddChannelCommand.runCommand(e, server);
                        return;
                    case "rc":
                    case "removechannel":
                        RemoveChannelCommand.runCommand(e, server);
                        return;
                    case "link":
                    case "lc":
                    case "linkcooldown":
                        if (isCommandChannel) {
                            LinkCooldownCommand.runCommand(e, server);
                        }
                        return;
                    case "m":
                    case "miss":
                    case "missed":
                        if (isCommandChannel && !isTesting) {
                            TrackerMissedCommand.runCommand(e, server);
                        }
                        return;
                    case "stat":
                    case "stats":
                        if (isCommandChannel && !isTesting) {
                            TrackerStatsCommand.runCommand(e, server);
                        }
                        return;
                    case "test":
                        if (isCommandChannel && isTesting) {
                            TestCommand.runCommand(e, server);
                        }
                        return;
                    case "save":
                        if (isCommandChannel && isTesting) {
                            TestCommand.runSaveCommand(e, server);
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