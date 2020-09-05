package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class ListSenseisCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        e.getChannel().sendMessage(Messages.LIST_SENSEIS.getMessage(e.getGuild(), "%senseis%", getSenseiList(server)).build()).complete();
    }
    
    private static List<String> getSenseiList(Server server) {
        return server.getSenseis().stream().map(sensei -> "- **" + sensei.getUser().getName() + "**").collect(Collectors.toList());
    }
    
}