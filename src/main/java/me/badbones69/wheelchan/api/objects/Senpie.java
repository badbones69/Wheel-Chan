package me.badbones69.wheelchan.api.objects;

import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.entities.User;

public class Senpie {
    
    private String senpieID;
    private WheelChan wheelChan = WheelChan.getInstance();
    
    public Senpie(String userID) {
        this.senpieID = userID;
    }
    
    public String getID() {
        return senpieID;
    }
    
    public User getUser() {
        return wheelChan.getJDA().getUserById(senpieID);
    }
    
}