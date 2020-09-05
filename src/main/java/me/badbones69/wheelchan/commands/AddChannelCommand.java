package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddChannelCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        MessageChannel channel = e.getChannel();
        Guild guild = e.getGuild();
        EmbedBuilder embed;
        if (server.isSensei(e.getAuthor(), e.getGuild())) {
            if (!server.isCommandChannel(channel)) {
                server.addCommandChannel(channel);
                embed = Messages.ADD_COMMAND_CHANNEL.getMessage(guild, "%channel%", "<#" + channel.getId() + ">");
            } else {
                embed = Messages.ALREADY_COMMAND_CHANNEL.getMessage(guild, "%channel%", "<#" + channel.getId() + ">");
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}