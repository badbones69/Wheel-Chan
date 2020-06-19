package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Sensei;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class ListSenseisCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage(Messages.LIST_SENSEIS.getMessage("%senseis%", getSenseiList()).build()).complete();
    }
    
    private static List<String> getSenseiList() {
        List<String> senseis = new ArrayList<>();
        for (Sensei sensei : wheelChan.getSenseis()) {
            senseis.add("- " + sensei.getUser().getAsMention());
        }
        return senseis;
    }
    
}