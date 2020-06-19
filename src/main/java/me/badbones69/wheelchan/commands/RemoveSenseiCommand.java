package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveSenseiCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        EmbedBuilder embed = Messages.REMOVE_SENSEI_DESCRIPTION.getMessage("%sensei%", user.getAsMention());
        if (wheelChan.isSensei(user, e.getGuild())) {
            if (!message.getMentionedUsers().isEmpty()) {
                User oldSensei = message.getMentionedUsers().get(0);
                if (wheelChan.isSensei(oldSensei, e.getGuild())) {
                    wheelChan.removeSensei(oldSensei);
                    embed = Messages.REMOVE_SENSEI.getMessage("%sensei%", oldSensei.getAsMention());
                } else {
                    embed = Messages.NOT_MY_SENSEI.getMessage("%sensei%", oldSensei.getAsMention());
                }
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage();
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}