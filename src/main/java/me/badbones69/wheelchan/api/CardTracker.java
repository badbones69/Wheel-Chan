package me.badbones69.wheelchan.api;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.objects.SpawnCard;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CardTracker {
    
    private static CardTracker instance = new CardTracker();
    private SpawnCard latestCard;
    private int totalSpawn;
    private String loggingChannelID;
    private Map<Integer, List<SpawnCard>> recentMissedCards = new HashMap<>();
    private Map<Integer, Integer> spawnAmount = new HashMap<>();
    private Map<Integer, Integer> missedAmount = new HashMap<>();
    
    public static CardTracker getInstance() {
        return instance;
    }
    
    public void load() {
        latestCard = new SpawnCard();
        FileConfiguration data = Files.DATA.getFile();
        loggingChannelID = data.getString("Tracker.Logging-Channel", "");
        totalSpawn = data.getInt("Tracker.Total-Spawns");
        IntStream.range(1, 8).forEachOrdered(tier -> {
            spawnAmount.put(tier, data.getInt("Tracker.Spawned." + tier));
            missedAmount.put(tier, data.getInt("Tracker.Missed." + tier));
            recentMissedCards.put(tier, data.getStringList("Tracker.Recent-Missed." + tier).stream().map(SpawnCard :: new).collect(Collectors.toList()));
        });
        System.out.println("Total-Spawns: " + totalSpawn);
        spawnAmount.forEach((tier, amount) -> System.out.println("Spawned Tier " + tier + ": " + amount));
        missedAmount.forEach((tier, amount) -> System.out.println("Missed Tier " + tier + ": " + amount));
        recentMissedCards.forEach((tier, cards) -> System.out.println("Recently Missed Tier " + tier + ": " + cards));
    }
    
    public void newSpawnCard(SpawnCard spawnCard) {
        latestCard = spawnCard;
        totalSpawn++;
        spawnAmount.put(spawnCard.getTier(), spawnAmount.getOrDefault(spawnCard.getTier(), 0) + 1);
    }
    
    public void newSpawnpackCard(SpawnCard spawnCard) {
        totalSpawn++;
        spawnAmount.put(spawnCard.getTier(), spawnAmount.getOrDefault(spawnCard.getTier(), 0) + 1);
    }
    
    public void setSpawnCardClaim(boolean claimed) {
        if (latestCard != null) {
            latestCard.setClaimed(claimed);
            if (!claimed) {
                if (!recentMissedCards.containsKey(latestCard.getTier())) {
                    recentMissedCards.put(latestCard.getTier(), new ArrayList<>(5));
                }
                List<SpawnCard> spawnCards = recentMissedCards.get(latestCard.getTier());
                spawnCards.add(latestCard);
                if (spawnCards.size() == 6) {
                    spawnCards.remove(spawnCards.get(0));
                }
                missedAmount.put(latestCard.getTier(), missedAmount.getOrDefault(latestCard.getTier(), 0) + 1);
            }
        }
        saveTracker();
    }
    
    public void saveTracker() {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Tracker.Logging-Channel", loggingChannelID);
        data.set("Tracker.Total-Spawns", totalSpawn);
        spawnAmount.forEach((key, value) -> data.set("Tracker.Spawned." + key, value));
        missedAmount.forEach((key, value) -> data.set("Tracker.Missed." + key, value));
        recentMissedCards.forEach((tier, cards) -> data.set("Tracker.Recent-Missed." + tier, cards.stream().map(SpawnCard :: toString).collect(Collectors.toList())));
        Files.DATA.saveFile();
    }
    
    public int getTotalSpawn() {
        return totalSpawn;
    }
    
    public SpawnCard getLatestCard() {
        return latestCard;
    }
    
    public List<SpawnCard> getRecentMissedCards(int tier) {
        return recentMissedCards.getOrDefault(tier, new ArrayList<>());
    }
    
    public int getSpawnedAmount(int tier) {
        return spawnAmount.getOrDefault(tier, 0);
    }
    
    public int getMissedAmount(int tier) {
        return missedAmount.getOrDefault(tier, 0);
    }
    
    public String getLoggingChannelID() {
        return loggingChannelID;
    }
    
    public void setLoggingChannelID(String loggingChannelID) {
        this.loggingChannelID = loggingChannelID;
    }
    
}