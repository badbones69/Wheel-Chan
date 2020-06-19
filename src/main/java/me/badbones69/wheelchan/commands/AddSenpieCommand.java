package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class AddSenpieCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        EmbedBuilder embed = Messages.ADD_SENPIE_DESCRIPTION.getMessage("%senpie%", user.getAsMention());
        if (wheelChan.isSensei(user, e.getGuild())) {
            if (!message.getMentionedUsers().isEmpty()) {
                List<User> newSenpais = getNewSenpieList(message.getMentionedUsers());
                if (!newSenpais.isEmpty()) {
                    wheelChan.addSenpais(newSenpais);
                    embed = Messages.ADD_SENPIE.getMessage("%senpais%", getSenpieList(newSenpais));
                } else {
                    embed = Messages.ALREADY_MY_SENPIE.getMessage("%senpais%", getSenpieList(message.getMentionedUsers()));
                }
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage();
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    private static List<User> getNewSenpieList(List<User> senpieList) {
        List<User> senpais = new ArrayList<>();
        for (User senpie : senpieList) {
            if (!wheelChan.isSenpie(senpie)) {
                senpais.add(senpie);
            }
        }
        return senpais;
    }
    
    private static List<String> getSenpieList(List<User> senpieList) {
        List<String> senpais = new ArrayList<>();
        for (User senpie : senpieList) {
            senpais.add("- " + senpie.getAsMention());
        }
        return senpais;
    }
    
}