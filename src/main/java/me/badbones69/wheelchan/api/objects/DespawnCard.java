package me.badbones69.wheelchan.api.objects;

import java.awt.*;

public class DespawnCard {
    
    private String cardURL;
    private SpawnCard card;
    
    public DespawnCard(String cardURL, SpawnCard card) {
        this.cardURL = cardURL;
        this.card = card;
    }
    
    public String getCardURL() {
        return cardURL;
    }
    
    public SpawnCard getCard() {
        return card;
    }
    
    public EmbedMessage getMessage() {
        return new EmbedMessage()
        .setThumbnailURL(cardURL)
        .setEmbedColor(Color.RED)
        .setTitle(":disagree: Despawned")
        .setDescription(
        "Tier: **" + card.getTier() + "**\n" +
        "Character: **" + card.getCharacterName() + "**\n");
    }
    
}