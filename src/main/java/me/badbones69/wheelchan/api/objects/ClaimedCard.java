package me.badbones69.wheelchan.api.objects;

import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class ClaimedCard {
    
    private String cardURL;
    private SpawnCard card;
    private int issue;
    private User user;
    
    public ClaimedCard(String cardURL, SpawnCard card, int issue, User user) {
        this.cardURL = cardURL;
        this.card = card;
        this.issue = issue;
        this.user = user;
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
    
    public EmbedMessage getMessage() {
        return new EmbedMessage()
        .setThumbnailURL(cardURL)
        .setEmbedColor(Color.GREEN)
        .setTitle(":agree: Claimed")
        .setDescription(
        "Tier: **" + card.getTier() + " v" + issue + "**\n" +
        "Character: **" + card.getCharacterName() + "**\n" +
        "Claimed By: **" + user.getName() + "**");
    }
    
}