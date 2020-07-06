package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpai;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class ListSenpaisCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        e.getChannel().sendMessage(Messages.LIST_SENPAIS.getMessage(e.getGuild(), "%senpais%", getSenpaiList()).build()).complete();
    }
    
    private static List<String> getSenpaiList() {
        List<String> senpais = new ArrayList<>();
        for (Senpai senpai : wheelChan.getSenpais()) {
            senpais.add("- " + getCooldown(senpai));
        }
        return senpais;
    }
    
    private static String getCooldown(Senpai senpai) {
        String cooldownString;
        //Was getting weird NPE from this line so needed to add some checks.
        String mention = "**" + senpai != null && senpai.getUser() != null ? senpai.getUser().getName() : "Failed to get user!" + "**";
        if (senpai.hasCooldown()) {
            if (senpai.isCooldownOver()) {
                cooldownString = ":agree: " + mention;
            } else {
                cooldownString = ":disagree: " + mention + ": `" + senpai.getCooldownString() + "`";
            }
        } else {
            cooldownString = ":Bully: " + mention;
        }
        return cooldownString;
    }
    
}