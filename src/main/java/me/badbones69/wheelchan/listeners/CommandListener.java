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
    private List<String> aliases = Arrays.asList("wheelchan", "wc", "wheel", "w");
    
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
                            AddSenpieCommand.runCommand(e);
                        }
                        return;
                    case "r":
                    case "remove":
                        if (isCommandChannel) {
                            RemoveSenpieCommand.runCommand(e);
                        }
                        return;
                    case "ac":
                    case "addchannel":
                        AddChannelCommand.runCommand(e);
                        return;
                    case "rc":
                    case "removechannel":
                        RemoveChannelCommand.runCommand(e);
                        return;
                }
            }
            //listCommand
        }
    }
    
    private boolean isCommand(Message message) {
        for (String aliase : aliases) {
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