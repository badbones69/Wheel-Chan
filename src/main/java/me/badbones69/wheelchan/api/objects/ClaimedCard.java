package me.badbones69.wheelchan.api.objects;

import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class ClaimedCard {
    
    private String cardURL;
    private SpawnCard card;
    private int issue;
    private User user;
    private boolean normalSpawn;
    
    public ClaimedCard(String cardURL, SpawnCard card, int issue, User user, boolean normalSpawn) {
        this.cardURL = cardURL;
        this.card = card;
        this.issue = issue;
        this.user = user;
        this.normalSpawn = normalSpawn;
    }
    
    public String getCardURL() {
        return cardURL;
    }
    
    public SpawnCard getCard() {
        return card;
    }
    
    public int getIssue() {
        return issue;
    }
    
    public User getUser() {
        return user;
    }
    
    public boolean isNormalSpawn() {
        return normalSpawn;
    }
    
    public EmbedMessage getMessage() {
        return new EmbedMessage()
        .setThumbnailURL(cardURL)
        .setColor(normalSpawn ? Color.GREEN : Color.MAGENTA)
        .setTitle(normalSpawn ? ":agree: Claimed" : ":agree: Claimed Spawn Pack")
        .setDescription(
        "Tier: **" + card.getTier() + " v" + issue + "**\n" +
        "Character: **" + card.getCharacterName() + "**\n" +
        "Claimed By: **" + user.getName() + "**");
    }
    
}