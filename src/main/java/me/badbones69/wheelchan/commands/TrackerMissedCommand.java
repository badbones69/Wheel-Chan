package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.SpawnCard;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrackerMissedCommand {
    
    private static CardTracker cardTracker = CardTracker.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        Message message = e.getChannel().sendMessage(Messages.TRACKER_MISSED.getMessage(e.getGuild(), getPlaceholders()).build()).complete();
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            e.getMessage().delete().complete();
            message.delete().complete();
        }, 90, TimeUnit.SECONDS);
    }
    
    private static Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new HashMap<>();
        for (int tier = 1; tier < 8; tier++) {
            List<SpawnCard> spawnCards = new ArrayList<>(cardTracker.getRecentMissedCards(tier));
            Collections.reverse(spawnCards);
            for (int slot = 1; slot < 6; slot++) {
                SpawnCard card = slot <= spawnCards.size() ? spawnCards.get(slot - 1) : null;
                String cardString = card == null ? "No card" : card.getCardInfo();
                placeholders.put("%missed_tier_" + tier + "_" + slot + "%", cardString);
            }
        }
        return placeholders;
    }
    
}