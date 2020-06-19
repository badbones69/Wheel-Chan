package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveChannelCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        MessageChannel channel = e.getChannel();
        EmbedBuilder embed;
        if (wheelChan.isCommandChannel(channel)) {
            wheelChan.removeCommandChannel(channel);
            embed = Messages.REMOVE_COMMAND_CHANNEL.getMessage("%channel%", "<#" + channel.getId() + ">");
        } else {
            embed = Messages.NOT_COMMAND_CHANNEL.getMessage("%channel%", "<#" + channel.getId() + ">");
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}