package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetLoggingChannel {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    private static CardTracker cardTracker = CardTracker.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        MessageChannel channel = e.getChannel();
        Guild guild = e.getGuild();
        EmbedBuilder embed;
        if (wheelChan.isSensei(e.getAuthor(), e.getGuild())) {
            cardTracker.setLoggingChannelID(channel.getId());
            embed = Messages.SET_LOGGING_CHANNEL.getMessage(guild, "%channel%", "<#" + channel.getId() + ">");
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}