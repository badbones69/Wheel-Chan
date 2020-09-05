package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveSenpaiCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        Guild guild = e.getGuild();
        EmbedBuilder embed = Messages.REMOVE_SENPIE_DESCRIPTION.getMessage(guild, "%senpai%", user.getAsMention());
        if (server.isSensei(user, e.getGuild())) {
            if (!message.getMentionedUsers().isEmpty()) {
                User oldSenpai = message.getMentionedUsers().get(0);
                if (wheelChan.isSenpai(oldSenpai)) {
                    wheelChan.removeSenpai(oldSenpai);
                    embed = Messages.REMOVE_SENPIE.getMessage(guild, "%senpai%", oldSenpai.getAsMention());
                } else {
                    embed = Messages.NOT_MY_SENPIE.getMessage(guild, "%senpai%", oldSenpai.getAsMention());
                }
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}