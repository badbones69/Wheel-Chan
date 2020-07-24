package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.SpawnCard;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CardSpawnListener extends ListenerAdapter {
    
    private WheelChan wheelChan = WheelChan.getInstance();
    private CardTracker cardTracker = CardTracker.getInstance();
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        if (wheelChan.isShoob(user)) {
            SpawnCard card;
            if (!message.getEmbeds().isEmpty()) {
                MessageEmbed embed = message.getEmbeds().get(0);
                //New card spawns
                if (embed.getTitle() != null && isSpawnMessage(embed.getTitle())) {
                    card = new SpawnCard(embed);
                    cardTracker.newSpawnCard(card);
                    System.out.println("Card has Spawned: " + card);
                    //Card is claimed
                } else if (embed.getDescription() != null && isClaimedMessage(embed.getDescription())) {
                    cardTracker.setSpawnCardClaim(true);
                    card = cardTracker.getLatestCard();
                    System.out.println("Card has been Claimed: " + card);
                }
                //Card despawns
            } else if (isDespawnMessage(message.getContentDisplay())) {
                cardTracker.setSpawnCardClaim(false);
                card = cardTracker.getLatestCard();
                System.out.println("Card has Despawned: " + card);
            }
        }
    }
    
    private boolean isSpawnMessage(String message) {
        return message.contains("Tier: ");
    }
    
    private boolean isClaimedMessage(String message) {
        return message.contains("got the card!");
    }
    
    private boolean isDespawnMessage(String message) {
        return message.contains("Looks like nobody got the card this time.");
    }
    
}