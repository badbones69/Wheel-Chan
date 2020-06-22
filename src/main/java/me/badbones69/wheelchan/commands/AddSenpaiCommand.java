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

public class AddSenpaiCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        Guild guild = e.getGuild();
        EmbedBuilder embed = Messages.ADD_SENPIE_DESCRIPTION.getMessage(guild, "%senpai%", user.getAsMention());
        if (wheelChan.isSensei(user, e.getGuild())) {
            if (!message.getMentionedUsers().isEmpty()) {
                List<User> newSenpais = getNewSenpaiList(message.getMentionedUsers());
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
    
    private static List<User> getNewSenpaiList(List<User> senpaiList) {
        List<User> senpais = new ArrayList<>();
        for (User senpai : senpaiList) {
            if (!wheelChan.isSenpai(senpai)) {
                senpais.add(senpai);
            }
        }
        return senpais;
    }
    
    private static List<String> getSenpaiList(List<User> senpaiList) {
        List<String> senpais = new ArrayList<>();
        for (User senpai : senpaiList) {
            senpais.add("- " + senpai.getAsMention());
        }
        return senpais;
    }
    
}