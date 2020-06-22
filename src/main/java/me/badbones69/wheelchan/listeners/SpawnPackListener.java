package me.badbones69.wheelchan.listeners;

import me.badbones69.wheelchan.api.WheelChan;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SpawnPackListener extends ListenerAdapter {
    
    private WheelChan wheelChan = WheelChan.getInstance();
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        User user = e.getAuthor();
        Message message = e.getMessage();
        if (user.isBot() && user.getId().equals("673362753489993749") && isSpawnPackMessage(message.getContentDisplay())) {
            User mentioned = message.getMentionedUsers().get(0);
            if (wheelChan.isSenpai(mentioned)) {
                wheelChan.getSenpai(mentioned).newCooldown();
            }
        }
    }
    
    private boolean isSpawnPackMessage(String message) {
        return message.contains("be the one to collect it!");
    }
    
}
