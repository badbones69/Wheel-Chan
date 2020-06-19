package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.FileManager;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReloadCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        FileManager.getInstance().setup();
        wheelChan.load();
        e.getChannel().sendMessage(Messages.RELOAD.getMessage().build()).complete();
    }
    
}