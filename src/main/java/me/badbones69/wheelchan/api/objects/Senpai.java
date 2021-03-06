package me.badbones69.wheelchan.api.objects;

import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.entities.User;

import java.util.Calendar;

public class Senpai {
    
    private String senpaiID;
    private long spawnPackCooldown;
    private String cacheName;
    private WheelChan wheelChan = WheelChan.getInstance();
    
    public Senpai(String userID) {
        this.senpaiID = userID;
        this.spawnPackCooldown = 0;
        cacheName = "Failed to get user!";
    }
    
    public Senpai(String userID, String cacheName) {
        this.senpaiID = userID;
        this.spawnPackCooldown = 0;
        this.cacheName = cacheName;
    }
    
    public Senpai(String userID, long spawnPackCooldown) {
        this.senpaiID = userID;
        this.spawnPackCooldown = spawnPackCooldown;
        cacheName = "Failed to get user!";
    }
    
    public Senpai(String userID, long spawnPackCooldown, String cacheName) {
        this.senpaiID = userID;
        this.spawnPackCooldown = spawnPackCooldown;
        this.cacheName = cacheName;
    }
    
    public String getID() {
        return senpaiID;
    }
    
    public User getUser() {
        return wheelChan.getJDA().getUserById(senpaiID);
    }
    
    public String getCacheName() {
        return cacheName;
    }
    
    public boolean hasCooldown() {
        return spawnPackCooldown != 0;
    }
    
    public long getSpawnPackCooldown() {
        return spawnPackCooldown;
    }
    
    public void setUnknownCooldown() {
        spawnPackCooldown = 0;
    }
    
    public void newCooldown() {
        newCooldown(Calendar.getInstance());
    }
    
    public void newCooldown(long time) {
        Calendar cooldown = Calendar.getInstance();
        cooldown.setTimeInMillis(time);
        newCooldown(cooldown);
    }
    
    public void newCooldown(Calendar spawned) {
        spawned.add(Calendar.DAY_OF_MONTH, 2);
        spawnPackCooldown = spawned.getTimeInMillis();
        wheelChan.saveSenpai(this);
    }
    
    public boolean isCooldownOver() {
        Calendar cooldown = Calendar.getInstance();
        cooldown.setTimeInMillis(spawnPackCooldown);
        return Calendar.getInstance().after(cooldown);
    }
    
    public String getCooldownString() {
        return wheelChan.convertToTime(spawnPackCooldown);
    }
    
}