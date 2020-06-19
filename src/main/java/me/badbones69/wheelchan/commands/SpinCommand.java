package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpie;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SpinCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    private static Random random = ThreadLocalRandom.current();
    
    public static void runCommand(MessageReceivedEvent e) {
        Senpie senpie = getRandomSenpie();
        e.getChannel().sendMessage(Messages.SPIN_WHEEL.getMessage("%senpie%", senpie.getUser().getAsMention()).build()).complete();
    }
    
    private static Senpie getRandomSenpie() {
        return wheelChan.getSenpais().get(random.nextInt(wheelChan.getSenpais().size()));
    }
    
}