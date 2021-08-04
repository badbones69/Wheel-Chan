package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.CardTracker;
import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.ClaimedCard;
import me.badbones69.wheelchan.api.objects.DespawnCard;
import me.badbones69.wheelchan.api.objects.Server;
import me.badbones69.wheelchan.api.objects.SpawnCard;
import me.badbones69.wheelchan.events.SpawnTimer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.util.Objects;

public class CardSpawnListener extends ListenerAdapter {
    
    private final WheelChan wheelChan = WheelChan.getInstance();
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        Server server = wheelChan.getServer(e.getGuild());
        CardTracker cardTracker = server.getCardTracker();
        User user = e.getAuthor();
        Message message = e.getMessage();
        MessageChannel logging;
        try {
            logging = e.getGuild().getTextChannelById(server.getCardTracker().getLoggingChannelID());
        } catch (Exception exception) {
            logging = null;
        }
        boolean isLogging = !wheelChan.isTesting() && logging != null;
        String serverName = server.getServerName() + " ";
        if (wheelChan.isShoob(user)) {
            SpawnCard card;
            if (!message.getEmbeds().isEmpty()) {
                MessageEmbed embed = message.getEmbeds().get(0);
                //New card spawns
                if (embed.getTitle() != null && isSpawnMessage(Objects.requireNonNull(embed.getDescription()))){
                    if (SpawnPackListener.isSpawnPackMessage(message.getContentDisplay())) {
                        card = new SpawnCard(embed);
                        cardTracker.newSpawnpackCard(card);
                        System.out.println(serverName + "Spawn Pack: " + card);
                    } else {
                        card = new SpawnCard(embed);
                        cardTracker.newSpawnCard(card);
                        if (!wheelChan.isTesting()) new SpawnTimer(e.getChannel(), card);
                        System.out.println(serverName + "Spawned: " + card);
                    }
                    //Card is claimed
                } else if (embed.getDescription() != null && isClaimedMessage(embed.getDescription())) {
                    //Card is normal spawn.
                    if (embed.getDescription().toLowerCase().contains(cardTracker.getLatestSpawnCard().getCharacterName().toLowerCase())) {
                        cardTracker.setSpawnCardClaim(true);
                        card = cardTracker.getLatestSpawnCard();
                        System.out.println(serverName + "Claimed: " + card);
                        User claimed = wheelChan.getJDA().getUserById(embed.getDescription().split("<@")[1].split(">")[0]);
                        int issue = Integer.parseInt(embed.getDescription().split("#: ")[1].split("\\.")[0].replace("`", ""));
                        if (isLogging) logging.sendMessage(new ClaimedCard(card.getCardURL(), card, issue, claimed, true).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
                        //Card is spawn pack
                    } else if (embed.getDescription().toLowerCase().contains(cardTracker.getLatestPackCard().getCharacterName().toLowerCase())) {
                        card = cardTracker.getLatestPackCard();
                        System.out.println(serverName + "Claimed Spawn Pack: " + card);
                        User claimed = wheelChan.getJDA().getUserById(embed.getDescription().split("<@")[1].split(">")[0]);
                        int issue = Integer.parseInt(embed.getDescription().split("#: ")[1].split("\\.")[0].replace("`", ""));
                        if (isLogging) logging.sendMessage(new ClaimedCard(card.getCardURL(), card, issue, claimed, false).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
                    }
                }
                //Card despawns
            } else if (isDespawnMessage(Objects.requireNonNull(message.getEmbeds().get(0).getDescription()))) {
                cardTracker.setSpawnCardClaim(false);
                card = cardTracker.getLatestSpawnCard();
                System.out.println(serverName + "Despawned: " + card);
                if (isLogging) logging.sendMessage(new DespawnCard(card.getCardURL(), card).getMessage().getEmbedMessage(e.getGuild()).setTimestamp(Instant.now()).build()).complete();
            }
        }
    }
    
    private boolean isSpawnMessage(String message) {
        return message.contains("To claim, ");
    }
    
    public boolean isClaimedMessage(String message) {
        return message.contains("got the");
    }
    
    private boolean isDespawnMessage(String message) {
        return message.contains("Looks like no one got the card");
    }
    
}