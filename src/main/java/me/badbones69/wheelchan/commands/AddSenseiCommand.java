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

public class AddSenseiCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        Guild guild = e.getGuild();
        EmbedBuilder embed = Messages.ADD_SENSEI_DESCRIPTION.getMessage(guild, "%sensei%", user.getAsMention());
        if (server.isSensei(user, e.getGuild())) {
            if (!message.getMentionedUsers().isEmpty()) {
                List<User> newSenseis = getNewSenseiList(message.getMentionedUsers(), server);
                if (!newSenseis.isEmpty()) {
                    server.addSenseis(newSenseis);
                    embed = Messages.ADD_SENSEI.getMessage(guild, "%senseis%", getSenseiList(newSenseis));
                } else {
                    embed = Messages.ALREADY_MY_SENSEI.getMessage(guild, "%senseis%", getSenseiList(message.getMentionedUsers()));
                }
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    private static List<User> getNewSenseiList(List<User> senseiList, Server server) {
        return senseiList.stream().filter(sensei -> !server.isSensei(sensei)).collect(Collectors.toList());
    }
    
    private static List<String> getSenseiList(List<User> senseiList) {
        return senseiList.stream().map(sensei -> "- " + sensei.getAsMention()).collect(Collectors.toList());
    }
    
}