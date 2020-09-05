package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpai;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SpinCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    private static Random random = new Random();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        Senpai senpai = getRandomSenpai(server);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%senpai%", senpai.getUser().getName());
        placeholders.put("%senpai_id%", senpai.getUser().getId());
        e.getChannel().sendMessage(Messages.SPIN_WHEEL.getMessage(e.getGuild(), placeholders).build()).complete();
    }
    
    public static Senpai getRandomSenpai(Server server) {
        random.setSeed(System.nanoTime());
        List<Senpai> senpais = wheelChan.getSenpais(server);
        return senpais.get(random.nextInt(senpais.size()));
    }
    
}