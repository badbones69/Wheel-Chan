package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TrackerStatsCommand {
    
    private static CardTracker cardTracker = CardTracker.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        Message message = e.getChannel().sendMessage(Messages.TRACKER_STATS.getMessage(e.getGuild(), getPlaceholders()).build()).complete();
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            e.getMessage().delete().complete();
            message.delete().complete();
        }, 90, TimeUnit.SECONDS);
    }
    
    private static Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%total_spawned_cards%", cardTracker.getTotalSpawn() + "");
        IntStream.range(1, 8).forEachOrdered(tier -> {
            placeholders.put("%spawned_tier_" + tier + "%", cardTracker.getSpawnedAmount(tier) + "");
            placeholders.put("%missed_tier_" + tier + "%", cardTracker.getMissedAmount(tier) + "");
        });
        return placeholders;
    }
    
}