package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage(Messages.HELP.getMessage().build()).complete();
    }
    
}