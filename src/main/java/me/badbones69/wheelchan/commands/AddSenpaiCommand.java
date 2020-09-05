package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class AddSenpaiCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        Guild guild = e.getGuild();
        EmbedBuilder embed = Messages.ADD_SENPIE_DESCRIPTION.getMessage(guild, "%senpai%", user.getAsMention());
        if (server.isSensei(user, e.getGuild())) {
            if (!message.getMentionedUsers().isEmpty()) {
                List<User> newSenpais = getNewSenpaiList(message.getMentionedUsers(), server);
                if (!newSenpais.isEmpty()) {
                    wheelChan.addSenpais(newSenpais);
                    embed = Messages.ADD_SENPIE.getMessage(guild, "%senpais%", getSenpaiList(newSenpais));
                } else {
                    embed = Messages.ALREADY_MY_SENPIE.getMessage(guild, "%senpais%", getSenpaiList(message.getMentionedUsers()));
                }
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    private static List<User> getNewSenpaiList(List<User> senpaiList, Server server) {
        return senpaiList.stream().filter(senpai -> !wheelChan.isSenpai(senpai)).collect(Collectors.toList());
    }
    
    private static List<String> getSenpaiList(List<User> senpaiList) {
        return senpaiList.stream().map(senpai -> "- " + senpai.getAsMention()).collect(Collectors.toList());
    }
    
}