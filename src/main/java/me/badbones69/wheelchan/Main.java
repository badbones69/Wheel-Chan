package me.badbones69.wheelchan;

import me.badbones69.wheelchan.api.FileManager;
import me.badbones69.wheelchan.api.WheelChan;

public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        FileManager.getInstance().logInfo(true).setup();
        WheelChan.getInstance().load();
    }
    
}