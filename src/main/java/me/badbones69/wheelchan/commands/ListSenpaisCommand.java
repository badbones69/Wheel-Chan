package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class ListSenpaisCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage(Messages.LIST_SENPAIS.getMessage("%senpais%", getSenpieList()).build()).complete();
    }
    
    private static List<String> getSenpieList() {
        List<String> senpais = new ArrayList<>();
        for (String senpieName : wheelChan.getSenpieNames()) {
            senpais.add("- **" + senpieName + "**");
        }
        return senpais;
    }
    
}