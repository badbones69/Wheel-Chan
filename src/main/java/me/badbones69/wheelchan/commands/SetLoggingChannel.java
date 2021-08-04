package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetLoggingChannel {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent event, Server server) {
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        EmbedBuilder embed;
        if (server.isSensei(event.getAuthor(), event.getGuild())) {
            server.getCardTracker().setLoggingChannelID(channel.getId());
            embed = Messages.SET_LOGGING_CHANNEL.getMessage(guild, "%channel%", "<#" + channel.getId() + ">");
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        event.getChannel().sendMessage(embed.build()).complete();
    }
    
}