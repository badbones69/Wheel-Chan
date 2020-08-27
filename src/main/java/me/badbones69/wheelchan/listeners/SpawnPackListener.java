package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.Senpai;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SpawnPackListener extends ListenerAdapter {
    
    private WheelChan wheelChan = WheelChan.getInstance();
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!wheelChan.isTesting()) {
            User user = e.getAuthor();
            Message message = e.getMessage();
            if (wheelChan.isShoob(user)) {
                if (isSpawnPackMessage(message.getContentDisplay())) {
                    User mentioned = message.getMentionedUsers().get(0);
                    if (wheelChan.isSenpai(mentioned)) {
                        wheelChan.getSenpai(mentioned).newCooldown();
                    }
                } else if (isSpawnPackDenyMessage(message.getContentDisplay())) {
                    User mentioned = message.getMentionedUsers().get(0);
                    Senpai senpai = wheelChan.getSenpai(mentioned);
                    if (wheelChan.isSenpai(mentioned) && senpai.isCooldownOver()) {
                        senpai.setUnknownCooldown();
                    }
                }
            }
        }
    }
    
    public static boolean isSpawnPackMessage(String message) {
        return message.contains("be the one to collect it!");
    }
    
    private boolean isSpawnPackDenyMessage(String message) {
        return message.contains("you don't have a pack to spawn at the moment.");
    }
    
}