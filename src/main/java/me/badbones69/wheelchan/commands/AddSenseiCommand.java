package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class AddSenseiCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        EmbedBuilder embed = Messages.ADD_SENSEI_DESCRIPTION.getMessage("%sensei%", user.getAsMention());
        if (wheelChan.isSensei(user, e.getGuild())) {
            if (!message.getMentionedUsers().isEmpty()) {
                List<User> newSenseis = getNewSenseiList(message.getMentionedUsers(), e.getGuild());
                if (!newSenseis.isEmpty()) {
                    wheelChan.addSenseis(newSenseis);
                    embed = Messages.ADD_SENSEI.getMessage("%senseis%", getSenseiList(newSenseis));
                } else {
                    embed = Messages.ALREADY_MY_SENSEI.getMessage("%senseis%", getSenseiList(message.getMentionedUsers()));
                }
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage();
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    private static List<User> getNewSenseiList(List<User> senseiList, Guild guild) {
        List<User> senseis = new ArrayList<>();
        for (User sensei : senseiList) {
            if (!wheelChan.isSensei(sensei)) {
                senseis.add(sensei);
            }
        }
        return senseis;
    }
    
    private static List<String> getSenseiList(List<User> senseiList) {
        List<String> senseis = new ArrayList<>();
        for (User sensei : senseiList) {
            senseis.add("- " + sensei.getAsMention());
        }
        return senseis;
    }
    
}