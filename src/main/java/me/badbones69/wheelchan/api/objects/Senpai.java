package me.badbones69.wheelchan.api.objects;

import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.entities.User;

import java.util.Calendar;

public class Senpai {
    
    private String senpaiID;
    private long spawnPackCooldown;
    private WheelChan wheelChan = WheelChan.getInstance();
    
    public Senpai(String userID) {
        this.senpaiID = userID;
        this.spawnPackCooldown = 0;
    }
    
    public Senpai(String userID, long spawnPackCooldown) {
        this.senpaiID = userID;
        this.spawnPackCooldown = spawnPackCooldown;
    }
    
    public String getID() {
        return senpaiID;
    }
    
    public User getUser() {
        return wheelChan.getJDA().getUserById(senpaiID);
    }
    
    public boolean hasCooldown() {
        return spawnPackCooldown != 0;
    }
    
    public long getSpawnPackCooldown() {
        return spawnPackCooldown;
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
        return convertToTime(spawnPackCooldown);
    }
    
    private String convertToTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int total = ((int) (cal.getTimeInMillis() / 1000) - (int) (Calendar.getInstance().getTimeInMillis() / 1000));
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        for (; total > 86400; total -= 86400, day++) ;
        for (; total > 3600; total -= 3600, hour++) ;
        for (; total >= 60; total -= 60, minute++) ;
        second += total;
        String message = "";
        if (day > 0) message += day + "d, ";
        if (day > 0 || hour > 0) message += hour + "h, ";
        if (day > 0 || hour > 0 || minute > 0) message += minute + "m, ";
        if (day > 0 || hour > 0 || minute > 0 || second > 0) message += second + "s, ";
        if (message.length() < 2) {
            message = "Now";
        } else {
            message = message.substring(0, message.length() - 2);
        }
        return message;
    }
    
}