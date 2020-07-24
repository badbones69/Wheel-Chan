package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.SpawnCard;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.*;

public class TrackerMissedCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    private static CardTracker cardTracker = CardTracker.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        Map<String, String> placeholders = getPlaceholders();
        EmbedBuilder embed = new EmbedBuilder()
        .setTitle("List of all recently missed cards.")
        .setColor(Color.RED)
        .addField("**__Tier 1:__**",
        Messages.replacePlaceholders(placeholders,
        "1: %missed_tier_1_1%\n" +
        "2: %missed_tier_1_2%\n" +
        "3: %missed_tier_1_3%\n" +
        "4: %missed_tier_1_4%\n" +
        "5: %missed_tier_1_5%\n"), true)
        .addField("**__Tier 2:__**",
        Messages.replacePlaceholders(placeholders,
        "1: %missed_tier_2_1%\n" +
        "2: %missed_tier_2_2%\n" +
        "3: %missed_tier_2_3%\n" +
        "4: %missed_tier_2_4%\n" +
        "5: %missed_tier_2_5%\n"), true)
        .addField("**__Tier 3:__**",
        Messages.replacePlaceholders(placeholders,
        "1: %missed_tier_3_1%\n" +
        "2: %missed_tier_3_2%\n" +
        "3: %missed_tier_3_3%\n" +
        "4: %missed_tier_3_4%\n" +
        "5: %missed_tier_3_5%\n"), true)
        .addField("**__Tier 4:__**",
        Messages.replacePlaceholders(placeholders,
        "1: %missed_tier_4_1%\n" +
        "2: %missed_tier_4_2%\n" +
        "3: %missed_tier_4_3%\n" +
        "4: %missed_tier_4_4%\n" +
        "5: %missed_tier_4_5%\n"), true)
        .addField("**__Tier 5:__**",
        Messages.replacePlaceholders(placeholders,
        "1: %missed_tier_5_1%\n" +
        "2: %missed_tier_5_2%\n" +
        "3: %missed_tier_5_3%\n" +
        "4: %missed_tier_5_4%\n" +
        "5: %missed_tier_5_5%\n"), true)
        .addField("**__Tier 6:__**",
        Messages.replacePlaceholders(placeholders,
        "1: %missed_tier_6_1%\n" +
        "2: %missed_tier_6_2%\n" +
        "3: %missed_tier_6_3%\n" +
        "4: %missed_tier_6_4%\n" +
        "5: %missed_tier_6_5%\n"), true)
        .setTimestamp(Instant.now());
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    private static Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new HashMap<>();
        for (int tier = 1; tier < 8; tier++) {
            List<SpawnCard> spawnCards = new ArrayList<>(cardTracker.getRecentMissedCards(tier));
            Collections.reverse(spawnCards);
            for (int slot = 1; slot < 6; slot++) {
                SpawnCard card = slot <= spawnCards.size() ? spawnCards.get(slot - 1) : null;
                String cardString = card == null ? "No card" : "**" + card.getCharacterName() + "** __" + wheelChan.convertToTime(card.getSpawnTime(), true) + "__";
                placeholders.put("%missed_tier_" + tier + "_" + slot + "%", cardString);
            }
        }
        return placeholders;
    }
    
}