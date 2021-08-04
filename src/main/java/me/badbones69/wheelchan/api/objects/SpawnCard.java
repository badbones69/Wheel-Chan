package me.badbones69.wheelchan.api.objects;

import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Calendar;

public class SpawnCard {
    
    private WheelChan wheelChan = WheelChan.getInstance();
    private int tier;
    private String characterName;
    private Calendar spawnTime;
    private boolean claimed;
    private String cardURL;
    
    public SpawnCard() {
        tier = 0;
        characterName = "Unknown Character";
        spawnTime = Calendar.getInstance();
        claimed = false;
        cardURL = "https://animesoul.com";
    }
    
    public SpawnCard(MessageEmbed embed) {
        tier = Integer.parseInt(trimURL(embed.getImage().getUrl()));
        characterName = embed.getTitle();
        spawnTime = Calendar.getInstance();
        claimed = false;
        cardURL = embed.getImage().getProxyUrl();
    }

    public SpawnCard(String convertString) {
        String[] split = convertString.split(", ");
        for (String option : split) {
            switch (option.split(":")[0]) {
                case "name":
                    characterName = option.replace("name:", "");
                    break;
                case "tier":
                    tier = Integer.parseInt(option.replace("tier:", ""));
                    break;
                case "claimed":
                    claimed = Boolean.parseBoolean(option.replace("claimed:", ""));
                    break;
                case "spawn-time":
                    spawnTime = Calendar.getInstance();
                    spawnTime.setTimeInMillis(Long.parseLong(option.replace("spawn-time:", "")));
            }
        }
    }
    
    public int getTier() {
        return tier;
    }
    
    public String getCharacterName() {
        return characterName;
    }
    
    public Calendar getSpawnTime() {
        return spawnTime;
    }
    
    public boolean isClaimed() {
        return claimed;
    }
    
    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }
    
    public String getCardURL() {
        return cardURL;
    }
    
    @Override
    public String toString() {
        return "name:" + characterName + ", tier:" + tier + ", claimed:" + claimed + ", spawn-time:" + spawnTime.getTimeInMillis();
    }

    private String trimURL(String cardURL){
        return cardURL.replace("https://cdn.animesoul.com/images/cards/", "").substring(0, 1);
    }
    
}