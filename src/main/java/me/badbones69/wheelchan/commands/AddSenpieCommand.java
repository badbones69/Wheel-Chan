package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddSenpieCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        EmbedBuilder embed = new EmbedBuilder();
        if (!message.getMentionedUsers().isEmpty()) {
            User newSenpie = message.getMentionedUsers().get(0);
            if (!wheelChan.isSenpie(newSenpie)) {
                wheelChan.addSenpie(newSenpie);
                embed = Messages.ADD_SENPIE.getMessage("%senpie%", newSenpie.getAsMention());
            } else {
                embed = Messages.ALREADY_MY_SENPIE.getMessage("%senpie%", newSenpie.getAsMention());
            }
        }
        if (embed.isEmpty()) {
            embed = Messages.ADD_SENPIE_DESCRIPTION.getMessage("%senpie%", user.getAsMention());
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}