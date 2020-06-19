package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        EmbedBuilder embed;
        if (wheelChan.isSensei(e.getAuthor(), e.getGuild())) {
            wheelChan.clearAllSenpais();
            embed = Messages.CLEAR_SENPAIS.getMessage();
        } else {
            embed = Messages.NO_PERMISSION.getMessage();
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}