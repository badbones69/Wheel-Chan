package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveSenpieCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        EmbedBuilder embed = new EmbedBuilder();
        if (!message.getMentionedUsers().isEmpty()) {
            User oldSenpie = message.getMentionedUsers().get(0);
            if (wheelChan.isSenpie(oldSenpie)) {
                wheelChan.removeSenpie(oldSenpie);
                embed = Messages.REMOVE_SENPIE.getMessage("%senpie%", oldSenpie.getAsMention());
            } else {
                embed = Messages.NOT_MY_SENPIE.getMessage("%senpie%", oldSenpie.getAsMention());
            }
        }
        if (embed.isEmpty()) {
            embed = Messages.REMOVE_SENPIE_DESCRIPTION.getMessage("%senpie%", user.getAsMention());
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}