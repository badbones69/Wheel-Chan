package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.FileManager;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReloadCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        Guild guild = e.getGuild();
        EmbedBuilder embed;
        if (wheelChan.isSensei(e.getAuthor(), e.getGuild())) {
            FileManager.getInstance().setup();
            wheelChan.load();
            embed = Messages.RELOAD.getMessage(guild);
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
}