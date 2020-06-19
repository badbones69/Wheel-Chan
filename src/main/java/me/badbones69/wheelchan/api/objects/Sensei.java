package me.badbones69.wheelchan.api.objects;

import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.entities.User;

public class Sensei {
    
    private String senseiID;
    private WheelChan wheelChan = WheelChan.getInstance();
    
    public Sensei(String userID) {
        this.senseiID = userID;
    }
    
    public String getID() {
        return senseiID;
    }
    
    public User getUser() {
        return wheelChan.getJDA().getUserById(senseiID);
    }
    
}