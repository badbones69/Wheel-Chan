package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class TrackerStatsCommand {
    
    private static CardTracker cardTracker = CardTracker.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        Map<String, String> placeholders = getPlaceholders();
        EmbedBuilder embed = new EmbedBuilder()
        .setTitle("Information of all spawned and missed cards.")
        .setColor(Color.CYAN)
        .setDescription(Messages.replacePlaceholders(placeholders,
        "**__Total Spawned Cards:__**\n" +
        "Total Cards: **%total_spawned_cards%**"))
        .addField("**__Spawned Cards:__**",
        Messages.replacePlaceholders(placeholders,
        "Tier 1: **%spawned_tier_1%**\n" +
        "Tier 2: **%spawned_tier_2%**\n" +
        "Tier 3: **%spawned_tier_3%**\n" +
        "Tier 4: **%spawned_tier_4%**\n" +
        "Tier 5: **%spawned_tier_5%**\n" +
        "Tier 6: **%spawned_tier_6%**\n"), true)
        .addField("**__Missed Cards:__**",
        Messages.replacePlaceholders(placeholders,
        "Tier 1: **%missed_tier_1%**\n" +
        "Tier 2: **%missed_tier_2%**\n" +
        "Tier 3: **%missed_tier_3%**\n" +
        "Tier 4: **%missed_tier_4%**\n" +
        "Tier 5: **%missed_tier_5%**\n" +
        "Tier 6: **%missed_tier_6%**\n"), true)
        .setTimestamp(Instant.now());
        e.getChannel().sendMessage(embed.build()).complete();
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